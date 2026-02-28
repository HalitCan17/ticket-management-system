CREATE TABLE products
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    description TEXT,
    is_active   BOOLEAN                  DEFAULT TRUE,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE tickets
    ADD COLUMN product_id UUID;
ALTER TABLE tickets
    ADD CONSTRAINT fk_tickets_product FOREIGN KEY (product_id) REFERENCES products (id);