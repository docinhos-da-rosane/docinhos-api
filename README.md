
# 🍬 Docinhos API

A **Docinhos API** é uma API REST desenvolvida em **Spring Boot** para gerenciamento do catálogo digital de uma pequena empreendedora do ramo de confeitaria.

A aplicação será responsável por fornecer os dados utilizados pela vitrine pública e pela área administrativa do sistema, permitindo o gerenciamento de produtos, categorias, porções, preços, disponibilidade e produtos em destaque.

O projeto tem como objetivo centralizar as informações do negócio em uma plataforma simples e organizada, facilitando a divulgação dos produtos e permitindo que os clientes consultem o catálogo antes de iniciar uma encomenda pelo WhatsApp.


<br>

🔗 **Aplicação publicada:** [Docinhos API Swagger](https://docinhos-api.onrender.com/swagger-ui/index.html)

*⚠️ A aplicação pode levar alguns segundos para responder na primeira requisição devido ao cold start da plataforma.*

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

## Frontend

A API será consumida por uma aplicação web desenvolvida separadamente utilizando **React**.

O frontend será responsável pela vitrine pública e pela interface utilizada pela administradora para gerenciar o catálogo.

> 🔗 Repositório do frontend: [Docinhos App](https://github.com/docinhos-da-rosane/docinhos-app).

<br>

## Tecnologias utilizadas

- **Backend:** Java 21, Spring Boot e Spring Data JPA
- **Banco de dados:** PostgreSQL
- **Segurança:** Spring Security e JWT
- **Documentação:** Swagger / OpenAPI
- **Mapeamento:** MapStruct
- **Upload e armazenamento de imagens:** Cloudinary
- **Testes e qualidade:** JUnit, JaCoCo, Checkstyle e Spotless
- **Infraestrutura:** Docker e Docker Compose
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


### 2. Configure as variáveis de ambiente
O projeto utiliza um arquivo `.env` na raiz do repositório para guardar as variáveis de ambiente utilizadas pela aplicação e pelo Docker Compose.

Crie um arquivo chamado `.env` na pasta raiz do projeto com o seguinte conteúdo:

```bash
WEB_API=http://localhost:5173

JWT_SECRET=sua_chave_secreta_jwt
JWT_EXPIRATION=3600000

CLOUDINARY_CLOUD_NAME=seu_cloud_name
CLOUDINARY_API_KEY=sua_api_key
CLOUDINARY_API_SECRET=sua_api_secret

IMAGEM_PATH=docinhos/dev/produtos
```

> **Importante**: o arquivo `.env` não deve ser commitado em repositórios públicos, pois contém segredos e credenciais.

### 3. Suba o banco de dados

```bash
docker compose up -d postgres
```

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

<br>

## Armazenamento de imagens com Cloudinary
A API utiliza o serviço Cloudinary para armazenar e gerenciar imagens dos produtos.
Esse armazenamento é configurado por meio das variáveis:
- `CLOUDINARY_CLOUD_NAME`
- `CLOUDINARY_API_KEY`
- `CLOUDINARY_API_SECRET`

Além disso, a pasta de armazenamento é definida em:
- `IMAGEM_PATH`

Essas configurações permitem que a aplicação envie imagens para o Cloudinary, obtenha a URL pública e também faça exclusão quando necessário.

<br>

## Documentação da API

A API utiliza Swagger / OpenAPI para disponibilizar uma documentação interativa dos endpoints.

Com a aplicação em execução, a documentação pode ser acessada em:

> [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

O Swagger permite consultar os endpoints disponíveis, visualizar os contratos de requisição e resposta e realizar chamadas diretamente pela interface.

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

<hr>

<p align="center">
  Desenvolvido com ☕ e 🍬 por
  <a href="https://br.linkedin.com/in/rachel-pizane">
    Rachel Pizane Maia
  </a>
</p>
