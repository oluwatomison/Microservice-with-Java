# --- Stage 1: Build from Source ---
# We MUST use a JDK image here so Gradle can compile the code
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app
# Copy gradle wrapper and source code
COPY build.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
# Build the JAR
RUN ./gradlew build -x test

# --- Stage 2: Extract Layers ---
# We use a JRE here just to run the extraction command
FROM eclipse-temurin:17-jre-jammy AS extractor
WORKDIR /app
# Copy the built JAR from the builder stage and rename it
COPY --from=builder /app/build/libs/*.jar app.jar
# NOW we extract the layers, because the JAR actually exists!
RUN java -Djarmode=layertools -jar app.jar extract

# --- Stage 3: Create the Runtime Image ---
# Clean JRE for the final lightweight image
FROM eclipse-temurin:17-jre-jammy
# Creates a work directory called apps
WORKDIR /app

# Copy the extracted layers in a specific order to maximize Docker caching
# Build image based on parts changed with the code
# (Dependencies change least often, application code changes most often) Build only parts of the app that has changed
COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

EXPOSE 8081

# Run using the efficient JarLauncher
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]