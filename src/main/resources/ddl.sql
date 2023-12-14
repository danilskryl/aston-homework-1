DROP SCHEMA IF EXISTS aston;

CREATE SCHEMA aston;

CREATE TABLE aston.market
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE aston.product
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    market_id   BIGINT       NOT NULL,
    FOREIGN KEY (market_id) REFERENCES aston.market (id) ON DELETE CASCADE
);

CREATE TABLE aston.order_table
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_date TIMESTAMP NOT NULL
);

CREATE TABLE aston.orders_products
(
    order_id   BIGINT,
    product_id BIGINT,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES aston.order_table (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES aston.product (id) ON DELETE CASCADE
);