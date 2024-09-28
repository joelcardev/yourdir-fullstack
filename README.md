# yourdir-fullstack

Yourdir.com é uma plataforma de gerenciamento e organização de arquivos que permite que os usuários criem, gerenciem e organizem diretórios e arquivos de forma intuitiva. Esta aplicação é uma solução robusta para as necessidades de gerenciamento de arquivos, seja para uso pessoal ou profissional.

## Funcionalidades

- Criação e gerenciamento de diretórios
- Upload e organização de arquivos de texto
- Interface de usuário intuitiva

## Tecnologias Utilizadas

- **Frontend**: React, Vite, TypeScript
- **Backend**: Spring Boot
- **Banco de Dados**: PostgreSQL
- **Controle de Versão**: Git e GitHub
- **Cache**: Redis

## Pré-requisitos

Antes de executar o projeto, verifique se você possui os seguintes coisas:

- docker instalado.
- Se rodar sem docker instale redis.

## Rodar projeto

- edite no projeto ydr o .env.exemple para .env
- docker compose build --no-cache; docker compose up

# Notas sobre o projeto

- É um projeto que cria diretorios e subdiretorios dentro de outros, e sendo possivel colocar files de texto em cada diretorio e subdiretorio.
- Existem melhorias, como aplicação para varios usuarios, redux para gerenciar estados da aplicação.
- É usado Redis como forma de melhorar a resposta da api, pois, como pode ser criados muitos diretorios e cada um contando subs e arquivos de texto, usar redis para pegar esses dados de uma vez é super essencial e escalavel.



