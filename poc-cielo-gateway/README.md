# README #

PoC - NPWiT para Accesstage - Cielo 3.0 API - Gateway / Zuul

### Requirements ###

* Java 8
* Maven 3.3.x
* Docker host or Docker machine
* Running Eureka cluster

### Building and executing the application from command line ###

```
mvn clean package
sudo java -Dspring.profiles.active=standalone -jar target/gateway.jar
```

Eureka cluster Open http://localhost:8001 in a browser

### How do I get set up using Docker? ###

```
sudo docker pull eheitor/poc-cielo-gateway
```

Simple container:
```
sudo docker run -idt -p 8090:8090 --net=host -e appPort=8090 -e hostName=$HOSTNAME -e eureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/" eheitor/poc-cielo-gateway
```
