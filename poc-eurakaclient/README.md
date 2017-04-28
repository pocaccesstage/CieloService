# README #

PoC - NPWiT para Accesstage - Cielo 3.0 API - Eureka client

### Requirements ###

* Java 8
* Maven 3.3.x
* Docker host or Docker machine
* Running Eureka cluster

### Building and executing the application from command line ###

```
mvn clean package
sudo java -DappPort=8601 -DhostName=$HOSTNAME -Deureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/" -jar target/poc-cielo-api-eurekaclient.jar
```

Eureka cluster Open http://localhost:8001 in a browser

### How do I get set up using Docker? ###

```
sudo docker pull eheitor/poc-cielo-api-eurekaclient

Single container:
```
sudo docker run -idt -p 8601:8601 --net=host -e appPort=8601 -e hostName=$HOSTNAME -e eureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/" eheitor/poc-cielo-api-eurekaclient
```

Multiple containers:
```
sudo docker run -idt -p 8601:8601 --net=host -e appPort=8601 -e hostName=$HOSTNAME -e eureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/,http://$HOSTNAME:8002/eureka/" eheitor/poc-cielo-api-eurekaclient
sudo docker run -idt -p 8602:8602 --net=host -e appPort=8602 -e hostName=$HOSTNAME -e eureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/,http://$HOSTNAME:8002/eureka/" eheitor/poc-cielo-api-eurekaclient
```

Open http://localhost:8601/admin/info or http://localhost:8602/admin/info in a browser
