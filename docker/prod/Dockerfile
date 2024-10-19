# Step 1: Build the Spring Boot application
FROM maven:3.8.5-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Step 2: Create the Docker image with the built JAR file
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the runtime image
COPY --from=build /app/target/*.jar app.jar

# Expose the port on which the application runs

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]