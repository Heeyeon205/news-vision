
-- [1] --
INSERT INTO users(is_paid, badge_id, create_at, email, image, introduce, nickname, password, provider_id, username, provider. role) VALUES
    (false,1,NOW(),'admin@example.com','admin.jpg','관리자입니다.','admin','admin',null,'관리자','KAKAO', ROLE_ADMIN),
    (false,2,NOW(),'creator@example.com','creator.jpg','크리에이터입니다.','creator','creator',null,'크리에이터','KAKAO',ROLE_CREATOR),
    (true,3,NOW(),'user1@example.com','example1.jpg','관리자입니다.','user1','user1',null,'유저1','KAKAO',ROLE_USER),
    (true,4,NOW(),'user2@example.com','example2.jpg','관리자입니다.','user2','user2',null,'유저2','KAKAO',ROLE_USER),
    (true,5,NOW(),'user3@example.com','example3.jpg','관리자입니다.','user3','user3',null,'유저3','KAKAO',ROLE_USER);


-- [2] --
INSERT INTO news(title, content, category_id, created_at, view, user_id)VALUES
                ('김정은 사망', '김정은 사망 관련 뉴스 본문입니다.', 1, NOW(), 1500,  1),
                 ('윤성렬 사망', '윤성렬 사망 뉴스 본문입니다.', 1, NOW(), 2015,  1),
                 ('일본 침몰', '일본 침몰 관련 뉴스 본문입니다.', 1, NOW(), 2525,  1),

                  ('QWER', 'QWER 행사 관련 뉴스 본문입니다.', 2, NOW(), 15000000,  2),
                  ('LESERAPHIM', 'LESERAPHIM 행사 뉴스 본문입니다.', 2, NOW(), 20150000,  2),
                 ('ASPA', 'ASPA 행사 관련 뉴스 본문입니다.', 2, NOW(), 25250000,  2),

                ('얼룩말', '길가다 얼룩말 뉴스 본문입니다.', 3, NOW(), 15000000,  3),
                ('곤지암', '곤지암 실종 사건뉴스 본문입니다.', 3, NOW(), 20150000,  3),
                ('평생솔로', '나는솔로 평생솔로', 3, NOW(), 25250000,  3),

                ('삼성 ', '삼성 부도 뉴스 본문입니다.', 3, NOW(), 15000000,  4),
                ('네이버  ', '네이버 부도 뉴스 본문입니다.', 3, NOW(), 20150000,  4),
                ('카카오', '카카오 부도 뉴스 본문입니다.', 3, NOW(), 25250000,  4),

                 ('삼성', '삼성 부도 뉴스 본문입니다.', 2, NOW(), 15000000,  5),
                ('네이버  ', '네이버 부도 뉴스 본문입니다.', 2, NOW(), 20150000,  5),
                 ('카카오', '카카오 부도 뉴스 본문입니다.', 2, NOW(), 25250000,  5);


-- [3] --
INSERT INTO categories(name)VALUES ('경제'),('정치,사회'),('문화'),('글로벌'),('예술'),('과학 기술'),('역사'),('도서,문화');



-- [4] --
INSERT INTO news_likes (news_id,user_id)VALUES
                  (7,3),
                  (8,4),
                  (9,5),
                  (10,3),
                  (11,4),
                  (12,5);



-- [5] --
INSERT INTO boards (is_reported, category_id, create_at, news_id, user_id, view, content, image) VALUES
                    (false,1,NOW(),1,1,456,'관리자다','admin.jpg'),
                    (false,1,NOW(),1,1,520,'관리자인데 어쩔?','admin.jpg'),
                    (false,1,NOW(),1,1,520,'관리자인데 어쩔?킹받쥬?','admin.jpg'),

                    (false,2,NOW(),2,2,456,'약한영웅2개봉 언제?','admin.jpg'),
                    (false,2,NOW(),2,2,520,'분노의질주 개봉언제?','admin.jpg'),
                    (false,2,NOW(),2,2,520,'여기 맛집','admin.jpg'),


                    (false,3,NOW(),3,3,456,'강남에서 한잔 할사람?','example1.jpg'),
                    (false,3,NOW(),3,3,520,'스파링 할사람?','example1.jpg'),
                    (false,3,NOW(),3,3,520,'스터디 할사람?','example1.jpg'),

                    (false,3,NOW(),3,4,456,'운동 할사람?','example2.jpg'),
                    (false,3,NOW(),3,4,520,'고기 먹을 사람?','example2.jpg'),
                    (false,3,NOW(),3,4,520,'노래방 갈사람?','example2.jpg'),

                    (false,2,NOW(),2,5,456,'운동 할사람?','example3.jpg'),
                    (false,2,NOW(),2,5,520,'고기 먹을 사람?','example3.jpg'),
                    (false,2,NOW(),2,5,520,'노래방 갈사람?','example3.jpg');



-- [6] --
INSERT INTO board_likes (user_id,board_id) VALUES
                        (3,1),
                        (3,2),
                        (3,3),
                        (4,4),
                        (4,5),
                        (4,6),
                        (5,7),
                        (5,8),
                        (5,9);



-- [7] --
INSERT INTO comments(is_reported, board_id, create_at, user_id, content) VALUES
        (false,1,now(),1,'계정 삭제해줘?'),
        (false,1,now(),1,'나 관리자야'),
        (false,1,now(),1,'안녕?'),

        (false,2,now(),2,'하이!하이!'),
        (false,2,now(),2,'여기 어디에요?'),
        (false,2,now(),2,'너 먼데? X발X끼야 ~'),

        (false,3,now(),3,'여기 삼겹살 1인분에 얼마에요?'),
        (false,3,now(),3,'여기 다운펌 얼마에요?'),
        (false,3,now(),3,'얼마냐고 X끼야? ㄷㅈ래?'),

        (false,4,now(),4,'하이!하이!'),
        (false,4,now(),4,'ㅇㅋㅇㅋ'),
        (false,4,now(),4,'ㄷㅈ래?'),

        (false,5,now(),5,'너무 좋아요!!'),
        (false,5,now(),5,'귀여워ㅜㅜ'),
        (false,5,now(),5,'어쩌라고 ㅅㅂ');




-- [8] --
INSERT INTO notice(send_id,receive_id,typ,url,title,is_read,created_at) VALUES
(3,5,NEWS_LIKE,'#','좋아요',false,now()),
(3,5,NEWS_LIKE,'#','좋아요',false,now()),
(3,5,NEWS_LIKE,'#','좋아요',false,now()),
(4,5,NEWS_LIKE,'#','좋아요',false,now()),
(4,5,NEWS_LIKE,'#','좋아요',false,now()),
(4,5,NEWS_LIKE,'#','좋아요',false,now()),
(5,3,NEWS_LIKE,'#','좋아요',false,now()),
(5,3,NEWS_LIKE,'#','좋아요',false,now()),
(5,3,NEWS_LIKE,'#','좋아요',false,now());



-- [9] --
INSERT INTO follow(follower_id,following_id) VALUES
                (1,2),
                (1,3),
                (1,4),
                (1,5),
                (1,6),
                (1,7),
                (1,8);



-- [10] --
INSERT INTO scraps(user_id, news_id) VALUES
                (3,4),
                (3,5),
                (3,6),
                (3,7),
                (3,8),
                (3,9);


-- [11] --
INSERT INTO badges(icon, title,role) VALUES
                ('icon','title',ROLE_ADMIN),
                ('icon','title',ROLE_CREATOR),
                ('icon','title',ROLE_USER),
                ('icon','title',ROLE_USER),
                ('icon','title',ROLE_USER);




-- [12] --
INSERT INTO board_reports(board_id, user_id) VALUES
                (1,1),
                (2,2),
                (3,3),
                (4,4),
                (5,5),
                (6,6),
                (7,7),
                (8,8),
                (9,9);


-- [13] --
INSERT INTO comment_reports(comment_id, user_id) VALUES
                (6,2),
                (9,3),
                (12,4),
                (15,5);


-- [16] --
INSERT INTO polls (title, content, expired_at, created_at , user_id)VALUES
    ('남북통일','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),1),
    ('윤석열 사형','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),1),
    ('담배 4500 -> 9000','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),1),

    ('QWER 공연','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),2),
    ('LESERAPHIM 공연','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),2),
    ('ASPA 공연','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),2),

    ('결혼','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),3),
    ('운동','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),3),
    ('주식','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),3),

    ('결혼','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),4),
    ('운동','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),4),
    ('주식','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),4),

    ('사업','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),5),
    ('헬스','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),5),
    ('공부','해야된다. vs 하지 말아야 된다.',NOW(),NOW(),5);



-- [17] --
INSERT INTO polls_option(content,count,poll_id)VALUES
                            ('해야된다. vs 하지 말아야 된다.',510,1),
                            ('해야된다. vs 하지 말아야 된다.',510,1),
                            ('해야된다. vs 하지 말아야 된다.',510,1),
                            ('해야된다. vs 하지 말아야 된다.',510,2),
                            ('해야된다. vs 하지 말아야 된다.',510,2),
                            ('해야된다. vs 하지 말아야 된다.',510,2);


-- [18] --
INSERT INTO polls_votes(vored_at,option_id,user_id)VALUES
                                                 (now(),1,3),
                                                 (now(),2,4),
                                                 (now(),2,5);


-- [19] --
INSERT INTO orders (product_id, product_name, price, quantity, imp_uid, merchant_uid, subscription_period, payment_date, user_id, username)VALUES
(1, '월간 뉴스 구독', 100, 2, 'imp_1234567890', 'order_20250424_001', 1, '2025-04-24 14:32:00', 3, '유저1'),
(2, '월간 뉴스 구독', 101, 1, 'imp_0987654321', 'sub_20250424_002', 3, '2025-04-24 14:35:15', 4, '유저2'),
(3, '월간 뉴스 구독', 102, 2, 'imp_1234567890', 'order_20250424_001', 6, '2025-04-24 14:32:00', 5, '유저3');




-- [20] --
INSERT INTO refund_requests ( imp_uid, username, status, request_date)VALUES
( 'imp_1234567890', '유저1', 'pending', '2025-04-24 14:32:00'),
( 'imp_1234567890', '유저2', 'pending', '2025-04-24 14:32:00'),
( 'imp_1234567890', '유저3', 'pending', '2025-04-24 14:32:00');




-- [21] --
INSERT INTO email_verification (email,code,expredAt) VALUES
      ('user1@example.com','code',now()),
      ('user2@example.com','code',now()),
      ('user3@example.com','code',now());