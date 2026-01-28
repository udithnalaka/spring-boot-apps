CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(50)
);


INSERT INTO product (name, description, status)VALUES ('Cricket bat', 'Cricket bat', 'available'),       ('Tennis racket', 'Tennis racket', 'not available');