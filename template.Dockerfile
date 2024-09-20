# Build stage with Java 21 image
FROM maven:3.8.1-openjdk-21 AS builder

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Package the application, skipping tests to speed up the build
RUN mvn clean package -DskipTests

# Base image for running the application (using Java 21)
FROM eclipse-temurin:21-jre

# Set the working directory inside the container
WORKDIR /usr/src/app

# Expose the application port
EXPOSE 8080

# Copy the JAR from the build stage
COPY --from=builder /usr/src/app/target/*.jar /usr/src/app/app.jar

# Entrypoint and command to run the application
ENTRYPOINT ["java", "-jar", "/usr/src/app/app.jar"]
