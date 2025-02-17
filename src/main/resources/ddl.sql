CREATE TABLE market
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    market_id   BIGINT       NOT NULL,
    FOREIGN KEY (market_id) REFERENCES market (id) ON DELETE CASCADE
);

CREATE TABLE order_table
(
    id         SERIAL PRIMARY KEY,
    order_date TIMESTAMP NOT NULL
);

CREATE TABLE orders_products
(
    order_id   BIGINT,
    product_id BIGINT,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES order_table (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);