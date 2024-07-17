FROM openjdk:21-jdk
WORKDIR /app
COPY ./target/gym-app-main-mq-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]