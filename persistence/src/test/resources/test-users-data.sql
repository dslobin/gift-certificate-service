INSERT INTO roles(name)
VALUES ('USER');
INSERT INTO roles(name)
VALUES ('ADMIN');


INSERT INTO users(email, password, first_name, last_name)
VALUES ('jared.mccarthy.admin@mail.com', '123456', 'Jared', 'Mccarthy');
INSERT INTO users(email, password, first_name, last_name)
VALUES ('felix.ryan@mail.com', '123456', 'Felix', 'Ryan');
INSERT INTO users(email, password, first_name, last_name)
VALUES ('rose.richards.admin@mail.com', '123456', 'Rose', 'Richards');
INSERT INTO users(email, password, first_name, last_name)
VALUES ('hanna.robbins@mail.com', '123456', 'Hanna', 'Robbins');
INSERT INTO users(email, password, first_name, last_name)
VALUES ('emilia.malone@mail.com', '123456', 'Emilia', 'Malone');


INSERT INTO user_roles(user_id, role_id)
VALUES (1, 2);
INSERT INTO user_roles(user_id, role_id)
VALUES (2, 1);
INSERT INTO user_roles(user_id, role_id)
VALUES (3, 2);
INSERT INTO user_roles(user_id, role_id)
VALUES (4, 1);
INSERT INTO user_roles(user_id, role_id)
VALUES (5, 1);


INSERT INTO carts(id)
VALUES (1);
INSERT INTO carts(id)
VALUES (2);
INSERT INTO carts(id)
VALUES (3);
INSERT INTO carts(id)
VALUES (4);
INSERT INTO carts(id)
VALUES (5);
