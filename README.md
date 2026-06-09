# Fluxo de Caixa Inteligente - Full-stack MVP

Aplicacao web para controle simples de fluxo de caixa, com backend Java/Spring Boot e frontend React/TypeScript/Vite.

O MVP atual trabalha em **base unica/local**, sem cadastro de empresa, sem `companyId`, sem login/autenticacao e sem multiempresa. O foco e registrar categorias, entradas, saidas e calcular lucro estimado diario/mensal.

## Escopo Atual

- Categorias de entrada e saida.
- Entradas de caixa.
- Saidas de caixa.
- Listagem de lancamentos por filtros simples.
- Resumo diario.
- Resumo mensal.
- Dashboard simples de resumo financeiro.
- Interface web para operar o MVP.

Fora do MVP atual:

- login/autenticacao;
- multiempresa;
- `companyId`;
- estoque;
- fiscal;
- integracoes;
- funcionalidades de ERP completo.

## Stack

### Backend

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

### Frontend

- React
- TypeScript
- Vite
- Tailwind CSS
- React Router
- TanStack Query
- Axios
- React Hook Form
- Zod
- Lucide React

## Estrutura

```txt
cashflow-erp/
├── apps/
│   ├── cashflow-api/
│   └── cashflow-web/
├── docs/
├── docker-compose.yml
└── README.md
```

## Pre-requisitos

- Java 21
- Maven Wrapper
- Node.js
- npm
- Docker
- Docker Compose

## Banco Local

Subir PostgreSQL com Docker Compose:

```powershell
docker compose up -d
```

As credenciais em `docker-compose.yml` e `apps/cashflow-api/src/main/resources/application.yaml` sao apenas para desenvolvimento local.

## Rodar Backend

```powershell
cd apps/cashflow-api
.\mvnw.cmd spring-boot:run
```

A API roda localmente em:

```txt
http://localhost:8080
```

## Rodar Testes do Backend

```powershell
cd apps/cashflow-api
.\mvnw.cmd test
```

## Rodar Frontend

```powershell
cd apps/cashflow-web
npm install
npm run dev
```

O Vite serve o frontend localmente e encaminha chamadas `/api` para `http://localhost:8080`.

## Rodar Build do Frontend

```powershell
cd apps/cashflow-web
npm run build
```

## Migrations Atuais

- `V1__create_categories_table.sql`
- `V2__create_cash_entries_table.sql`
- `V3__create_cash_expenses_table.sql`

Nao existe tabela para resumo ou dashboard. Esses dados sao calculados a partir dos lancamentos ativos.

## Endpoints Atuais

Exemplos de uso com `curl` estao em [`docs/api-examples.md`](docs/api-examples.md).

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

Filtros disponiveis:

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

Filtros disponiveis:

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

Filtros disponiveis:

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

Quando `date` nao e informado, o backend usa a data atual.

## Frontend

O frontend em `apps/cashflow-web` consome a API Spring Boot usando o proxy `/api` configurado no Vite. A interface atual possui dashboard, listagem, filtros e cadastro de categorias, entradas e saidas.

Requisitos do frontend: [`docs/frontend-requirements.md`](docs/frontend-requirements.md).

## Regra Financeira

```txt
estimatedProfit = totalIncome - totalExpense
```

O valor representa lucro estimado do fluxo de caixa registrado. Nao e lucro contabil formal.

## Seguranca de Dados

Este repositorio nao deve conter dados reais, segredos, chaves privadas ou credenciais de producao.
