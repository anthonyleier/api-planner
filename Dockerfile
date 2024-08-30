# Imagem oficial para OpenJDK pela Eclipse Temurin
FROM eclipse-temurin:21-jdk-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia a compilação JAR para dentro do container
COPY target/*.jar app.jar

# Indica que a porta HTTP deve ser exposta
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

# Sugestão de comando para criar a imagem
# docker build --tag api-planner-image .

# Sugestão de comando para criar o container
# docker run -p 8080:8080 --name api-planner-container -d api-planner-image

# Sugestão de comando para acessar o container
# docker exec -it api-planner-container /bin/sh
