# Fluxo de Caixa Inteligente — Backend MVP

Backend Java/Spring Boot para controle simples de fluxo de caixa.

O MVP atual trabalha em **base única/local**, sem cadastro de empresa, sem `companyId`, sem autenticação e sem multiempresa. O foco é registrar categorias, entradas, saídas e calcular lucro estimado diário/mensal.

## Escopo Atual

- Categorias de entrada e saída.
- Entradas de caixa.
- Saídas de caixa.
- Listagem de lançamentos por filtros simples.
- Resumo diário.
- Resumo mensal.
- Dashboard simples de resumo financeiro.

Fora do MVP atual:

- login/autenticação;
- frontend;
- estoque;
- fiscal;
- integrações;
- multiempresa;
- funcionalidades de ERP completo.

## Pré-requisitos

- Java 21
- Maven Wrapper
- Docker
- Docker Compose

## Stack

- Java 21
- Spring Boot 3.5.14
- Maven
- PostgreSQL
- Flyway
- Spring Data JPA
- Bean Validation
- JUnit 5
- Mockito
- Testcontainers
- MockMvc

## Estrutura

```txt
cashflow-erp/
├── apps/
│   └── cashflow-api/
├── docs/
├── docker-compose.yml
└── README.md
```

## Banco Local

Subir PostgreSQL com Docker Compose:

```powershell
docker compose up -d
```

As credenciais em `docker-compose.yml` e `apps/cashflow-api/src/main/resources/application.yaml` são apenas para desenvolvimento local.

## Rodar API

```powershell
cd apps/cashflow-api
.\mvnw.cmd spring-boot:run
```

## Rodar Testes

```powershell
cd apps/cashflow-api
.\mvnw.cmd test
```

## Migrations Atuais

- `V1__create_categories_table.sql`
- `V2__create_cash_entries_table.sql`
- `V3__create_cash_expenses_table.sql`

Não existe tabela para resumo ou dashboard. Esses dados são calculados a partir dos lançamentos ativos.

## Endpoints Atuais

Exemplos de uso com `curl` estão em [`docs/api-examples.md`](docs/api-examples.md).

### Health

```txt
GET /health
```

### Categories

```txt
GET    /categories
GET    /categories/{id}
POST   /categories
PUT    /categories/{id}
DELETE /categories/{id}
```

Filtros disponíveis:

```txt
GET /categories?type=INCOME&active=true
GET /categories?type=EXPENSE&active=true
```

### Cash Entries

```txt
GET    /cash-entries
GET    /cash-entries/{id}
POST   /cash-entries
PUT    /cash-entries/{id}
DELETE /cash-entries/{id}
```

Filtros disponíveis:

```txt
GET /cash-entries?startDate=2026-06-01&endDate=2026-06-30
GET /cash-entries?categoryId={uuid}&paymentMethod=PIX&active=true
```

### Cash Expenses

```txt
GET    /cash-expenses
GET    /cash-expenses/{id}
POST   /cash-expenses
PUT    /cash-expenses/{id}
DELETE /cash-expenses/{id}
```

Filtros disponíveis:

```txt
GET /cash-expenses?startDate=2026-06-01&endDate=2026-06-30
GET /cash-expenses?categoryId={uuid}&paymentMethod=PIX&active=true
```

### Cash Summary

```txt
GET /cash-summary/daily?date=YYYY-MM-DD
GET /cash-summary/monthly?year=YYYY&month=MM
```

### Dashboard

```txt
GET /dashboard/summary
GET /dashboard/summary?date=YYYY-MM-DD
```

Quando `date` não é informado, o backend usa a data atual.

## Regra Financeira

```txt
estimatedProfit = totalIncome - totalExpense
```

O valor representa lucro estimado do fluxo de caixa registrado. Não é lucro contábil formal.

## Segurança de Dados

Este repositório não deve conter dados reais de cliente, segredos, chaves privadas ou credenciais de produção.
