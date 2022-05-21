INSERT INTO tag
    (name)
VALUES ('food'),
       ('delivery');

INSERT INTO certificate
    (name, description, price, creation_date, update_date, duration)
VALUES ('Certificate for one purchase', 'Certificate for one going to the shop', 50.0, '2020-10-22T11:45:11',
        '2020-10-22T11:50:50', 360),
       ('Certificate for dinner in a restaurant', 'Food and drink without check limit at Viet Express', 100.0,
        '2020-11-22T12:45:11', '2020-11-22T12:55:55', 100);
INSERT INTO tag_certificate
VALUES (1, 1),
       (1, 2),
       (2, 1);