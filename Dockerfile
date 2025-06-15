# Use uma imagem base de Tomcat com JDK 11
FROM tomcat:9-jdk11-temurin

# Define o diretório de trabalho dentro do contêiner
WORKDIR /usr/local/tomcat

# Remove as aplicações padrão do Tomcat para manter a imagem limpa
RUN rm -rf webapps/*

# Copia o arquivo .war da sua aplicação para o diretório webapps do Tomcat.
# A pasta "target" é criada pelo Maven durante o build.
COPY target/gestao-tarefas.war webapps/ROOT.war

# (Opcional) Configura opções JVM, como o charset e limites de memória.
ENV JAVA_OPTS="-Dfile.encoding=UTF-8 -Xms256m -Xmx512m"

# Expõe a porta padrão do Tomcat
EXPOSE 8080

# Comando para iniciar o Tomcat em primeiro plano
CMD ["catalina.sh", "run"]