# Desafio de Gerenciamento de Tarefas com Microserviços

Este repositório contém a implementação de um sistema de gerenciamento de tarefas. A aplicação é construída em uma arquitetura de microserviços e inclui um frontend para interação do usuário.

## Tecnologias Utilizadas

- **Backend:** Java 21, Spring Boot 3
- **Frontend:** Angular 19
- **Banco de Dados:** PostgreSQL
- **Containerização:** Docker & Docker Compose
- **Testes:**
    - Backend: JUnit 5, Mockito
    - Frontend: Jasmine (Unit), Cypress (E2E)
- **Documentação da API:** SpringDoc (Swagger)

## Estrutura do Projeto

O projeto está organizado em três pastas principais na raiz do repositório:
- '/userservice': Microserviço Spring Boot para gerenciamento de usuários.
- '/taskservice': Microserviço Spring Boot para gerenciamento de tarefas.
- '/frontend': Aplicação Angular que consome as APIs dos microserviços.


## Como Executar a Aplicação (Docker)

Esta é a forma recomendada e mais simples de executar toda a aplicação.

1.  **Clone o repositório:**
```bash
    git clone https://github.com/marcelo-mlls/cadastro-tarefas
```

2.  **Construa e suba os contêineres:**
    Na pasta raiz do projeto, execute o comando abaixo. Ele irá construir as imagens para os três serviços e iniciar todos os contêineres.
    ```bash
    docker compose up --build
    ```

    Aguarde até que todos os logs de inicialização terminem.

3.  **Acesse a aplicação:**
    - **Frontend:** Abra seu navegador e acesse http://localhost:4200
    - **API do Serviço de Usuários (Swagger):** http://localhost:8080/swagger-ui.html
    - **API do Serviço de Tarefas (Swagger):**  http://localhost:8081/swagger-ui.html

## Como Executar os Testes

### Testes de Backend

Para rodar os testes de integração de cada microserviço, navegue até a pasta do serviço e use o Maven.

**Para o userservice:**
```bash
cd userservice 
mvn test
```

**Para o taskservice:**
```bash
cd taskservice
mvn test
```

### Testes de Frontend
Os testes de frontend requerem a instalação das dependências do Node.js.

Instale as dependências:
```bash
cd frontend
npm install
```
Rode os testes unitários (Jasmine/Karma):
```bash
ng test
```

Rode os testes End-to-End (Cypress): Primeiro, garanta que a aplicação completa esteja rodando (com docker-compose up). Depois, execute:
```bash
npx cypress open
```

Isso abrirá o Cypress Test Runner, onde você poderá clicar nos arquivos de teste para executá-los em um navegador.