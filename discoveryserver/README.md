# README #

PoC - NPWiT para Accesstage - Cielo 3.0 API - Eureka Server

### Requirements ###

* Java 8
* Maven 3.3.x
* Docker host or Docker machine

### Building and executing application from command line ###

```
mvn clean package
java -Dspring.profiles.active=standalone -DappPort=8001 -jar target/discovery-server.jar
```

Open http://localhost:8001 in a browser

### How do I get set up using Docker? ###

```
sudo docker pull eheitor/discovery-server
```

Single container:
```
sudo docker run -idt -p 8001:8001 --net=host -e spring.profiles.active=standalone -e appPort=8001 -e hostName=$HOSTNAME -e dataCenter=npwit-cloud-dal -e environment=staging  eheitor/discovery-server
```

Open http://localhost:8001 in a browser
