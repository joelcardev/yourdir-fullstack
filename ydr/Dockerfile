# Use uma imagem do Node.js como base
FROM node:18-alpine

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo package.json e instale as dependências
COPY package*.json ./
RUN npm install

# Copie todo o código do projeto
COPY . .

# Comando para rodar o app
CMD ["npm", "start"]

# Exponha a porta
EXPOSE 3000
