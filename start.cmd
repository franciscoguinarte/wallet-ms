@echo off
echo 🔄 Limpando e empacotando o projeto...
call mvnw.cmd clean package -DskipTests
if ERRORLEVEL 1 (
    echo ❌ Falha na compilação.
    exit /b 1
)

echo 🐳 Subindo os containers com Docker Compose...
docker-compose up --build
