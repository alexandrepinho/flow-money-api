INSERT INTO usuario (id, nome, email, senha) values (1, 'Administrador', 'admin@flowmoney.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO usuario (id, nome, email, senha) values (2, 'Alexandre', 'alexandre@flowmoney.com', '$2a$10$VGjYee2NKOn5rhh.gbRT8exzLA2HT50E3Cime8WunMKJ5rBFqTzlu');
INSERT INTO usuario (id, nome, email, senha) values (3, 'Juliana', 'juliana@flowmoney.com', '$2a$10$VGjYee2NKOn5rhh.gbRT8exzLA2HT50E3Cime8WunMKJ5rBFqTzlu');

INSERT INTO permissao (id, descricao) values (1, 'ROLE_CADASTRAR_CATEGORIA');
INSERT INTO permissao (id, descricao) values (2, 'ROLE_ALTERAR_CATEGORIA');
INSERT INTO permissao (id, descricao) values (3, 'ROLE_REMOVER_CATEGORIA');
INSERT INTO permissao (id, descricao) values (4, 'ROLE_PESQUISAR_CATEGORIA');

INSERT INTO permissao (id, descricao) values (5, 'ROLE_CADASTRAR_USUARIO');
INSERT INTO permissao (id, descricao) values (6, 'ROLE_ALTERAR_USUARIO');
INSERT INTO permissao (id, descricao) values (7, 'ROLE_REMOVER_USUARIO');
INSERT INTO permissao (id, descricao) values (8, 'ROLE_PESQUISAR_USUARIO');

INSERT INTO permissao (id, descricao) values (9, 'ROLE_CADASTRAR_TRANSACAO');
INSERT INTO permissao (id, descricao) values (10, 'ROLE_ALTERAR_TRANSACAO');
INSERT INTO permissao (id, descricao) values (11, 'ROLE_REMOVER_TRANSACAO');
INSERT INTO permissao (id, descricao) values (12, 'ROLE_PESQUISAR_TRANSACAO');

-- Admin
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 1);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 2);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 3);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 4);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 5);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 6);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 7);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 8);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 9);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 10);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 11);

-- Alexandre
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 1);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 2);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 3);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 4);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 9);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 10);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 11);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 12);

-- Juliana
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (3, 4);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (3, 12);