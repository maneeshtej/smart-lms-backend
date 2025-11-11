# Use an official OpenJDK 17 runtime
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY . .

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose the port Render will assign (Render uses PORT env var)
EXPOSE 8080

# Run the Spring Boot JAR
CMD ["sh", "-c", "java -jar target/*.jar --server.port=$PORT"]
