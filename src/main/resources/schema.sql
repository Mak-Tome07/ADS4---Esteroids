CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    stock INTEGER NOT NULL,
    image_url TEXT,
    category VARCHAR(25),
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50),
    email VARCHAR(50),
    password VARCHAR(255),
    role VARCHAR(20) DEFAULT 'CLIENTE',
    active BOOLEAN DEFAULT TRUE
);