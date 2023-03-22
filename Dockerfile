FROM openjdk:17-ea-slim
COPY build/libs/heytossme-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]