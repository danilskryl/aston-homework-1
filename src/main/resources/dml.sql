INSERT INTO market (name)
VALUES ('Meta'),
       ('Yandex'),
       ('Google');


INSERT INTO product (name, description, market_id)
VALUES ('Browser', 'Google browser', 3),
       ('YouTube', 'Video hosting', 3),
       ('Gmail', 'Google mail', 3),
       ('Browser', 'Yandex browser', 2),
       ('Food', 'Yandex food', 2),
       ('Maps', 'Yandex maps', 2),
       ('Taxi', 'Yandex taxi', 2),
       ('Facebook', 'Social network', 1),
       ('Instagram', 'Social network', 1),
       ('Threads', 'Social network', 1);


INSERT INTO order_table (order_date)
VALUES ('2023-12-01 15:30:00'),
       ('2023-12-15 16:45:00'),
       ('2024-01-05 12:00:00'),
       ('2024-02-10 14:20:00'),
       ('2024-03-20 18:30:00'),
       ('2024-04-02 10:45:00'),
       ('2024-05-12 11:15:00'),
       ('2024-06-25 17:00:00'),
       ('2024-07-08 09:30:00'),
       ('2024-08-19 13:55:00');


INSERT INTO orders_products (order_id, product_id)
VALUES (1, 1),
       (1, 5),
       (1, 9),
       (2, 2),
       (2, 6),
       (2, 10),
       (3, 3),
       (3, 7),
       (4, 4),
       (4, 8),
       (5, 1),
       (5, 5),
       (5, 9),
       (6, 2),
       (6, 6),
       (6, 10),
       (7, 3),
       (7, 7),
       (8, 4),
       (8, 8),
       (9, 1),
       (9, 5),
       (9, 9),
       (10, 2),
       (10, 6),
       (10, 10);