FROM eclipse-temurin:21-jdk-alpine
WORKDIR /opt/app
COPY build/*.jar app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=80"]