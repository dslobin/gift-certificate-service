INSERT INTO roles(name)
VALUES ('USER');

INSERT INTO users(email, password, first_name, last_name)
VALUES ('jared.mccarthy@mail.com', '123456', 'Jared', 'Mccarthy');

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 1);
