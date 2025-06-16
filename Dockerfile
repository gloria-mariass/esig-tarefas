# Maven 3.9.9 com JDK 11
FROM maven:3.9.6-eclipse-temurin-11 AS builder

# Isso serve como a raiz do seu projeto Maven para o build.
WORKDIR /app

# Copia o arquivo pom.xml
COPY pom.xml .

# Baixa as dependências do Maven
RUN mvn dependency:go-offline -B

# Copia o código fonte do projeto para o contêiner.
COPY src ./src

# Compila o projeto e empacota o WAR.
RUN mvn clean package -DskipTests

# LINHA DE DEBUG: Garante que qualquer falha de comando irá parar o build do Docker.
RUN set -e

# LINHA DE DEBUG: Lista o conteúdo do diretório target para verificar o WAR.
RUN ls -l /app/target/

# Use uma imagem base de Tomcat com JDK 11
FROM tomcat:9-jdk11-temurin

# Define o diretório de trabalho dentro do contêiner
WORKDIR /usr/local/tomcat

# Remove as aplicações padrão do Tomcat para manter a imagem limpa
RUN rm -rf webapps/*

# Copia o arquivo .war da sua aplicação para o diretório webapps do Tomcat.
# A pasta "target" é criada pelo Maven durante o build.
COPY --from=builder /app/target/gestao-tarefas.war webapps/ROOT.war

# (Opcional) Configura opções JVM, como o charset e limites de memória.
ENV JAVA_OPTS="-Dfile.encoding=UTF-8 -Xms256m -Xmx512m"

# Expõe a porta padrão do Tomcat
EXPOSE 8080

# Comando para iniciar o Tomcat em primeiro plano
CMD ["catalina.sh", "run"]