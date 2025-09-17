# Build stage
FROM eclipse-temurin:24-jdk as builder

WORKDIR /app

# Copy Gradle files
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# Add execution permissions to gradlew
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew build -x test --no-daemon

# Run stage
FROM eclipse-temurin:24-jre

WORKDIR /app

# Copy the built artifact from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
