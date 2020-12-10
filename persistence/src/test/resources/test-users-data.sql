INSERT INTO roles(name)
VALUES ('USER');
INSERT INTO roles(name)
VALUES ('ADMIN');


INSERT INTO users(email, password, first_name, last_name, enabled)
VALUES ('jared.mccarthy.admin@mail.com', '123456', 'Jared', 'Mccarthy', true);
INSERT INTO users(email, password, first_name, last_name, enabled)
VALUES ('felix.ryan@mail.com', '123456', 'Felix', 'Ryan', true);
INSERT INTO users(email, password, first_name, last_name, enabled)
VALUES ('rose.richards.admin@mail.com', '123456', 'Rose', 'Richards', true);
INSERT INTO users(email, password, first_name, last_name, enabled)
VALUES ('hanna.robbins@mail.com', '123456', 'Hanna', 'Robbins', true);
INSERT INTO users(email, password, first_name, last_name, enabled)
VALUES ('emilia.malone@mail.com', '123456', 'Emilia', 'Malone', true);


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
