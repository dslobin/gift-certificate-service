DROP TABLE IF EXISTS gift_certificate_tag;
DROP TABLE IF EXISTS gift_certificates;
DROP TABLE IF EXISTS tags;

CREATE TABLE tags
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE gift_certificates
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(255)  NOT NULL,
    description      VARCHAR(1024) NOT NULL,
    price            DECIMAL(6, 2) NOT NULL,
    create_date      TIMESTAMP     NOT NULL,
    last_update_date TIMESTAMP,
    duration         LONG          NOT NULL
);

CREATE TABLE gift_certificate_tag
(
    gift_certificate_id BIGINT,
    tag_id              BIGINT,
    CONSTRAINT FK_gift_certificate_tag_tagId FOREIGN KEY (tag_id)
        REFERENCES tags (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_gift_certificate_tag_gift_certificate_id FOREIGN KEY (gift_certificate_id)
        REFERENCES gift_certificates (id) ON DELETE CASCADE ON UPDATE CASCADE
);










