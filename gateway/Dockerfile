# Use a Maven image with Java 17 for building
FROM maven:3.8.3-openjdk-17-slim AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Compile the application
RUN mvn clean package

# Use a lightweight Java image with Java 17 for the runtime
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled application from the builder stage
COPY --from=builder /app/target/*.jar ./app.jar

# Specify the command to run the application
CMD ["java", "-jar", "app.jar"]
