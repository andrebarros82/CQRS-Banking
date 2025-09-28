
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

# ==========================================================
# Build do projeto usando Maven + JDK
# ==========================================================
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Diretório de trabalho
WORKDIR /app

# Copia o pom.xml para aproveitar cache das dependências
COPY pom.xml .

# Baixa dependências
RUN mvn dependency:go-offline

# Copia o código-fonte
COPY src ./src

# Compila e gera o JAR
RUN mvn package -DskipTests


# ==========================================================
# Imagem final com Redis + JDK
# ==========================================================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Instala o Redis no container
RUN apt-get update \
    && apt-get install -y redis-server \
    && rm -rf /var/lib/apt/lists/*

# Copia o JAR gerado na primeira imagem
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Inicia o Redis e depois a aplicação Spring Boot
CMD redis-server --daemonize yes && java -jar app.jar
