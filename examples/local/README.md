# Local development example

Start the application
```shell
mvn clean compile quarkus:dev
```
The MockServer will start with fix port `1080` which is defined in the `application.properties`
```properties
%dev.quarkus.mockserver.devservices.config-dir=src/test/resources/mockserver-local
%dev.quarkus.mockserver.devservices.port=1080
```
The MockServer Dashboard [http://localhost:1080/mockserver/dashboard](http://localhost:1080/mockserver/dashboard)

Test the application
```shell
curl -X POST -H "Content-Type: application/json" --data '{"key":"1","value":"v"}' http://localhost:8080/test/1
```
