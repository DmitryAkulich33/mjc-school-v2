USE gift_certificate;

create table tag
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(70) NOT NULL UNIQUE,
    state INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT pk_tag PRIMARY KEY (id)
);

create table certificate
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(300) NOT NULL,
    price DOUBLE NOT NULL,
    creation_date DATETIME NOT NULL,
    update_date DATETIME,
    duration INTEGER NOT NULL,
    state INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT pk_certificate PRIMARY KEY (id)
);

create table tag_certificate
(
    tag_id BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,

    CONSTRAINT pk_tag_certificate PRIMARY KEY (certificate_id, tag_id),
    CONSTRAINT fk_tag_certificate_tag_id FOREIGN KEY (tag_id)
        REFERENCES tag (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_tag_certificate_certificate_id FOREIGN KEY (certificate_id)
        REFERENCES certificate (id) ON DELETE NO ACTION ON UPDATE NO ACTION
)