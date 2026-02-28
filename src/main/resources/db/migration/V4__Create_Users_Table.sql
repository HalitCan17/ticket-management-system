CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       role VARCHAR(50) NOT NULL,
                       full_name VARCHAR(150)
);