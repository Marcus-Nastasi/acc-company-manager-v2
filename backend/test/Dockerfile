FROM openjdk:21-oracle

WORKDIR /app
COPY /target/test-0.0.1-SNAPSHOT.jar /app/test-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/test-0.0.1-SNAPSHOT.jar"]
