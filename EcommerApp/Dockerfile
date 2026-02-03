# Start with a base image with Java installed
FROM amazoncorretto:25-alpine-jdk AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from your target directory into the container
# Make sure to build your app first (e.g., using ./mvnw package)
COPY target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]