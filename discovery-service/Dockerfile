FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

COPY build.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre-jammy AS extractor
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

EXPOSE 8761

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]