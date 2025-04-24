
INSERT INTO users(is_paid, badge_id, create_at, email, image, introduce, nickname, password, provider_id, username, provider) VALUES
(false,2,NOW(),'expmasdasda@asdad3.com','dfgag.jpg','asdad','sga','asdasdasda',null,'ㅁㄴㅇㅁㄴㅇㅁㄴㅇ,luyf','KAKAO'),
(false,2,NOW(),'expmasdasda@asdad4.com','agrew.jpg','asdad','adsasd','asdf',null,'ㅏ숑악쇼아쇼ㅏㅅ요,luyf','KAKAO');

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