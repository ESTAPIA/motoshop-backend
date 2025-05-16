FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY motorshop-api/target/motorshop-api-*.jar app.jar

EXPOSE 9090

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
