# Microbackend

**Microbackend** is a Spring Boot microservice application designed for seamless integration with MySQL. It includes Dockerfiles and Kubernetes manifest files for easy deployment to a K8s cluster, ensuring scalability and efficient resource management.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Spring Boot**: Robust and easy-to-use framework for building Java applications.
- **MySQL Integration**: Reliable and scalable database support.
- **Docker Support**: Containerize your application with Docker for consistent environments.
- **Kubernetes Deployment**: Deploy seamlessly to a Kubernetes cluster using manifest files.
- **Scalable Architecture**: Designed to handle growing demands efficiently.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 11 or higher
- Docker
- Kubernetes (Minikube or a Kubernetes cluster)
- MySQL

## Getting Started

### Clone the Repository

git clone https://github.com/Ghofrane-Ferchichi/microbackend.git
cd microbackend

## Set Up MySQL Database

 - Install MySQL.
 - Create a new database:
Copier le code
CREATE DATABASE microbackend_db;

Update the database configuration in application.properties:
properties :

spring.datasource.url=jdbc:mysql://localhost:3306/microbackend_db
spring.datasource.username=your_username
spring.datasource.password=your_password


## Build and Run the Application
Build the project:
./mvnw clean install
Run the application:

./mvnw spring-boot:run

## Usage
Once the application is running, you can access it at http://localhost:8080. You can use tools like Postman or curl to interact with your microservices.

## Deployment
Docker
- Build the Docker image:

docker build -t microbackend .
Run the Docker container:

docker run -p 8080:8080 microbackend
Kubernetes
- Ensure your Kubernetes cluster is up and running.
- Apply the manifest files:

Copier le code
kubectl apply -f k8s/
Check the status of your pods:

kubectl get pods


## Contributing
Contributions are welcome! Please open an issue or submit a pull request. Follow the contribution guidelines for more details.

License
This project is licensed under the MIT License. See the LICENSE file for details.

Feel free to use this code in your `README.md` file!
