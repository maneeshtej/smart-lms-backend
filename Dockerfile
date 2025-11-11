# ---------- Build Stage ----------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper & config first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Preload dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the actual source code
COPY src src

# Build the jar
RUN ./mvnw clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port Render assigns
EXPOSE 8080

# Run the app on Render's assigned port
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
