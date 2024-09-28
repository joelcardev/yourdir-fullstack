FROM openjdk:21-jdk-slim AS builder

# Instala o Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Compile o projeto
RUN mvn clean package

FROM openjdk:21-jdk-slim

WORKDIR /app

# Copie o arquivo jar compilado
COPY --from=builder /app/target/*.jar app.jar

# Comando para rodar o app
ENTRYPOINT ["java", "-jar", "app.jar"]

# Exponha a porta
EXPOSE 8080
