# README #

PoC - NPWiT para Accesstage - Cielo 3.0 API

## Introdução ##

O objetivo deste projeto é a utilização de um microserviço que acessa a biblioteca do Cielo 3.0, uma nova API REST com ambiente de teste dedicado. Das diversas funções que a API fornece, foram escolhidas 3 para testes: pagamento com cartão de crédito via transação simples, consulta de uma venda realizada pelo PaymentId e geração de um código criptografrado chamado de “token” do número do cartão de crédito, que poderá ser armazenado em banco de dados.

Este microserviço foi construído usando Spring Boot, Spring Cloud, Netflix Eureka, Netflix Zuul, Netflix Ribbon, Feign e Docker. Também foi utilizado o SDK API-3.0 Java da Cielo. Há diversas funcionalidades desta API que podem ser desenvolvidas e até a troca do SDK da Cielo por um próprio que seja mais adequado às regras do negócio.

Neste projeto não consta alguns itens originais da PoC, por simples falta de tempo hábil.

- Kubernetes: demonstrado anteriormente na primeira versão e publicado no Google Cloud

- uso de banco de dados PostgreSQL: demonstrado anteriormente no microserviço Task Manager

- uso do Maven com SCM: porém há um projeto com o Parent POM/Maven, para fins de demonstração, sem o SCM

- Gitflow: não houve mais que uma versão, mas entendemos o conceito apresentado em https://github.com/accesstage/accesstage.github.io/blob/master/_posts/2017-03-27-gitflow-na-pratica.md


## Elementos funcionais ##

Para proxy service / gateway e filtro da solução, foi utilizada Spring Netflix Zuul, usado para lidar com solicitações encaminhando-as para o serviço de back-end apropriado e este roteia para o apropriado microserviço. O Cielo Service é registrado no Netflix Eureka Server, usado neste projeto para descoberta do lado do cliente, quando o cliente é responsável por determinar as localizações das instâncias de serviço disponíveis. Há uma interface do Eureka que mostra os serviços e as instâncias que rodam.

O Eureka Client fornece uma lista dinâmica dos serviços para que o Ribbon faça o balanceamento entre eles. Foi usado o Feign para realizar a integração do Ribbon.


 
Method	| Path	| Description	
------------- | ------------------------------------ | --------------------- |
GET	| /poc-cielo-api-eurekaclient/aggregation/payments1/{id}	| Retorna dados de pagamento previamente realizados 	
GET	| /poc-cielo-api-eurekaclient/aggregation/payments1/token/{id}	| Converte o número do cartão para token
GET	| /poc-cielo-api-eurekaclient/newsale/{valor}/{cartao}/{cliente}	| Realiza nova venda com cartão de crédito no Cielo Sandbox


## Testes ##

Para esta PoC, foram definidos em código os dados merchantId e merchantId da Cielo Sandbox obtidos no site da Cielo. Também, somente para testes, deixado hardcoded algumas outras informações, como bandeira do cartão, código-secreto, merchant order.

Rodar os 4 containers citados dentro das subpastas deste projeto. Abaixo um exemplo de verificação de processo rodando:
```
docker ps
CONTAINER ID        IMAGE                                COMMAND                  CREATED             STATUS              PORTS               
f73d27415321        eheitor/poc-cielo-gateway            "java -jar gateway..."   About an hour ago   Up About an hour
41d7124b63bc        eheitor/poc-cielo-api-eurekaclient   "java -jar poc-cie..."   About an hour ago   Up About an hour
f61ac9ed4e3b        eheitor/poc-cielo-api                "java -jar poc-cie..."   About an hour ago   Up About an hour
3f838bf6130c        eheitor/discovery-server             "java -jar discove..."   31 hours ago        Up 31 hours
```


- exemplo de coleta de informações de venda:
curl http://localhost:8090/poc-cielo-api-eurekaclient/aggregation/payments1/00942849-2ac0-4cf0-a6db-a26da837ab28
{"payment":"00942849-2ac0-4cf0-a6db-a26da837ab28","request":"661501","cardToken":null,"merchantOrder":"2017041601","amount":37,"status":1}

- exemplo de conversão de um número de cartão de crédito para token, com a finalidade de armazenar em banco para uso futuro: 
curl http://localhost:8090/poc-cielo-api-eurekaclient/aggregation/payments1/token/0000000000000001
1aa94ecb-5b20-4cdc-821c-743ec6eb2c04

- exemplo de execução de uma nova transação simples de cartão de crédito:
curl -v http://localhost:8090/poc-cielo-api-eurekaclient/aggregation/payments1/newsale/37/0000000000000001/Gabriel-NPWiT
{"payment":"6465b812-b8b0-4f80-a630-50aef7ef7f41","request":"12345678-0001-1234-1234-123456789012","cardToken":"ef7d6409-6e50-4bd8-a4dc-0358737b9522","merchantOrder":"2017041601","amount":37,"status":4}


## Deploy ##

Verificar dentro de cada pasta do projeto as instruções para deploy utilizando o Docker.