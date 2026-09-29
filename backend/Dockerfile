FROM maven:3.9.9-eclipse-temurin-17 AS dependencies
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

FROM dependencies AS build
COPY src src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S aerocadet && adduser -S aerocadet -G aerocadet
WORKDIR /app
COPY --from=build /workspace/target/aerocadet-api-0.1.0-SNAPSHOT.jar app.jar
RUN mkdir -p /app/uploads && chown -R aerocadet:aerocadet /app
USER aerocadet
EXPOSE 8080
HEALTHCHECK --interval=20s --timeout=5s --start-period=35s --retries=5 CMD wget -qO- http://127.0.0.1:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
