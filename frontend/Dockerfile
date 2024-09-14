# Dockerfile
FROM node:latest

# Criar diretório da aplicação
WORKDIR /app

# Copiar arquivos da aplicação para o container
COPY ./app /app

# Instalar dependências da aplicação

RUN npm install

# Expor a porta 3030
EXPOSE 3000

# Comando para rodar a aplicação
CMD ["npm", "run", "dev"]
