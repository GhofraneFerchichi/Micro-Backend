# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files
COPY ./microservice-produits /app/mproduits
COPY ./microservice-panier /app/mpanier

# Resolve dependencies and build mproduits
RUN mvn -f mproduits/pom.xml clean install -DskipTests

# Resolve dependencies and build mcommandes
# Build mpaniercd
RUN mvn -f mpanier/pom.xml clean package -DskipTests

# Use AdoptOpenJDK image as the base image for the final image
FROM adoptopenjdk/openjdk11:alpine-jre

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file from the builder stage to the final image
COPY --from=builder /app/mpanier/target/mpanier-0.0.1-SNAPSHOT.jar /app/mpanier.jar

# Expose ports if needed
# EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "mpanier.jar"]













