insert into role (id, name) values
    (101, 'ROLE_USER');



insert into user_entity (id, about, city, email, full_name, image_link, is_deleted, is_enabled, last_redaction_date,
                         link_github, link_site, link_vk, nickname, password, persist_date, role_id) values
    (101, 'I am test user', 'Moskow', 'user101@mail.ru', 'User 101', '/images/noUserAvatar.png', false, true, date(NOW()),
    null, null, null, 'user_101', '$2a$10$hl61sc4Ic/znptF.bwbkv.sNvGX9rr7J32DYbAUkcvn.qDOaUE1Aa', DATE(NOW()), 101),

    (102, 'I am test user', 'Moskow', 'user102@mail.ru', 'User 102', '/images/noUserAvatar.png', false, true, DATE(NOW()),
    null, null, null, 'user_102', '$2a$10$LGed.2Ev4jPoM2pL0qmyI.nE8F1d2.pWSKkjChKjjLcso4jsTbc.q', DATE(NOW()), 101);


insert into chat (id, chat_type, persist_date, is_global) values
    (101, 1, date(NOW()), false);


insert into group_chat (chat_id, title, author_id, image) values
    (101, 'GroupChat 101', 101, 'image 101');


insert into groupchat_has_users (chat_id, user_id) values
    (101, 102);