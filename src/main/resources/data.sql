INSERT INTO usuario (nome_usuario, email, senha, role, ativo)
VALUES 
('Administrador', 'admin@esteroids.com', '123456', 'ADMIN', true)
ON CONFLICT (email) DO NOTHING;

INSERT INTO produto 
(nome, descricao, preco, estoque, imagem_url, categoria, ativo)
VALUES
('The One', 'Figure premium da série de renome As Crônicas da Fatec RL', 666.00, 1, '/uploads/1781052358023_The-One.png', 'SERIES', true),
('Maven Sirius', 'Figure premium da série de jogos The Strange World of Sirius', 474.99, 17, '/uploads/1781110069815_Maven-Sirius.png', 'JOGOS', true),
('The Hacker', 'Figure premium exclusiva da série de renome As Crônicas da Fatec-RL', 14999.99, 0, '/uploads/1781110200029_The-Hacker.png', 'SERIES', true),
('Bunde The Ladiesman + Waifus', 'Figure premium da série de renome As Crônicas da Fatec-RL', 749.99, 5, '/uploads/1781110743906_Bundle-The-Ladiesman-plus-Waifus.png', 'SERIES', true),
('The Incompetent', 'Figure premium da série de renome As Crônicas da Fatec-RL', 399.99, 9, '/uploads/1781110805097_The-Incompetent.png', 'SERIES', true),
('The Fallen Soccer', 'Figure premium da série de animações Super Dez', 549.99, 10, '/uploads/1781111481975_The-Fallen-Soccer.png', 'ANIMES', true),
('The Latecomer', 'Figure premium da série de renome As Crônicas da Fatec-RL', 419.99, 20, '/uploads/1781110318058_The-Latecomer.png', 'SERIES', true),
('The Librarian', 'Figure premium da série de renome As Crônicas da Fatec-RL', 419.99, 12, '/uploads/1781111591140_The-Librarian.png', 'SERIES', true),
('The Ladiesman Big Boss Edition', 'Figure premium exclusiva da série de renome As Crônicas da Fatec-RL', 799.99, 2, '/uploads/1781111816803_The-Ladiesman-Big-Boss-Edition.png', 'SERIES', true),
('Jareth Sirius', 'Figure premium da série de jogos The Strange World of Sirius', 474.99, 13, '/uploads/1781110033639_Jareth-Sirius.png', 'JOGOS', true),
('The Pope', 'Figure premium do filme Right Here Right Now', 666.00, 4, '/uploads/1781110636377_The-Pope.png', 'FILMES', true),
('John Marshall', 'Figure premium da série de livros brasileira O Assassino do Espelho', 674.99, 5, '/uploads/1781111095516_John-Marshall.png', 'LIVROS', true),
('The Defiant', 'Figure premium da série de renome As Crônicas da Fatec RL', 599.90, 10, '/uploads/1781109907355_The-Defiant.png', 'SERIES', true) 
ON CONFLICT (nome) DO NOTHING;