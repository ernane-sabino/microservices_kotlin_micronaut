## Sobre este projeto

* Ao realizar uma venda, o microsserviço de loja se comunica com o microsserviço de veículos que recupera o veículo de um banco de dados PostgreSQL
* Foi implementado um circuit breaker para que, caso haja indisponibilidade do microsserviço de veículos, os dados do veículo sejam recuperados do cache no Redis.
* Os dados da venda gerada são armazenados em um tópico do Kafa para que o microsserviço de relatório possa consumi-los e gravá-los no MongoDB. Este microsserviço também possui um endpoint que retona todas as vendas realizadas.
* Todos os microsserviços se registram no Consul (service discovery) permitindo que a comunicação entre eles seja dinâmica.
* Foi implementada também uma api-gateway alocada na porta 9090 com balanceamento de carga para acessar todos os microsserviços.

Exemplo de solicitações utilizadas:

Cadastro de novo veículo - Microsserviço Veículos
	- Requisição POST
		- http://localhost:9090/veiculo-service/veiculos
	- Dados da requisição:
		{
			"marca": "string",
			"modelo": "string",
			"placa": "string"
		}

Recuperação de veículos cadastrados - Microsserviço Veículos
	- Requisição GET
		- http://localhost:9090/veiculo-service/veiculos/{id}

Efetuar uma venda - Microsserviço Loja
	- Requisição POST
		- http://localhost:9090/loja-service/vendas
	- Dados da requisição:
		{
			"cliente": "string",
			"veiculo": int {id do veiculo},
			"valor": decimal,
			"quantidadeParcelas": int
		}


Listagem das vendas realizadas - Microsserviço Relatório
	- Requisição GET
		- http://localhost:9090/relatorio-service/vendas

Todos os microsserviçoes e aplicações estão em uma rede docker.

## Arquitetura de Microserviços

![image](https://user-images.githubusercontent.com/59898958/138009488-ee7427b5-b824-41c2-adf4-084954ded4de.png)

## Instruções

### Criando rede no docker:

```
docker network create micronaut-net
```

### Criando imagem Postgresql no Docker:

```
docker run --name ms-postgres --network micronaut-net -e "POSTGRES_PASSWORD=123456" -p 5432:5432 -d postgres
```

### Criando imagem Redis no Docker:

```
docker run --name ms-redis --network micronaut-net -p 6379:6379 -d redis
docker exec -it ms-redis redis-cli
```

### Criando imagem Mongo no Docker:

```
docker run -p 27017:27017 --name ms-mongo --network micronaut-net -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=e296cd9f mongo
docker run -it --rm --network micronaut-net mongo mongo --host ms-mongo -u root -p e296cd9f --authenticationDatabase admin vendas
```

### Criando imagem Kafka no Docker:

```
docker run -d --name zookeeper-server --network micronaut-net -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest
docker run -d --name kafka-server --network micronaut-net -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper-server:2181 bitnami/kafka:latest
```

### Criando imagem Consul no Docker:

```
docker run --network micronaut-net -p 8500:8500 --name ms-consul consul
```

### Criando imagem gateway no Docker:

```
docker build -t api-gateway:v1 .
docker run -p 9090:9090 --name api-gateway --network micronaut-net api-gateway:v1
```

### Criando imagem veiculo-service no Docker:

```
docker build -t veiculo-service:v1 .
docker run -P --network micronaut-net veiculo-service:v1
```

### Criando imagem loja-service no Docker:

```
docker build -t loja-service:v1 .
docker run -P --network micronaut-net loja-service:v1
```

### Criando imagem relatorio-service no Docker:

```
docker build -t relatorio-service:v1 .
docker run -P --network micronaut-net relatorio-service:v1
```
