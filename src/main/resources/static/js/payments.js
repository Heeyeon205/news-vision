document.addEventListener('DOMContentLoaded', function() {
// 아임포트 코드
    const paymentBody = document.querySelector('.payments-body');
    const impCode = paymentBody.dataset.impCode;
    const cardPayButton = document.getElementById('cardPay');
    const kakaoPayButton = document.getElementById('kakaoPay');
    function handlePayment(pg, payMethod) {
        console.log("handlePayment");
        console.log(pg);
        console.log(payMethod);

        var order = {
            productId: 1,
            productName: '상품1',
            price: 100,
            quantity: 1
        };

        // 결제하기 버튼 클릭 시 결제 요청
        IMP.init(impCode);
        console.log("IMP.init 호출:", impCode);
        IMP.request_pay({
            pg: pg,
            pay_method: payMethod,
            merchant_uid: '212R3A11TD233AAC', // 주문번호 생성
            name: '상품1',
            amount: 100, // 결제 가격
            buyer_name: '송명보',
            buyer_tel: '010-4849-6063'
        }, function (rsp) {
            // ... (나머지 콜백 함수 코드) ...
        });
    }

    cardPayButton.addEventListener('click', function() {
        handlePayment('html5_inicis.INIpayTest', 'card');
    });

    kakaoPayButton.addEventListener('click', function() {
        handlePayment('kakaopay', 'card');
    });

});