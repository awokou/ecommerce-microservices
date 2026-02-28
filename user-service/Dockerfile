FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar user-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "user-service.jar"]