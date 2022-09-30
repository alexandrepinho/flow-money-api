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
-- Table `flowmoney-api`.`categoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `flowmoney-api`.`categoria` ;

CREATE TABLE IF NOT EXISTS `flowmoney-api`.`categoria` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  `tipo` SMALLINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


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
  `usuario_id` BIGINT(20) NOT NULL,
  `permissao_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`usuario_id`, `permissao_id`),
  INDEX `fk_usuario_has_permissao_permissao1_idx` (`permissao_id` ASC) VISIBLE,
  INDEX `fk_usuario_has_permissao_usuario_idx` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_has_permissao_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `flowmoney-api`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_permissao_permissao1`
    FOREIGN KEY (`permissao_id`)
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
  `saldo` DECIMAL NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`, `usuario`),
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
  `valor` DECIMAL NOT NULL,
  `tipo` SMALLINT(1) NOT NULL,
  `descricao` VARCHAR(100) NOT NULL,
  `data` DATE NOT NULL,
  `categoria` BIGINT(20) NOT NULL,
  `usuario` BIGINT(20) NOT NULL,
  `conta` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`, `categoria`, `usuario`, `conta`),
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
