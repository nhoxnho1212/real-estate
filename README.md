# real-estate

## Supported versions

The real-estate supports the following versions.
- [Maven 3](https://maven.apache.org/)
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Spring Boot 2.4]()

## Building and testing

To build and test application:
```shell
./mvnw clean package
```

To build and skip test:
```shell
./mvnw clean package -DskipTests
```

## Running application

To run application with default profile:
```shell
java -jar target\real-estate-0.0.1-SNAPSHOT.jar
```

To run and override configuration with dev profile:
```shell
java -jar target\real-estate-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

Before running the project, you must override necessary individual properties:
```yaml
# Properties to connect your MySQL server
spring:
  datasource:
    url: $DATASOURCE_URL
    username: $DATASOURCE_USR
    password: $DATASOURCE_PWD
    
# When using the application, if you have a CORS issue when the front-end server connects, 
# you must add the HTTP address to this server to allow the front-end address.
jhipster:
    cors:
        allowed-origins: $HTTP_ADDRESS
```
