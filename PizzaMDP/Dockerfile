# Stage 1: Build the application
FROM maven:3.8.5-openjdk-8-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:8-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/pizzamdp-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
RUN mkdir -p /app/data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
