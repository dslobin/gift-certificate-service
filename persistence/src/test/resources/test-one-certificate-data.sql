INSERT INTO tags(name)
VALUES ('active_rest');
INSERT INTO tags(name)
VALUES ('sport');

INSERT INTO gift_certificates(name, description, price, create_date, duration, available)
VALUES ('Diving lessons',
        'Gift certificate entitles you to attend 3 diving lessons from a professional instructor',
        130.00,
        CURRENT_TIMESTAMP,
        31,
        true);

INSERT INTO certificate_tag(certificate_id, tag_id)
VALUES (1, 1),
       (1, 2);
