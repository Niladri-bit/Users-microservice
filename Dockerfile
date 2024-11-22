# Base image with Java 21
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from your local machine to the container
COPY target/UserService-0.0.1-SNAPSHOT.jar.original app.jar

# Expose the port your microservice will use
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
