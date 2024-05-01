# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-8 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files
COPY /Micro-Backend/microservice-produits /app/mproduits
COPY . /app/mcommandes

# Resolve dependencies and build mproduits
RUN mvn -f mproduits/pom.xml clean install -DskipTests

# Create a directory to store the mproduits artifact in mcommandes
RUN mkdir -p /app/mcommandes/libs

# Copy the mproduits artifact into the mcommandes/libs directory
RUN cp /app/mproduits/target/mproduits-0.0.1-SNAPSHOT.jar /app/mcommandes/libs/mproduits.jar

# Build mcommandes
RUN mvn -f mcommandes/pom.xml clean package -DskipTests

# Use AdoptOpenJDK image as the base image for the final image
FROM adoptopenjdk/openjdk8:alpine-jre

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file from the builder stage to the final image
COPY --from=builder /app/mcommandes/target/mcommandes-0.0.1-SNAPSHOT.jar /app/mcommandes.jar

# Expose ports if needed
# EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "mcommandes.jar"]
