# Use the official OpenJDK 17 image as a parent image
FROM openjdk:17-oracle

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/*.jar /app/app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
