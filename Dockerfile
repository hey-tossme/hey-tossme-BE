FROM openjdk:17-oraclelinux7
COPY build/libs/heytossme-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]