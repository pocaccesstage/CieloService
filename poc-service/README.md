# README #

PoC - NPWiT para Accesstage - Cielo 3.0 API - Services

### Requirements ###

* Java 8
* Maven 3.3.x
* Docker host or Docker machine
* Running Eureka cluster

### Building and executing the application from command line ###

```
mvn clean package
sudo java -DappPort=8501 -DhostName=$HOSTNAME -Deureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/" -jar poc-cielo-api/target/poc-cielo-api.jar
```

Eureka cluster Open http://localhost:8001

### How do I get set up using Docker? ###

```
sudo docker pull eheitor/poc-cielo-api
```

Simple container:
```
sudo docker run -idt -p 8501:8501 --net=host -e appPort=8501 -e hostName=$HOSTNAME -e eureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/" eheitor/poc-cielo-api
```

Multiple containers:
```
sudo docker run -idt -p 8501:8501 --net=host -e appPort=8501 -e hostName=$HOSTNAME -e eureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/,http://$HOSTNAME:8002/eureka/" eheitor/poc-cielo-api
sudo docker run -idt -p 8502:8502 --net=host -e appPort=8502 -e hostName=$HOSTNAME -e eureka.client.serviceUrl.defaultZone="http://$HOSTNAME:8001/eureka/,http://$HOSTNAME:8002/eureka/" eheitor/poc-cielo-api
```

Open http://localhost:8501/admin/info or http://localhost:8502/admin/info in a browser
