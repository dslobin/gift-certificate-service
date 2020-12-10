INSERT INTO tags(name)
VALUES ('beauty_salon_service');
INSERT INTO tags(name)
VALUES ('active_rest');
INSERT INTO tags(name)
VALUES ('animals');
INSERT INTO tags(name)
VALUES ('travel');
INSERT INTO tags(name)
VALUES ('romantics');
INSERT INTO tags(name)
VALUES ('photo_sessions');
INSERT INTO tags(name)
VALUES ('quests');
INSERT INTO tags(name)
VALUES ('motorists');
INSERT INTO tags(name)
VALUES ('exclusive');
INSERT INTO tags(name)
VALUES ('fitness_and_sports');


INSERT INTO gift_certificates(name, description, price, created_at, days_duration, available)
VALUES ('Полет на авиатренажере Diamond',
        'Вы всю жизнь боялись самолетов и предпочитаете путешествовать поездом? У вас есть по-настоящему уникальная возможность попробовать себя в качестве капитана воздушного судна и убедиться в безопасности современной техники. Все это можно сделать, не отрываясь от земли.',
        55.00,
        CURRENT_TIMESTAMP,
        7,
        true);

INSERT INTO gift_certificates(name, description, price, created_at, days_duration, available)
VALUES ('Картинг по двухэтажной трассе',
        'От создателей фраз «Попробуй догони!» и «Ты будешь нюхать мои выхлопные газы!» уникальная возможность разогнаться до предела. Подарочный сертификат на катание в обновленном клубе Минска «Картленд». Он находится в новом, чистом, сухом помещении. Но даже тут уже прочно обосновался запах жженой резины и бензина. Именно он привлекает многих любителей скорости.',
        16.00,
        CURRENT_TIMESTAMP,
        2,
        true);

INSERT INTO gift_certificates(name, description, price, created_at, days_duration, available)
VALUES ('Гитарные курсы',
        'Если в доме завелся почитатель Queen, Nirvana или Клэптона, этому человеку точно охота научиться играть их песни. Вот тут-то и пора подарить сертификат на курсы игры на акустической, электрогитаре или бас-гитаре. А раз к хорошему стоит приучать с детства, это станет толковым подарком и ребенку. Учиться никогда не рано и не поздно — на курсы ждут с 5 лет.',
        164.00,
        CURRENT_TIMESTAMP,
        14,
        true);

INSERT INTO gift_certificates(name, description, price, created_at, days_duration, available)
VALUES ('Курс «Азы рисования»',
        'Познакомьтесь с миром живописи и научитесь рисовать в разных техниках — карандашом, акварелью и маслом. Курс «Азы рисования» состоит из 10 занятий по 2 часа каждое, а точнее — из 9 и выставки работ всех учеников группы с фуршетом.',
        250.00,
        CURRENT_TIMESTAMP,
        10,
        true);

INSERT INTO gift_certificates(name, description, price, created_at, days_duration, available)
VALUES ('Классический массаж тела',
        'Классический массаж в студии коррекции фигуры «Линия совершенства» — беспроигрышный вариант подарка. Целый час отдыха и массирования тела опытным мастером — что может быть прекраснее после тяжелого рабочего дня? Подарочный сертификат порадует как родителей, так и коллегу либо любимого человека. Дарите полезные для здоровья подарки и наслаждайтесь благодарными отзывами!',
        30.00,
        CURRENT_TIMESTAMP,
        10,
        true);


INSERT INTO certificate_tag(certificate_id, tag_id)
VALUES (1, 2),
       (1, 9);

INSERT INTO certificate_tag(certificate_id, tag_id)
VALUES (2, 2),
       (2, 8);

INSERT INTO certificate_tag(certificate_id, tag_id)
VALUES (3, 5);

INSERT INTO certificate_tag(certificate_id, tag_id)
VALUES (4, 9);

INSERT INTO certificate_tag(certificate_id, tag_id)
VALUES (5, 1),
       (5, 9),
       (5, 10);


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


INSERT INTO orders(created_at, price, user_id)
VALUES (CURRENT_TIMESTAMP, 0, 1);


INSERT INTO order_items(certificate_id, order_id, quantity)
VALUES (1, 1, 1);
INSERT INTO order_items(certificate_id, order_id, quantity)
VALUES (2, 1, 1);
INSERT INTO order_items(certificate_id, order_id, quantity)
VALUES (3, 1, 1);


UPDATE gift_certificates
SET available= false
WHERE id = 1
   OR id = 2
   OR id = 3;
UPDATE orders
SET price = (
    SELECT SUM(gift_certificates.price) AS certificate_price
    FROM gift_certificates
    WHERE gift_certificates.id IN (1, 3)
    )
WHERE id = 1;


INSERT INTO orders(created_at, price, user_id)
VALUES (CURRENT_TIMESTAMP, 0, 1);
INSERT INTO order_items(certificate_id, order_id, quantity)
VALUES (4, 2, 1);


UPDATE gift_certificates
SET available= false
WHERE id = 4;

UPDATE orders
SET price = (
    SELECT SUM(gift_certificates.price) AS certificate_price
    FROM gift_certificates
    WHERE gift_certificates.id = 4
    )
WHERE id = 2;

INSERT INTO public.cart_items(cart_id, certificate_id, quantity)
VALUES (2, 5, 1);
