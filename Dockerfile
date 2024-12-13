# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Argument for the JAR file
ARG JAR_FILE=target/moments-0.0.1-SNAPSHOT.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
