
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

# Stage 1: Build do projeto usando Maven + JDK
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml primeiro para cache de dependências
COPY pom.xml .

# Baixa todas as dependências do projeto (para cache)
RUN mvn dependency:go-offline

# Copia o código-fonte
COPY src ./src

# Build do projeto (gera o jar)
RUN mvn package -DskipTests

# Stage 2: Imagem final mais leve apenas com o JDK e o jar
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o jar buildado da imagem anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar o jar
ENTRYPOINT ["java", "-jar", "app.jar"]