# Etapa 1: Construir a aplicação
FROM node:20-alpine AS build

# Definir o diretório de trabalho
WORKDIR /app

# Copiar os arquivos de configuração do projeto
COPY package.json package-lock.json ./

# Instalar as dependências
RUN npm ci

# Copiar o código-fonte da aplicação
COPY . .

# Construir a aplicação
RUN npm run build

# Etapa 2: Servir a aplicação
FROM nginx:alpine

# Copiar a configuração personalizada do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar os arquivos construídos para o diretório padrão do Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Expor a porta 80
EXPOSE 80

# Iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]

# Sugestão de comando para criar a imagem
# docker build --tag frontend-planner-image .

# Sugestão de comando para criar o container
# docker run -p 80:80 --name frontend-planner-container -d frontend-planner-image

# Sugestão de comando para acessar o container
# docker exec -it frontend-planner-container /bin/sh
