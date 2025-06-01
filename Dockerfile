# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia arquivos Maven
COPY pom.xml .
COPY src ./src

# Compila o projeto e gera o .jar
RUN mvn clean package -DskipTests

# Etapa 2: imagem leve para executar o .jar
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o .jar gerado da etapa anterior
COPY --from=builder /app/target/wallet-service-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
