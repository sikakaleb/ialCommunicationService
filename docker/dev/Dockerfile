FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and project files
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

# Copy the source code
COPY src src

# Install Spring Boot DevTools for hot-reloading
RUN ./mvnw install

# Command to run the application using Spring Boot's hot-reload feature
CMD ["./mvnw", "spring-boot:run"]
