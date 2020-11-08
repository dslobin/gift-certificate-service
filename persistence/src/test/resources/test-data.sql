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

INSERT INTO gift_certificates(name, description, price, create_date, duration)
VALUES ('Полет на авиатренажере Diamond',
        'Вы всю жизнь боялись самолетов и предпочитаете путешествовать поездом? У вас есть по-настоящему уникальная возможность попробовать себя в качестве капитана воздушного судна и убедиться в безопасности современной техники. Все это можно сделать, не отрываясь от земли.',
        55.00,
        CURRENT_TIMESTAMP,
        7);

INSERT INTO gift_certificates(name, description, price, create_date, duration)
VALUES ('Картинг по двухэтажной трассе',
        'От создателей фраз «Попробуй догони!» и «Ты будешь нюхать мои выхлопные газы!» уникальная возможность разогнаться до предела. Подарочный сертификат на катание в обновленном клубе Минска «Картленд». Он находится в новом, чистом, сухом помещении. Но даже тут уже прочно обосновался запах жженой резины и бензина. Именно он привлекает многих любителей скорости.',
        16.00,
        CURRENT_TIMESTAMP,
        2);

INSERT INTO gift_certificates(name, description, price, create_date, duration)
VALUES ('Гитарные курсы',
        'Если в доме завелся почитатель Queen, Nirvana или Клэптона, этому человеку точно охота научиться играть их песни. Вот тут-то и пора подарить сертификат на курсы игры на акустической, электрогитаре или бас-гитаре. А раз к хорошему стоит приучать с детства, это станет толковым подарком и ребенку. Учиться никогда не рано и не поздно — на курсы ждут с 5 лет.',
        164.00,
        CURRENT_TIMESTAMP,
        14);

INSERT INTO gift_certificates(name, description, price, create_date, duration)
VALUES ('Курс «Азы рисования»',
        'Познакомьтесь с миром живописи и научитесь рисовать в разных техниках — карандашом, акварелью и маслом. Курс «Азы рисования» состоит из 10 занятий по 2 часа каждое, а точнее — из 9 и выставки работ всех учеников группы с фуршетом.',
        250.00,
        CURRENT_TIMESTAMP,
        10);

INSERT INTO gift_certificates(name, description, price, create_date, duration)
VALUES ('Классический массаж тела',
        'Классический массаж в студии коррекции фигуры «Линия совершенства» — беспроигрышный вариант подарка. Целый час отдыха и массирования тела опытным мастером — что может быть прекраснее после тяжелого рабочего дня? Подарочный сертификат порадует как родителей, так и коллегу либо любимого человека. Дарите полезные для здоровья подарки и наслаждайтесь благодарными отзывами!',
        30.00,
        CURRENT_TIMESTAMP,
        10);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 2),
       (1, 9);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (2, 2),
       (2, 8);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (3, 5);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (4, 9);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (5, 1),
       (5, 9),
       (5, 10);


