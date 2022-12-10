# flow-money-api
API de controle financeiro pessoal

## Pré-requisitos

1. Java 17+
1. Maven 3.6
1. MySQL 8


## Scripts do Banco

[criar base de dados e estrutura com o script](https://github.com/alexandrepinho/flow-money-api/blob/main/sql/flowmoney-api.sql)

[inserts com usuários(admin e 2 comuns) e registros para cadastro de transação](https://github.com/alexandrepinho/flow-money-api/blob/main/sql/flowmoney-api.sql)

### Usuários no script de teste:
  1. admin@flowmoney.com senha admin
  1. joao@flowmoney.com senha teste123
  1. maria@flowmoney.com senha teste123

## Comandos do Maven

Instalando as dependências da aplicação

- `mvn clean install`

## Iniciando a aplicação

Usando o maven

- `mvn spring-boot:run`

Usando Java

- `java -jar target/flowmoney-api-1.0.0-SNAPSHOT.jar`

-Iniciará por padrão em localhost:8080
