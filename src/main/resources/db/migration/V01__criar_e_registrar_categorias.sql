CREATE TABLE categoria (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	tipo SMALLINT(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria(nome,tipo) values('Lazer',2);
INSERT INTO categoria(nome,tipo) values('Alimentacao',2);
INSERT INTO categoria(nome,tipo) values('Supermercado',2);
INSERT INTO categoria(nome,tipo) values('Salário',1);
INSERT INTO categoria(nome,tipo) values('Outros',2);

