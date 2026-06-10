CREATE TABLE IF NOT EXISTS produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao TEXT NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    estoque INTEGER NOT NULL,
    imagem_url TEXT NOT NULL,
    categoria VARCHAR(25) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome_usuario VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'CLIENTE',
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS pedido(
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL,
    data_pedido TIMESTAMP NOT NULL,
    valor_total NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    FOREIGN KEY(usuario_id)
    REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS pedido_item(
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER NOT NULL,
    produto_id INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    FOREIGN KEY(pedido_id)
    REFERENCES pedido(id),
    FOREIGN KEY(produto_id)
    REFERENCES produto(id)
);

ALTER TABLE produto OWNER TO esteroids_user;
ALTER TABLE pedido OWNER TO esteroids_user;
ALTER TABLE pedido_item OWNER TO esteroids_user;