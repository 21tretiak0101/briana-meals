DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users(id, name, email, password, enabled) VALUES
    (1, 'simple_user', 'test@gmail.com', 'password', true),
    (2, 'admin', 'admin@mail.com', 'password88-2', true);

INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 100000),
    (2, 100000),
    (2, 100001);

INSERT INTO meals(id, user_id, calories, date_time, description) VALUES
    (3, 1, 999, '2020-05-13 20:00', 'some meal'),
    (4, 1, 370, '2020-06-01 10:00', 'morning meal'),
    (5, 2, 550, '2020-06-01 13:00', 'first meal'),
    (6, 2, 450, '2020-06-01 14:00', 'evening meal'),
    (7, 2, 680, now(), 'night meal');
