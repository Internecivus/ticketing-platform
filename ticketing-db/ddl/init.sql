DROP TABLE IF EXISTS remember_me_token;
DROP TABLE IF EXISTS password_reset_token;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS ticket_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS credentials;
DROP TABLE IF EXISTS user_group_role;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user_user_group;
DROP TABLE IF EXISTS user_group;
DROP TABLE IF EXISTS category_assigned_user;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(120) UNIQUE NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted TIMESTAMP,
    last_updated TIMESTAMP,
    created_user_id INTEGER REFERENCES app_user(id) NOT NULL,
    last_update_user_id INTEGER REFERENCES app_user(id)
);

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) UNIQUE NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP,
    deleted TIMESTAMP,
    created_user_id INTEGER REFERENCES app_user(id) NOT NULL,
    last_update_user_id INTEGER REFERENCES app_user(id)
);

CREATE TABLE user_group (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE user_user_group (
    user_id INTEGER REFERENCES app_user(id) NOT NULL,
    user_group_id INTEGER REFERENCES user_group(id) NOT NULL,
    PRIMARY KEY(user_id, user_group_id)
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) UNIQUE NOT NULL
);

CREATE TABLE user_group_role (
    user_group_id INTEGER REFERENCES user_group(id) NOT NULL,
    role_id INTEGER REFERENCES role(id) NOT NULL,
    PRIMARY KEY(user_group_id, role_id)
);

CREATE TABLE category_assigned_user (
    category_id INTEGER REFERENCES category(id),
    user_id INTEGER REFERENCES app_user(id),
    PRIMARY KEY(user_id, category_id)
);

CREATE TABLE credentials (
    id SERIAL PRIMARY KEY,
    password CHAR(186) NOT NULL,
    user_id INTEGER REFERENCES app_user(id),
    pepper_alias VARCHAR(100) NOT NULL
);

CREATE TABLE ticket (
    id SERIAL PRIMARY KEY,
    title VARCHAR(240) NOT NULL UNIQUE,
    content VARCHAR(32000) NOT NULL,
    priority SMALLINT NOT NULL DEFAULT 0,
    assigned_user_id INTEGER REFERENCES app_user(id),
    denied_user_id INTEGER REFERENCES app_user(id),
    category_id INTEGER REFERENCES category(id) NOT NULL,
    denied TIMESTAMP,
    resolved TIMESTAMP,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP,
    deleted TIMESTAMP,
    created_user_id INTEGER REFERENCES app_user(id) NOT NULL,
    last_update_user_id INTEGER REFERENCES app_user(id)
);

CREATE TABLE tag (
    id SERIAL PRIMARY KEY,
    name VARCHAR(120) UNIQUE NOT NULL,

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP,
    deleted TIMESTAMP,
    created_user_id INTEGER REFERENCES app_user(id) NOT NULL,
    last_update_user_id INTEGER REFERENCES app_user(id)
);

CREATE TABLE ticket_tag (
    ticket_id INTEGER REFERENCES ticket(id),
    tag_id INTEGER REFERENCES tag(id),
    PRIMARY KEY(ticket_id, tag_id)
);

CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    content VARCHAR(4096) NOT NULL,
    parent_comment_id INTEGER REFERENCES comment(id),

    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP,
    deleted TIMESTAMP,
    created_user_id INTEGER REFERENCES app_user(id) NOT NULL,
    last_update_user_id INTEGER REFERENCES app_user(id)
);

CREATE TABLE password_reset_token (
    id SERIAL PRIMARY KEY,
    hash CHAR(186) NOT NULL,
    generated TIMESTAMP NOT NULL,
    expiration TIMESTAMP NOT NULL,
    used TIMESTAMP,
    removed TIMESTAMP,
    user_id INTEGER REFERENCES app_user(id) NOT NULL,
    pepper_alias VARCHAR(100) NOT NULL
);

CREATE TABLE remember_me_token (
    id SERIAL PRIMARY KEY,
    hash CHAR(186) NOT NULL,
    generated TIMESTAMP NOT NULL,
    used TIMESTAMP,
    removed TIMESTAMP,
    user_id INTEGER REFERENCES app_user(id) NOT NULL,
    pepper_alias VARCHAR(100) NOT NULL
);



