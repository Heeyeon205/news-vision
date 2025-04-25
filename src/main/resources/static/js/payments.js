if (!window.paymentScriptLoaded) {
    window.paymentScriptLoaded = true;

    document.addEventListener('DOMContentLoaded', function () {
        const paymentBody = document.querySelector('.payments-body');
        const impCode = paymentBody.dataset.impCode;
        const planButtons = [
            document.getElementById('plan1Month'),
            document.getElementById('plan3Month'),
            document.getElementById('plan6Month')
        ];

        let isProcessing = false;
        const debounceDelay = 500;

        async function handlePayment(pg, payMethod, period, price, productName, event) {
            event.stopPropagation();
            event.preventDefault();

            if (isProcessing) {
                console.log('이미 결제 처리 중입니다.');
                return;
            }
            isProcessing = true;

            setTimeout(() => {
                isProcessing = false;
            }, debounceDelay);

            try {
                let token = localStorage.getItem('accessToken');
                if (!token) {
                    alert('로그인이 필요합니다.');
                    return;
                }

                const order = {
                    pg: pg,
                    payMethod: payMethod,
                    productId: period, // 예: 1, 3, 6
                    productName: productName,
                    price: price,
                    quantity: 1,
                    subscriptionPeriod: period // 구독 기간 추가
                };

                let initResponse = await fetch('/api/v1/payment/init', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(order)
                });

                if (initResponse.status === 401) {
                    token = await refreshToken();
                    if (!token) {
                        alert('토큰 갱신에 실패했습니다. 다시 로그인해주세요.');
                        return;
                    }
                    initResponse = await fetch('/api/v1/payment/init', {
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + token,
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(order)
                    });
                }

                if (!initResponse.ok) {
                    const errorText = await initResponse.text();
                    console.error('결제 초기화 실패:', errorText);
                    alert('결제 초기화에 실패했습니다: ' + errorText);
                    return;
                }

                const initData = await initResponse.json();
                console.log('결제 초기화 응답:', initData);

                let success, isPaid, pgData, payMethodData, merchantUid, name, amount, buyerName;

                if (initData.success !== undefined) {
                    success = initData.success;
                    isPaid = initData.isPaid;
                    pgData = initData.pg;
                    payMethodData = initData.pay_method;
                    merchantUid = initData.merchant_uid;
                    name = initData.name;
                    amount = initData.amount;
                    buyerName = initData.buyer_name;
                } else {
                    alert('서버 응답 형식이 올바르지 않습니다.');
                    return;
                }

                if (!success) {
                    alert(initData.message || '결제 초기화에 실패했습니다.');
                    return;
                }

                if (isPaid) {
                    alert('이미 구독 중입니다. 중복 결제는 허용되지 않습니다.');
                    return;
                }

                IMP.init(impCode);
                IMP.request_pay(
                    {
                        pg: pgData,
                        pay_method: payMethodData,
                        merchant_uid: merchantUid,
                        name: name,
                        amount: amount,
                        buyer_name: buyerName
                    },
                    async function (rsp) {
                        try {
                            if (rsp.success) {
                                let authToken = token;
                                let validationResponse = await fetch(`/api/v1/payment/validation/${rsp.imp_uid}`, {
                                    method: 'POST',
                                    headers: {
                                        'Authorization': 'Bearer ' + authToken
                                    }
                                });

                                if (validationResponse.status === 401) {
                                    authToken = await refreshToken();
                                    if (!authToken) {
                                        alert('토큰 갱신에 실패했습니다. 다시 로그인해주세요.');
                                        return;
                                    }
                                    validationResponse = await fetch(`/api/v1/payment/validation/${rsp.imp_uid}`, {
                                        method: 'POST',
                                        headers: {
                                            'Authorization': 'Bearer ' + authToken
                                        }
                                    });
                                }

                                if (!validationResponse.ok) {
                                    alert('결제 검증에 실패했습니다.');
                                    return;
                                }

                                const data = await validationResponse.json();
                                console.log('결제 검증 응답:', data);

                                if (!data.response || !data.response.amount) {
                                    alert('결제 검증 데이터가 올바르지 않습니다.');
                                    return;
                                }

                                if (order.price === Number(data.response.amount)) {
                                    order.impUid = rsp.imp_uid;
                                    order.merchantUid = rsp.merchant_uid;

                                    const orderResponse = await fetch('/api/v1/payment/order', {
                                        method: 'POST',
                                        headers: {
                                            'Authorization': 'Bearer ' + authToken,
                                            'Content-Type': 'application/json'
                                        },
                                        body: JSON.stringify(order)
                                    });

                                    if (!orderResponse.ok) {
                                        alert('주문 정보 저장에 실패했습니다.');
                                        return;
                                    }

                                    const res = await orderResponse.text();
                                    console.log('주문 저장 결과:', res);
                                    if (res.includes("성공")) {
                                        alert('결제가 완료되었습니다. 구독이 활성화되었습니다.');
                                        window.location.href = '/';
                                    } else {
                                        alert('주문 정보 저장에 실패했습니다: ' + res);
                                    }
                                } else {
                                    console.log('금액 불일치:', order.price, data.response.amount);
                                    alert('결제 금액이 유효하지 않습니다.');
                                }
                            } else {
                                alert('결제에 실패했습니다: ' + rsp.error_msg);
                            }
                        } catch (error) {
                            console.error('결제 처리 중 오류:', error);
                            alert('결제 처리 중 오류가 발생했습니다: ' + error.message);
                        }
                    }
                );
            } catch (error) {
                console.error('결제 처리 중 오류:', error);
                alert('결제 처리 중 오류가 발생했습니다: ' + error.message);
            }
        }

        // 각 버튼에 이벤트 리스너 추가
        planButtons.forEach(button => {
            if (button) {
                button.onclick = (event) => {
                    const period = parseInt(button.dataset.period);
                    const price = parseInt(button.dataset.price);
                    const productName = button.dataset.name;
                    handlePayment('html5_inicis.INIpayTest', 'card', period, price, productName, event);
                };
            }
        });
    });
}