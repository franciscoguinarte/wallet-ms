#!/bin/bash

echo "🔄 Limpando e empacotando o projeto..."
./mvnw clean package -DskipTests || { echo "❌ Falha na compilação."; exit 1; }

echo "🐳 Subindo os containers com Docker Compose..."
docker-compose up --build