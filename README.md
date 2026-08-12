# 🍬 Docinhos API

A **Docinhos API** é uma API REST desenvolvida em **Spring Boot** para gerenciamento do catálogo digital de uma pequena empreendedora do ramo de confeitaria.

A aplicação será responsável por fornecer os dados utilizados pela vitrine pública e pela área administrativa do sistema, permitindo o gerenciamento de produtos, categorias, porções, preços, disponibilidade e produtos em destaque.

O projeto tem como objetivo centralizar as informações do negócio em uma plataforma simples e organizada, facilitando a divulgação dos produtos e permitindo que os clientes consultem o catálogo antes de iniciar uma encomenda pelo WhatsApp.


<br>

## Principais funcionalidades

### Área administrativa

- Autenticação da administradora
- Cadastro de produtos
- Listagem de produtos
- Consulta detalhada de produtos
- Edição de produtos
- Ativação e inativação de produtos
- Exclusão de produtos
- Gerenciamento de produtos em destaque

### Vitrine pública

- Consulta dos produtos disponíveis
- Consulta dos detalhes de um produto
- Identificação de produtos temporariamente indisponíveis
- Consulta dos produtos em destaque

<br>

## Tecnologias utilizadas

- **Backend:** Java 21, Spring Boot e Spring Data JPA
- **Banco de dados:** PostgreSQL
- **Mapeamento:** MapStruct
- **Testes e qualidade:** JUnit, JaCoCo, Checkstyle e Spotless
- **Infraestrutura:** Docker
- **Build:** Maven

<br>

## Como rodar o projeto

### Pré-requisitos

Antes de começar, você precisará ter instalado:

- **Git**
- **Java 21**
- **Docker**
- **Docker Compose**

> O projeto possui o Maven Wrapper, portanto não é necessário instalar o Maven globalmente.

### 1. Clone o repositório

```bash
git clone https://github.com/docinhos-da-rosane/docinhos-api.git

cd docinhos-api
```

### 2. Suba o banco de dados

```bash
docker compose up -d
```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

<br>

## Testes

Para executar os testes:

```bash
./mvnw test
```

<br>

## Cobertura de código

O projeto utiliza **JaCoCo** para análise da cobertura dos testes.

Para executar as verificações e gerar o relatório:

```bash
./mvnw clean verify
```

O relatório será gerado em:

```text
target/site/jacoco/index.html
```

Abra o arquivo `index.html` no navegador para visualizar os detalhes da cobertura.

> O projeto estabelece uma cobertura mínima de **80%**.

<br>

## Padronização de código

O projeto utiliza **Checkstyle** e **Spotless** para manter o código consistente e padronizado.

### Verificar os padrões de código

```bash
./mvnw checkstyle:check
```

### Verificar a formatação

```bash
./mvnw spotless:check
```

### Corrigir a formatação automaticamente

```bash
./mvnw spotless:apply
```

<br>

## Docker

O projeto utiliza **Docker** para facilitar a configuração do ambiente de desenvolvimento e garantir maior consistência entre os ambientes.

Para iniciar os serviços:

```bash
docker compose up -d
```

Para visualizar os containers em execução:

```bash
docker compose ps
```

Para encerrar os serviços:

```bash
docker compose down
```

<br>


## Frontend

A API será consumida por uma aplicação web desenvolvida separadamente utilizando **React**.

O frontend será responsável pela vitrine pública e pela interface utilizada pela administradora para gerenciar o catálogo.

> 🔗 Repositório do frontend: 🚧 **Em desenvolvimento**.

<br>


<hr>

<p align="center">
  Desenvolvido com ☕ e 🍬 por
  <a href="https://br.linkedin.com/in/rachel-pizane">
    Rachel Pizane Maia
  </a>
</p>
