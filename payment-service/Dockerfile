FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar payment-service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "payment-service.jar"]