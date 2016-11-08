Run manual:
    install java (https://java.com/ru/download/)
    install maven (https://maven.apache.org/download.cgi/)
    set properties:
        services/src/main/resources/application.properties
        web/src/main/resources/application.properties
    cd <project_path>/
    mvn clean package -DskipTests
    java -jar services/target/services-0.0.1-SNAPSHOT.jar
    java -jar web/target/web-0.0.1-SNAPSHOT.jar
    open browser: http://localhost:8081/

Comment:
   TestDataProvider.java is responsible for adding test data into database

Next steps:
    handle errors
    use DTO instead of jpa entities as api
    use binary protocol (e.g. protobuf, thrift) for interaction between services and web
    ...
