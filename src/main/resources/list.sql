INSERT INTO users(is_paid, badge_id, create_at, email, image, introduce, nickname, password, provider_id, username, provider) VALUES
(false,2,NOW(),'expmasdasda@asdad3.com','dfgag.jpg','asdad','sga','asdasdasda',null,'ㅁㄴㅇㅁㄴㅇㅁㄴㅇ,luyf','KAKAO');

INSERT INTO news (title, content, category_id, created_at, view, user_id)
VALUES
    ('AI 뉴스 대폭발', 'AI 관련 뉴스 본문입니다.', 1, NOW(), 15,  33);


INSERT INTO boards (is_reported, category_id, create_at, news_id, user_id, view, content, image) VALUES
                                                                                                     (false,2,20250417,4,34,456,'adads.jpg','fhuyfgilyfly'),
                                                                                                     (false,2,20250418,4,34,456,'adads.jpg','fhuyfgilyfly');


INSERT INTO polls (title, content, expired_at, created_at , user_id)
VALUES ('afafaf','afasfasf',NOW(),NOW(),12),
       ('asdasdasd','asdasdasd',NOW(),NOW(),12),
       ('asdasdasd','asdasdasd',NOW(),NOW(),12);


INSERT INTO board_reports(board_id, user_id) VALUES (52,12),(53,12),(54,12),(55,12);

INSERT INTO categories(name)VALUES ('ㅁㄴㅇ'),('asd'),('afafjno');

INSERT INTO comments(is_reported, board_id, create_at, user_id, content) VALUES
                                                                             (false,52,now(),12,'asdasdaasd'),
                                                                             (false,53,now(),12,'asdasdaasd'),
                                                                             (false,54,now(),12,'asdasdaasd');

INSERT INTO comment_reports(comment_id, user_id) VALUES
                                                     (2,12),(2,12),(3,12),
                                                     (2,12),(2,12),(3,12);

INSERT INTO naver_news(published_at, description, link, title) values
                                                                   (NOW(),'adsa0','hpaefsjoako;fshjn','ijkopadsajks'),
                                                                   (NOW(),'adsa0','hpaefsjoako;asd','adaefgrwhrw'),
                                                                   (NOW(),'adsa0','dfgslmo;fshjn','efjoqrpefipj');

INSERT INTO gpt_news(created_at, news_id, summary, title) VALUES
                                                              (NOW(),3,'oaiksdfhjafijosd','bhjkafsljkn'),
                                                              (NOW(),2,'aio;dhs;o','bhjkafsljkn'),
                                                              (NOW(),1,'ksjbkbjkjkbbhjk','fuvygygiygiuhou');

INSERT INTO orders (product_id, product_name, price, quantity, imp_uid, merchant_uid, subscription_period, payment_date, user_id, username)
VALUES (101, '프리미엄 커피 원두', 25000, 2, 'imp_1234567890', 'order_20250424_001', NULL, '2025-04-24 14:32:00', 1, 'user123');

INSERT INTO orders (product_id, product_name, price, quantity, imp_uid, merchant_uid, subscription_period, payment_date, user_id, username)
VALUES (205, '월간 뉴스 구독', 10000, 1, 'imp_0987654321', 'sub_20250424_002', 1, '2025-04-24 14:35:15', 2, 'subscriber');

INSERT INTO payments_init_requests (pg, pay_method, product_id, product_name, price, quantity, subcription_period)
VALUES ('html5_inicis', 'card', 101, '프리미엄 커피 원두', 25000, 2, NULL);

INSERT INTO payments_init_requests (pg, pay_method, product_id, product_name, price, quantity, subcription_period)
VALUES ('nice', 'bank', 310, '기프트 카드 5만원권', 50000, 1, NULL);

INSERT INTO payments_init_requests (pg, pay_method, product_id, product_name, price, quantity, subcription_period)
VALUES ('kakao', 'money', 205, '월간 뉴스 구독', 10000, 1, 1);

-- 환불 요청 데이터 삽입
INSERT INTO refund_requests (id, imp_uid, username, status, request_date)
VALUES (1, 'imp_abcdefg123', 'user123', 'pending', '2025-04-24 14:38:00');

INSERT INTO refund_requests (id, imp_uid, username, status, request_date)
VALUES (2, 'imp_hijklmn456', 'subscriber', 'processing', '2025-04-24 14:40:30');

-- 특정 사용자의 환불 요청 조회
