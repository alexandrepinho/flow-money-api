INSERT INTO usuario (id, nome, email, senha) values (1, 'Administrador', 'admin@flowmoney.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO usuario (id, nome, email, senha) values (2, 'João', 'joao@flowmoney.com', '$2a$10$VGjYee2NKOn5rhh.gbRT8exzLA2HT50E3Cime8WunMKJ5rBFqTzlu');
INSERT INTO usuario (id, nome, email, senha) values (3, 'Maria', 'maria@flowmoney.com', '$2a$10$VGjYee2NKOn5rhh.gbRT8exzLA2HT50E3Cime8WunMKJ5rBFqTzlu');

INSERT INTO permissao (id, descricao) values (1, 'CRUD_USUARIOS');
INSERT INTO permissao (id, descricao) values (2, 'CRUD_TRANSACOES'); 
-- VERIFICAR NECESSIADE DESTA SEGUNDA PERMISSÃO, TENDO EM VISTA QUE TODOS OS USUÁRIOS CADASTRADOS PODERÃO TER ACESSO AO CRUD DE TRANSAÇÕES

-- 3 perfis -  administrador com acesso para cadastrar novos usuários e 2 usuários comuns sem gerencia de outros usuários mas com gerencia das suas transações

-- Admin PERMISSÃO DE CADASTRO DE TRANSAÇÕES E USUÁRIOS
INSERT INTO usuario_permissao (usuario, permissao) values (1, 1);
INSERT INTO usuario_permissao (usuario, permissao) values (1, 2);
-- tipos categorias especiais
INSERT INTO categoria (nome, tipo, usuario) values ('Reajuste Saída', 1, 1);
INSERT INTO categoria (nome, tipo, usuario) values ('Reajuste Entrada', 2, 1);
INSERT INTO categoria (nome, tipo, usuario) values ('Pagamento parcial de cartão', 2, 1);
-- tipo saídas
INSERT INTO categoria (nome, tipo, usuario) values ('Casa', 1, 1);
INSERT INTO categoria (nome, tipo, usuario) values ('Educação', 1, 1);
INSERT INTO categoria (nome, tipo, usuario) values ('Eletrônicos', 1, 1);
INSERT INTO categoria (nome, tipo, usuario) values ('Lazer', 1, 1);
INSERT INTO categoria (nome, tipo, usuario) values ('Reajuste Saída', 1, 1);
-- tipo entradas
INSERT INTO categoria (nome, tipo, usuario) values('Investimento', 2, 1);
INSERT INTO categoria (nome, tipo, usuario) values('Prêmio', 2, 1);
INSERT INTO categoria (nome, tipo, usuario) values('Presente', 2, 1);
INSERT INTO categoria (nome, tipo, usuario) values('Salário', 2, 1);
-- contas
INSERT INTO conta (descricao, saldo, usuario) values('Conta 1', 3000, 1);
INSERT INTO conta (descricao, saldo, usuario) values('Conta 2', 2000, 1);

-- João PERMISSÃO SOMENTE DE CADASTRO DE TRANSAÇÕES
INSERT INTO usuario_permissao (usuario, permissao) values (2, 2);

-- Maria PERMISSÃO SOMENTE DE CADASTRO DE TRANSAÇÕES
INSERT INTO usuario_permissao (usuario, permissao) values (3, 2);

-- Cadastros para teste de categorias------------------------------------
-- João
-- tipos categorias especiais
INSERT INTO categoria (nome, tipo, usuario) values ('Reajuste Saída', 1, 2);
INSERT INTO categoria (nome, tipo, usuario) values ('Reajuste Entrada', 2, 2);
INSERT INTO categoria (nome, tipo, usuario) values ('Pagamento parcial de cartão', 2, 2); 
-- tipo saídas
INSERT INTO categoria (nome, tipo, usuario) values ('Casa', 1, 2);
INSERT INTO categoria (nome, tipo, usuario) values ('Educação', 1, 2);
INSERT INTO categoria (nome, tipo, usuario) values ('Eletrônicos', 1, 2);
INSERT INTO categoria (nome, tipo, usuario) values ('Lazer', 1, 2);
-- tipo entradas
INSERT INTO categoria (nome, tipo, usuario) values('Investimento', 2, 2);
INSERT INTO categoria (nome, tipo, usuario) values('Prêmio', 2, 2);
INSERT INTO categoria (nome, tipo, usuario) values('Presente', 2, 2);
INSERT INTO categoria (nome, tipo, usuario) values('Salário', 2, 2);
-- contas
INSERT INTO conta (descricao, saldo, usuario) values('Conta 1', 3000, 2);
INSERT INTO conta (descricao, saldo, usuario) values('Conta 2', 2000, 2);

-- Maria -
-- tipos categorias especiais
INSERT INTO categoria (nome, tipo, usuario) values ('Reajuste Saída', 1, 3);
INSERT INTO categoria (nome, tipo, usuario) values ('Reajuste Entrada', 2, 3);
INSERT INTO categoria (nome, tipo, usuario) values ('Pagamento parcial de cartão', 2, 3);  
-- tipo saídas
INSERT INTO categoria (nome, tipo, usuario) values ('Supermercado', 1, 3);
INSERT INTO categoria (nome, tipo, usuario) values ('Transporte', 1, 3);
INSERT INTO categoria (nome, tipo, usuario) values ('Vestuário', 1, 3);
INSERT INTO categoria (nome, tipo, usuario) values ('Viagem', 1, 3);
-- tipo entradas
INSERT INTO categoria (nome, tipo, usuario) values('Investimento', 2, 3);
INSERT INTO categoria (nome, tipo, usuario) values('Prêmio', 2, 3);
INSERT INTO categoria (nome, tipo, usuario) values('Presente', 2, 3);
INSERT INTO categoria (nome, tipo, usuario) values('Salário', 2, 3);
-- contas
INSERT INTO conta (descricao, saldo, usuario) values('Conta 1', 3000, 3);
INSERT INTO conta (descricao, saldo, usuario) values('Conta 2', 2000, 3);



