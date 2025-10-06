# Etapa 1: build do projeto
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -Dmaven.test.skip=true

# Etapa 2: runtime (execução)
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Render define a porta dinamicamente via SERVER_PORT
ENV PORT=10000
EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
