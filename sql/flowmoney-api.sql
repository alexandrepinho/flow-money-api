-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema flowmoney-api
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema flowmoney-api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `flowmoney-api` DEFAULT CHARACTER SET utf8 ;
USE `flowmoney-api` ;

-- -----------------------------------------------------
-- Table `flowmoney-api`.`usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`usuario` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`usuario` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `senha` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`categoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`categoria` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`categoria` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  `tipo` SMALLINT(1) NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  `arquivada` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_categoria_usuario1_idx` (`usuario` ASC) VISIBLE,
  CONSTRAINT `fk_categoria_usuario1`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`permissao`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`permissao` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`permissao` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`usuario_permissao`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`usuario_permissao` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`usuario_permissao` (
  `usuario` BIGINT(20) NOT NULL,
  `permissao` BIGINT(20) NOT NULL,
  PRIMARY KEY (`usuario`, `permissao`),
  INDEX `fk_usuario_has_permissao_permissao1_idx` (`permissao` ASC) VISIBLE,
  INDEX `fk_usuario_has_permissao_usuario_idx` (`usuario` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_has_permissao_usuario`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_permissao_permissao1`
    FOREIGN KEY (`permissao`)
    REFERENCES `flowmoney-api`.`permissao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`conta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`conta` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`conta` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(100) NOT NULL,
  `saldo` DECIMAL(10,2) NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  `arquivada` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_conta_usuario1_idx` (`usuario` ASC) VISIBLE,
  CONSTRAINT `fk_conta_usuario1`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`transacao`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`transacao` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`transacao` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(10,2) NOT NULL,
  `tipo` SMALLINT(1) NOT NULL,
  `descricao` VARCHAR(100) NOT NULL,
  `data` DATE NOT NULL,
  `categoria` BIGINT(20) NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  `conta` BIGINT(20) NOT NULL,
  `efetuada` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_transacao_categoria1_idx` (`categoria` ASC) VISIBLE,
  INDEX `fk_transacao_usuario1_idx` (`usuario` ASC) VISIBLE,
  INDEX `fk_transacao_conta1_idx` (`conta` ASC) VISIBLE,
  CONSTRAINT `fk_transacao_categoria1`
    FOREIGN KEY (`categoria`)
    REFERENCES `flowmoney-api`.`categoria` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transacao_usuario1`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transacao_conta1`
    FOREIGN KEY (`conta`)
    REFERENCES `flowmoney-api`.`conta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`cartao_credito`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`cartao_credito` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`cartao_credito` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `limite` DECIMAL(10,2) NOT NULL,
  `descricao` VARCHAR(45) NOT NULL,
  `data_fechamento` DATE NOT NULL,
  `data_vencimento` DATE NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_cartao_credito_usuario1_idx` (`usuario` ASC) VISIBLE,
  CONSTRAINT `fk_cartao_credito_usuario1`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`fatura`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`fatura` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`fatura` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `valor_total` DECIMAL(10,2) NOT NULL,
  `pago` TINYINT(1) NOT NULL DEFAULT 0,
  `data_pagamento` DATE NULL,
  `conta` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_fatura_conta1_idx` (`conta` ASC) VISIBLE,
  CONSTRAINT `fk_fatura_conta1`
    FOREIGN KEY (`conta`)
    REFERENCES `flowmoney-api`.`conta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '	';


-- -----------------------------------------------------
-- Table `flowmoney-api`.`lancamento_fatura`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`lancamento_fatura` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`lancamento_fatura` (
  `id` BIGINT(20) NOT NULL,
  `data` DATE NOT NULL,
  `descricao` VARCHAR(45) NOT NULL,
  `parcelado` TINYINT(1) ZEROFILL NOT NULL,
  `qtd_parcelas` INT ZEROFILL NOT NULL,
  `fatura` BIGINT(20) NOT NULL,
  `cartao_credito` BIGINT(20) NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  `categoria` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_lancamento_fatura_fatura1_idx` (`fatura` ASC) VISIBLE,
  INDEX `fk_lancamento_fatura_cartao_credito1_idx` (`cartao_credito` ASC) VISIBLE,
  INDEX `fk_lancamento_fatura_usuario1_idx` (`usuario` ASC) VISIBLE,
  INDEX `fk_lancamento_fatura_categoria1_idx` (`categoria` ASC) VISIBLE,
  CONSTRAINT `fk_lancamento_fatura_fatura1`
    FOREIGN KEY (`fatura`)
    REFERENCES `flowmoney-api`.`fatura` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lancamento_fatura_cartao_credito1`
    FOREIGN KEY (`cartao_credito`)
    REFERENCES `flowmoney-api`.`cartao_credito` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lancamento_fatura_usuario1`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lancamento_fatura_categoria1`
    FOREIGN KEY (`categoria`)
    REFERENCES `flowmoney-api`.`categoria` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`objetivo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`objetivo` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`objetivo` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `valor` DECIMAL(10,2) NOT NULL,
  `valor_inicial` DECIMAL(10,2) NOT NULL,
  `valor_restante` DECIMAL(10,2) NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_objetivo_usuario1_idx` (`usuario` ASC) VISIBLE,
  CONSTRAINT `fk_objetivo_usuario1`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`planejamento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`planejamento` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`planejamento` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `valor_total` DECIMAL(10,2) NOT NULL,
  `data_inicial` DATE NOT NULL,
  `data_final` VARCHAR(45) NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_planejamento_usuario1_idx` (`usuario` ASC) VISIBLE,
  CONSTRAINT `fk_planejamento_usuario1`
    FOREIGN KEY (`usuario`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `flowmoney-api`.`planejamento_categoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`planejamento_categoria` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`planejamento_categoria` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(10,2) NOT NULL,
  `categoria` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_planejamento_categoria_categoria1_idx` (`categoria` ASC) VISIBLE,
  CONSTRAINT `fk_planejamento_categoria_categoria1`
    FOREIGN KEY (`categoria`)
    REFERENCES `flowmoney-api`.`categoria` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
