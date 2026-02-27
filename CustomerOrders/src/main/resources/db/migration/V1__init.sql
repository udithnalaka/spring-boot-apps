CREATE TABLE customers (
                           id BIGSERIAL PRIMARY KEY,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           name VARCHAR(255) NOT NULL
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        customer_id BIGINT NOT NULL,
                        order_date TIMESTAMP NOT NULL,
                        amount NUMERIC(10,2) NOT NULL,
                        status VARCHAR(50) NOT NULL,

                        CONSTRAINT fk_customer
                            FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Indexes
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_customer_date ON orders(customer_id, order_date);
CREATE INDEX idx_orders_status ON orders(status);