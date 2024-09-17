# Company Manager

## Sobre o Projeto

Sistema de Gerenciamento de Associações entre Empresas e Fornecedores.

Esta é uma aplicação desenvolvida para facilitar o gerenciamento de associações entre empresas e seus fornecedores. 

A aplicação permite a visualização de empresas e fornecedores com paginação e pesquisa por nome, CNPJ e CEP para empresas, e pesquisa por nome e CNPJ/CPF para fornecedores.

A aplicação permite o cadastro, edição e deleção de empresas e fornecedores, assim como a associação entre as empresas e os fornecedores, 
oferecendo uma interface amigável no front-end e uma API robusta no back-end.

Além disso, a API está documentada com Swagger API para facilitar o compreendimento das rotas disponíveis, seus payloads e retornos. 

Também foi designada uma rota para fornecer o consumo da API dos correios.

Esse foi um projeto desafiador, onde pude ter contato com o framework Vue.js pela primeira vez.

## Modelagem das entidades
![accenture drawio](https://github.com/user-attachments/assets/cf961050-1a82-48c4-a097-8b1cbdc9e09d)

## Tecnologias Utilizadas

### Front-end
- **Framework**: Vue.js
- **Linguagem**: TypeScript
- **Estilos**: Tailwind CSS e Vuetify

### Back-end
- **Linguagem**: Java
- **Framework**: Spring
- **Testes**: JUnit e Mockito

### Banco de Dados
- **Banco**: PostgreSQL

### Documentação da API
- **Ferramenta**: Swagger API

### Containerização
- **Docker** e **Docker Compose**

## Como Rodar o Projeto

Siga os passos abaixo para configurar e executar o projeto em sua máquina local.

### Pré-requisitos

- Git
- Node.js e npm (para o front-end)
- java 21 (JDK) e Maven
- Docker e Docker Compose

### Passos

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/Marcus-Nastasi/acc-company-manager
   
2. **Importar dependencias do front-end:**
   ```bash
   cd frontend/app
   npm install

3. **Build do Maven no Back-end:**
   ```bash
   cd backend/test
   mvn clean install -DskipTests

4. **Executar a aplicação com Docker: Certifique-se de estar na raiz do projeto e rode o Docker Compose para subir todos os serviços de forma automatizada:**
   ```bash
    [sudo] docker-compose up --build

5. **Aguardar o build ser finalizado e acessar a aplicação: Após o build ser concluído, a aplicação estará disponível no navegador:**
   ```bash
    http://localhost:3000/

6. **Você pode acessar a documentação da API feita com o Swagger pela rota:**
   ```bash
    http://localhost:8080/swagger-ui/index.html


## Testes unitários

### Execução e Build sem pular os testes unitários
- **Para realizar a execução dos testes, ou o build (com o comando "mvn clean install", removendo o "-DskipTests" do exemplo acima), você deve ter em execução um banco de dados postgresql em seu localhost, expondo a porta 5432. Caso tenha um banco de dados postgres fora da máquina local ou em outra porta, será necessário alterar a url do banco no arquivo "application.properties". Apesar das chamadas para o banco nos testes estarem mockadas, o Spring testará a conexão com o banco de dados.** 
