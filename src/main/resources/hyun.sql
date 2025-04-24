INSERT INTO users (username, password, nickname, role, badge_id) VALUES
('admin', 'admin', '뉴션', 'ROLE_ADMIN', 1),
('user1', 'pw1', '유저1', 'ROLE_USER', null),
('user2', 'pw2', '유저2', 'ROLE_USER', null),
('user3', 'pw3', '유저3', 'ROLE_USER', null),
('user4', 'pw4', '유저4', 'ROLE_USER', null);

INSERT INTO badges (icon, title, role) VALUES
('admin_badge', '뉴션', 'ROLE_ADMIN'),
('creator_badge', '뉴션 공식 크리에이터', 'ROLE_CREATOR');

INSERT INTO follows (follower_id, following_id, created_at) VALUES
(8, 7, NOW()),
(10, 7, NOW());

INSERT INTO follows (follower_id, following_id, created_at) VALUES
(7, 9, NOW());

INSERT INTO follows (follower_id, following_id, created_at) VALUES
(7, 12, NOW()),
(8, 12, NOW());

INSERT INTO follows (follower_id, following_id, created_at) VALUES
(12, 9, NOW()),
(12, 10, NOW());

INSERT INTO categories (name) VALUES
('카테1'),
('카테2'),
('카테3');


INSERT INTO news (title, content, category_id, created_at, view, user_id)
VALUES
    ('AI 뉴스 대폭발', 'AI 관련 뉴스 본문입니다.', 1, NOW(), 15,  2),  -- admin
    ('테슬라 신모델 발표', '테슬라 뉴스 내용입니다.', 1, NOW(), 22,  2), -- admin
    ('경제전망 하락세', '경제 뉴스입니다.', 2, NOW(), 8,  2),        -- creator
    ('우주 산업의 미래', '우주 뉴스입니다.', 1, NOW(), 10,  2),         -- creator
    ('새로운 인터넷 밈 등장', '밈 관련 소식입니다.', 3, NOW(), 5, 2),
    ('요약 테스트 타이틀', '한국 경제가 지난해 2분기부터 올해 1분기까지 사실상 ‘제로 성장’ 상태에 빠졌다. 이런 추세는 내수 침체에다 관세 전쟁에 따른 수출 위축 여파로 올해 2분기에도 계속될 가능성이 매우 높다. 사실상 경기침체 상황이 1년 넘게 이어지는 셈이다. 1997년 외환위기와 2008년 글로벌 금융위기 때도 없었던 일이다. 그만큼 지금의 경제 상황이 심각하다는 방증이다.

우리 경제는 지난해 2분기에 역성장(-0.2%)을 한 이후로 3분기 0.1%, 4분기 0.07%의 성장률을 기록했다. 올해 1분기에도 역성장을 하거나 플러스(+) 성장을 한다고 해도 0.1% 이하일 가능성이 크다. 1년째 역성장 또는 초저성장을 이어가는 셈이다. 한국 현대사에서 최악의 경제위기였던 외환위기 때도 3분기 연속 역성장 이후 플러스 성장으로 돌아선 바 있다. 최근 경제 상황은 윤석열 정권의 경제 운용이 파탄 났음을 보여준다. 건전재정과 대규모 감세를 축으로 한 재정정책으로 경제 활력을 높이겠다고 했으나, 경기침체를 수수방관하고 세수 기반만 훼손한 꼴이 됐다.

정부는 마지못해 3년 만에 12조2천억원 규모의 추경안을 내놨으나 경기 대응에는 턱없이 부족한 수준이다. 추경안의 성장률 제고 효과는 0.1%포인트뿐이다. 정부 스스로도 ‘경기 대응용’이 아니라고 인정했다. 실제로, 통상 대응과 인공지능 경쟁력 제고에 4조4천억원, 재해·재난 대응에 3조2천억원이 배정됐고, 민생 지원은 4조3천억원에 불과하다. 그나마도 민생 지원 중에 영세 소상공인(311만명)에게 직접 지원하는 금액은 최대 50만원뿐이다. 연 매출 30억원 이하 사업자에게 카드 소비 증가액의 20%를 최대 30만원어치 온누리상품권으로 지급하는 ‘상생 페이백’ 제도는 1조4천억원이 배정됐는데, 과연 누가 쓸 수 있을지 실효성마저 의문시된다. 경제는 경기 대응과 민생 지원 용도의 추경을 절실히 필요로 하는 상황인데 애써 여기에는 눈을 감고 있다는 인상마저 지울 수 없다.

정부가 오는 22일 국회에 추경안을 제출할 예정인데, 국회가 경제 상황을 고려해 대폭 증액을 추진하기 바란다. 대선 국면으로 접어들었지만 경제를 살리고 민생을 보듬는 데 여야가 따로 있을 수 없다. 재정당국인 기획재정부도 추경 규모와 성격에 대해 “아주 유연하고 탄력적으로 대응하겠다”며 국회가 요구해 오면 수용할 뜻을 밝힌 바 있다. 정당들이 실질적인 경기 활성화와 민생 지원 방안을 놓고 정책 경쟁을 벌이는 모습을 보고 싶다.', 3, NOW(), 5, 2);     -- creator


-- 1~10번: ADMIN 작성자
INSERT INTO news (news_id, title, content, category_id, created_at, view, image, user_id) VALUES
                                                                                              (10, '관리자 뉴스 1일 전', '본문입니다.', 1, NOW() - INTERVAL 1 DAY, 23, '/images/news1.jpg', 1),
                                                                                              (11, '관리자 뉴스 2일 전', '본문입니다.', 2, NOW() - INTERVAL 2 DAY, 18, '/images/news2.jpg', 1),
                                                                                              (12, '관리자 뉴스 3일 전', '본문입니다.', 3, NOW() - INTERVAL 3 DAY, 12, '/images/news3.jpg', 1),
                                                                                              (13, '관리자 뉴스 4일 전', '본문입니다.', 1, NOW() - INTERVAL 4 DAY, 7, '/images/news4.jpg', 1),
                                                                                              (14, '관리자 뉴스 5일 전', '본문입니다.', 2, NOW() - INTERVAL 5 DAY, 14, '/images/news5.jpg', 1),
                                                                                              (15, '관리자 뉴스 6일 전', '본문입니다.', 3, NOW() - INTERVAL 6 DAY, 6, '/images/news6.jpg', 1),
                                                                                              (16, '관리자 뉴스 7일 전', '본문입니다.', 1, NOW() - INTERVAL 7 DAY, 30, '/images/news7.jpg', 1),
                                                                                              (17, '관리자 뉴스 10일 전', '본문입니다.', 2, NOW() - INTERVAL 10 DAY, 20, '/images/news8.jpg', 1),
                                                                                              (18, '관리자 뉴스 12일 전', '본문입니다.', 3, NOW() - INTERVAL 12 DAY, 15, '/images/news9.jpg', 1),
                                                                                              (19, '관리자 뉴스 14일 전', '본문입니다.', 1, NOW() - INTERVAL 14 DAY, 10, '/images/news10.jpg', 1);
-- 20~29번: CREATOR 2번 작성자
INSERT INTO news (news_id, title, content, category_id, created_at, view, image, user_id) VALUES
                                                                                              (20, '크리에이터1 뉴스 1일 전', '본문입니다.', 1, NOW() - INTERVAL 1 DAY, 8, '/images/news11.jpg', 2),
                                                                                              (21, '크리에이터1 뉴스 2일 전', '본문입니다.', 2, NOW() - INTERVAL 2 DAY, 3, '/images/news12.jpg', 2),
                                                                                              (22, '크리에이터1 뉴스 3일 전', '본문입니다.', 3, NOW() - INTERVAL 3 DAY, 14, '/images/news13.jpg', 2),
                                                                                              (23, '크리에이터1 뉴스 5일 전', '본문입니다.', 1, NOW() - INTERVAL 5 DAY, 5, '/images/news14.jpg', 2),
                                                                                              (24, '크리에이터1 뉴스 7일 전', '본문입니다.', 2, NOW() - INTERVAL 7 DAY, 9, '/images/news15.jpg', 2);
-- 30~34번: CREATOR 3번 작성자
INSERT INTO news (news_id, title, content, category_id, created_at, view, image, user_id) VALUES
                                                                                              (25, '크리에이터2 뉴스 2일 전', '본문입니다.', 1, NOW() - INTERVAL 2 DAY, 12, '/images/news16.jpg', 3),
                                                                                              (26, '크리에이터2 뉴스 4일 전', '본문입니다.', 2, NOW() - INTERVAL 4 DAY, 16, '/images/news17.jpg', 3);