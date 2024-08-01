# API de Gerenciamento de Livros

Este projeto é uma API simples de Gerenciamento de Livros construída com Spring Boot. Ela fornece endpoints para criar, ler, atualizar e excluir livros. O projeto também inclui testes unitários utilizando JUnit 5 e Mockito.
O objetivo do projeto é testar minhas habilidades com a ferramenta de testes JUnit. 

## Funcionalidades
  
- Criar um novo registro de livro
- Recuperar um livro por ID
- Atualizar um livro por ID
- Excluir um livro por ID
- Testes unitários para as camadas de controlador

## Tecnologias Utilizadas

- Java 11
- Spring Boot
- Spring Data JPA
- JUnit 5
- Mockito

## Primeiros Passos

### Pré-requisitos

- Java 11
- Maven

### Instalação

1. Clone o repositório:
    ```bash
    git clone https://github.com/seuusuario/api-gerenciamento-livros.git
    cd api-gerenciamento-livros
    ```

2. Construa o projeto:
    ```bash
    mvn clean install
    ```

3. Execute a aplicação:
    ```bash
    mvn spring-boot:run
    ```

4. A API estará disponível em `http://localhost:8080`.

## Executando os Testes

Para executar os testes unitários, use o seguinte comando:
```bash
mvn test
