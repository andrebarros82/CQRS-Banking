---

# CQRS Banking API

API bancária implementada em **Java 17** com **Spring Boot 3.5.6**, seguindo o padrão **CQRS (Command Query Responsibility Segregation)**, utilizando **JWT** para autenticação, **Redis** para cache de transações e **H2** como banco de dados em memória.

---

## Descrição

Esta aplicação simula operações bancárias de usuários, incluindo:

* Cadastro de usuários
* Depósitos
* Saques
* Pagamento de contas
* Histórico de transações

O sistema é dividido entre **Commands** (operações que alteram estado) e **Queries** (operações de leitura), seguindo o padrão CQRS.

Além disso, a API possui:

* **Autenticação JWT** para segurança dos endpoints
* **Validação de dados** usando `jakarta.validation`
* **Cache de transações em Redis** (incluindo Redis embutido para ambiente local)
* **Documentação Swagger/OpenAPI** para todos os endpoints REST

---

## Tecnologias Utilizadas

* Java 24
* Spring Boot 3.5.6
* Spring Security com JWT
* Spring Data JPA
* H2 Database
* Redis (embutido para dev/local)
* Maven
* Springdoc OpenAPI / Swagger

---

## Estrutura do Projeto

```text
src/main/java
├── command
│   ├── controller       # Endpoints de comandos (deposit, withdraw, payBill, registerUser)
│   ├── dto              # DTOs de requests/responses de comandos
│   └── service          # Lógica de comandos
├── query
│   ├── controller       # Endpoints de consultas (getUser, historico)
│   ├── dto              # DTOs de responses de queries
│   └── service          # Lógica de consultas
├── cache
│   ├── dto              # DTOs de cache (TransactionCacheDTO, HistoricoDTO)
│   └── service          # Serviço de cache em Redis
├── model                # Modelos JPA (UserModel, TransactionModel)
├── repository           # Repositórios Spring Data JPA
├── security
│   ├── jwt              # JwtUtil e filtro de autenticação
│   └── CustomUserDetailsService
├── exception            # Tratamento global de exceções
└── config               # Configurações (Redis, Swagger, Security)
```

---

## Autenticação

A API utiliza **JWT** para autenticação.

Endpoints abertos para registro e login:

* `POST /api/commands/users/register` → Cadastra um usuário
* `POST /api/auth/login` → Retorna JWT válido

Todos os demais endpoints requerem **Bearer Token** no header `Authorization`.

---

## Endpoints Principais

### Commands

| Método | Endpoint                     | Descrição                                      |
| ------ | ---------------------------- | ---------------------------------------------- |
| POST   | /api/commands/users/register | Cadastra um usuário                            |
| POST   | /api/commands/users/deposit  | Deposita valor na conta do usuário autenticado |
| POST   | /api/commands/users/withdraw | Realiza saque da conta do usuário autenticado  |
| POST   | /api/commands/users/payBill  | Paga conta do usuário autenticado              |

### Queries

| Método | Endpoint                        | Descrição                                              |
| ------ | ------------------------------- | ------------------------------------------------------ |
| GET    | /api/queries/users/{id}         | Retorna dados do usuário por ID                        |
| GET    | /api/queries/users/transactions | Retorna histórico de transações do usuário autenticado |

---

## DTOs Importantes

### Command DTOs

* `DepositRequestDTO` / `DepositResponseDTO`
* `WithdrawRequestDTO` / `WithdrawResponseDTO`
* `PayBillRequestDTO` / `PayBillResponseDTO`
* `RegisterUserRequestDTO`

### Query DTOs

* `UserViewResponseDTO`
* `HistoricoDTO` / `HistoricoResponseDTO`

### Cache DTOs

* `TransactionCacheDTO`

---

## Configurações

### Redis

* Configuração local embutida via `EmbeddedRedisConfig`
* RedisTemplate customizado para `TransactionCacheDTO`

### Swagger / OpenAPI

A documentação Swagger está disponível em:

```
/swagger-ui.html
/v3/api-docs
```

### Segurança

* Configuração de `SecurityFilterChain`
* `JwtAuthenticationFilter` para validação de tokens JWT
* `PasswordEncoder` BCrypt para senhas

---

## Observações

* Saldo negativo é permitido em saques e pagamentos de contas
* Juros aplicados automaticamente em caso de saldo negativo antes de depósito
* Histórico de transações limitado a **100 transações mais recentes** em cache Redis
* Validação de inputs usando `@NotNull` e `@Min` para valores monetários

---

## Executando o Projeto

1. Clone o repositório:

```bash
git clone https://github.com/andrebarros82/CQRS-Banking.git
cd cqrs-banking-api
```

2. Execute com Maven:

```bash
mvn spring-boot:run
```

3. Acesse a API:

* Swagger UI: `http://localhost:8080/swagger-ui.html`
* H2 Console: `http://localhost:8080/h2-console`

---

## Referências

* [Spring Boot Documentation](https://spring.io/projects/spring-boot)
* [Spring Security + JWT](https://spring.io/guides/tutorials/spring-security-and-angular-js/)
* [Spring Data Redis](https://spring.io/projects/spring-data-redis)
* [Springdoc OpenAPI](https://springdoc.org/)

---