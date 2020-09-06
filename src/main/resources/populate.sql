DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users(id, name, email, password, enabled) VALUES
    (1, 'simple_user', 'test@gmail.com', '$2y$10$TczuFGRWTfbOCmSYktMqne99oIAYbn.8QZv3Id32/aZdBLdDo7Wfu', true),
    (2, 'admin', 'admin@mail.com', '$2y$12$TCwbCOGqkSOqzDvXjrewYOuR6yeIjPieeBNm4D56IsujhF6gaEIh6', true);

INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 9999),
    (2, 9999),
    (2, 9998);

INSERT INTO meals(id, user_id, calories, date_time, description) VALUES
    (3, 1, 999, '2020-05-13 20:00:00', 'some meal'),
    (4, 1, 370, '2020-06-01 10:00:00', 'morning meal'),
    (5, 2, 550, '2020-06-01 13:00:00', 'first meal'),
    (6, 2, 450, '2020-06-01 14:00:00', 'evening meal'),
    (7, 2, 680, now(), 'night meal');
