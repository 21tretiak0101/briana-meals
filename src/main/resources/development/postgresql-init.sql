DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 10000;

CREATE TABLE users (
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                                NOT NULL,
    email            VARCHAR                                NOT NULL,
    password         VARCHAR                                NOT NULL,
    registered       TIMESTAMP DEFAULT now()::timestamp(0)  NOT NULL,
    enabled          BOOL DEFAULT TRUE                      NOT NULL,
    calories_per_day INTEGER DEFAULT 2000                   NOT NULL
);

CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles (
    user_id INTEGER NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE roles (
    id   INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('global_seq'),
    name VARCHAR(100) NOT NULL
);

CREATE TABLE user_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role_id)
);

CREATE TABLE meals (
    id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    user_id     INTEGER     NOT NULL,
    calories    INTEGER     NOT NULL,
    date_time   TIMESTAMP   NOT NULL,
    description TEXT        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX meals_unique_user_datetime_idx ON meals (user_id, date_time);
INSERT INTO roles(id, name) VALUES (9998, 'ADMIN'), (9999, 'USER') ;
