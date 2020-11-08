INSERT INTO tags(name)
VALUES ('active_rest');
INSERT INTO tags(name)
VALUES ('sport');

INSERT INTO gift_certificates(name, description, price, create_date, duration)
VALUES ('Diving lessons',
        'Gift certificate entitles you to attend 3 diving lessons from a professional instructor',
        130.00,
        CURRENT_TIMESTAMP,
        31);

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2);
