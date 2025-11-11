# Use an official lightweight OpenJDK 17 image
FROM openjdk:17-jdk-slim AS build

# Set working directory inside the container
WORKDIR /app

# Copy project files (Maven wrapper + pom.xml first for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY src src

# Build the JAR file (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# ===============================
# Final runtime image
# ===============================
FROM openjdk:17-jdk-slim

# Working directory
WORKDIR /app

# Copy built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port Render will assign
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
