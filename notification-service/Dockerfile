FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar notification-service.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "notification-service.jar"]