FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar cart-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "cart-service.jar"]