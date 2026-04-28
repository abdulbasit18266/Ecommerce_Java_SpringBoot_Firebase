# Step 1: Use JDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Set working directory inside container
WORKDIR /app

# Step 3: Copy the jar file from your target folder
# Note: Render automatic build karega toh target folder mein jar hogi
COPY target/*.jar app.jar

# Step 4: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]