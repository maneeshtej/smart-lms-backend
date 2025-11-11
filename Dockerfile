# ---------- Build Stage ----------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper & configuration first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make sure the Maven wrapper is executable (Render sometimes drops exec bit)
RUN chmod +x mvnw

# Preload dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Render dynamically assigns a port via $PORT
# Use this to ensure Tomcat binds to the correct port
EXPOSE 10000

# Run Spring Boot with Render’s assigned port
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
