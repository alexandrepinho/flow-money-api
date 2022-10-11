INSERT INTO usuario (id, nome, email, senha) values (1, 'Administrador', 'admin@flowmoney.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO usuario (id, nome, email, senha) values (2, 'João', 'joao@flowmoney.com', '$2a$10$VGjYee2NKOn5rhh.gbRT8exzLA2HT50E3Cime8WunMKJ5rBFqTzlu');

INSERT INTO permissao (id, descricao) values (1, 'CRUD_USUARIOS');
INSERT INTO permissao (id, descricao) values (2, 'CRUD_TRANSACOES'); 
-- VERIFICAR NECESSIADE DESTA SEGUNDA PERMISSÃO, TENDO EM VISTA QUE TODOS OS USUÁRIOS CADASTRADOS PODERÃO TER ACESSO AO CRUD DE TRANSAÇÕES

-- 2 perfis -  administrador com acesso para cadastrar novos usuários e usuário comum sem gerencia de outros usuários mas com gerencia das suas transações

-- Admin
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 1);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 2);

-- Alexandre
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (2, 2);

