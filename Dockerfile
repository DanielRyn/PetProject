FROM gradle:9.1.0-jdk17 AS builder
WORKDIR /app

COPY build.gradle /app/
RUN gradle clean build --no-daemon > /dev/null 2>&1 || true

COPY --chown=gradle:gradle . .
RUN gradle clean generateOpenApiAndBuild -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /app/opentelemetry-javaagent.jar ./

ENTRYPOINT ["java", "-javaagent:opentelemetry-javaagent.jar", "-Dotel.traces.exporter=otlp", "-Dotel.exporter.otlp.traces.endpoint=http://tempo:4318/v1/traces", "-Dotel.service.name=pet-service", "-Dotel.metrics.exporter=none", "-Dotel.logs.exporter=none", "-Dio.opentelemetry.javaagent.slf4j.simpleLogger.defaultLogLevel=off", "-jar", "app.jar"]