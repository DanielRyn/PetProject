# --- Stage 1: Build ---
FROM gradle:9.1.0-jdk17 AS builder
WORKDIR /app

COPY --chown=gradle:gradle . .
RUN gradle dependencies --no-daemon
RUN gradle openApiGenerate
RUN gradle clean build -x test --no-daemon


FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
