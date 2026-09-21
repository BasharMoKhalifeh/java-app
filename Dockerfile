# ---- Stage 1: build the jar ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom first so dependency layers are cached
COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src ./src
RUN mvn -q package -DskipTests

# ---- Stage 2: small runtime image ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Run as a non-root user
RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=build /app/target/app.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]