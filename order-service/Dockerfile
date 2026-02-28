FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar order-service.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "order-service.jar"]