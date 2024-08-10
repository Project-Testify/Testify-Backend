# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder
LABEL authors="nirmal"
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B package --file pom.xml -DskipTests

# Stage 2: Create the final Docker image
FROM openjdk:21-oracle
LABEL authors="nirmal"
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
