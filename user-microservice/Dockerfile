# Use an official Maven image as the base image for the builder stage
FROM maven:3.8.4-openjdk-11 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files
COPY . /app

# Resolve dependencies and build the project
RUN mvn clean package -DskipTests

# Use a lightweight Java runtime image as the final base image
FROM adoptopenjdk/openjdk11:alpine-jre

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file from the builder stage to the final image
COPY --from=builder /app/target/user-microservice-0.0.1-SNAPSHOT.jar /app/user-microservice.jar

# Expose ports if needed
# EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "user-microservice.jar"]
