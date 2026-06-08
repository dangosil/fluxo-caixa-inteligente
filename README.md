# CashFlow ERP — MVP de Fluxo de Caixa

Sistema de fluxo de caixa empresarial focado inicialmente em uma empresa cliente.

## Escopo inicial

A primeira versão deve permitir:

- cadastro de entradas;
- cadastro de saídas;
- categorias;
- resumo diário;
- resumo mensal;
- lucro estimado diário e mensal;
- dashboard simples.

## Regra principal

```txt
Lucro estimado = Total de entradas - Total de saídas
```

## Stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Flyway
- JPA
- Bean Validation
- JUnit 5
- Mockito
- Testcontainers
- IntelliJ IDEA

## Estrutura planejada

```txt
cashflow-erp/
├── apps/
│   └── api/
├── docs/
│   ├── requisitos-iniciais.md
│   └── apresentacao-funcionalidades.md
├── docker-compose.yml
├── README.md
└── .gitignore
```

## Como executar localmente

Subir banco:

```bash
docker compose up -d
```

Rodar API:

```bash
cd apps/api
./mvnw spring-boot:run
```

Rodar testes:

```bash
cd apps/api
./mvnw test
```

Verificação completa:

```bash
cd apps/api
./mvnw clean verify
```

## Documentos importantes

Antes de implementar, leia:

1. `docs/requisitos-iniciais.md`
2. `docs/apresentacao-funcionalidades.md`
