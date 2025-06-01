# Usa a imagem base com Java 21 (compatível com seu pom.xml)
FROM eclipse-temurin:21-jdk

# Define o argumento com o caminho do JAR
ARG JAR_FILE=target/wallet-service-0.0.1-SNAPSHOT.jar

# Copia o JAR para dentro da imagem
COPY ${JAR_FILE} app.jar

# Expõe a porta padrão da aplicação
EXPOSE 8080

# Comando de entrada para rodar o app
ENTRYPOINT ["java", "-jar", "/app.jar"]
