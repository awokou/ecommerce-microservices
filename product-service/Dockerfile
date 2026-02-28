FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar product-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "product-service.jar"]