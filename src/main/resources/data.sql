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

