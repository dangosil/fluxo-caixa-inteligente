# Requisitos do Frontend MVP

## Objetivo do Frontend

Criar uma interface web simples, responsiva e objetiva para operar o MVP de fluxo de caixa.

O frontend deve consumir a API Spring Boot existente e permitir que o usuário visualize rapidamente:

- entradas;
- saídas;
- categorias;
- lucro estimado diário;
- lucro estimado mensal;
- dashboard simples.

O MVP atual é base única/local. Não haverá login, multiempresa ou funcionalidades de ERP completo nesta etapa.

## Stack Proposta

- React
- TypeScript
- Vite
- React Router
- TanStack Query
- React Hook Form
- Zod
- Axios ou Fetch API
- CSS simples, Tailwind CSS ou biblioteca de componentes leve

## Telas do MVP

### Dashboard

- Exibir resumo do dia.
- Exibir resumo do mês.
- Mostrar total de entradas.
- Mostrar total de saídas.
- Mostrar lucro estimado.
- Permitir escolher a data base do dashboard.

### Categorias

- Listar categorias.
- Criar categoria.
- Editar categoria.
- Desativar categoria.
- Filtrar por tipo: `INCOME` ou `EXPENSE`.
- Filtrar por status ativo/inativo.

### Entradas

- Listar entradas.
- Criar entrada.
- Editar entrada.
- Desativar entrada.
- Filtrar por período.
- Filtrar por categoria.
- Filtrar por forma de recebimento.

### Saídas

- Listar saídas.
- Criar saída.
- Editar saída.
- Desativar saída.
- Filtrar por período.
- Filtrar por categoria.
- Filtrar por forma de pagamento.

### Resumos

- Consultar resumo diário.
- Consultar resumo mensal.
- Exibir totais e lucro estimado.

## Requisitos Funcionais

- Consumir os endpoints atuais da API.
- Criar, editar, listar e desativar categorias.
- Criar, editar, listar e desativar entradas.
- Criar, editar, listar e desativar saídas.
- Consultar dashboard por data.
- Consultar resumo diário por data.
- Consultar resumo mensal por ano e mês.
- Validar formulários antes do envio.
- Exibir mensagens de erro retornadas pela API.
- Usar valores monetários formatados em reais na interface.
- Usar datas no formato adequado para o usuário e enviar `YYYY-MM-DD` para a API.

## Requisitos Não Funcionais

- Interface responsiva para desktop e mobile.
- Carregamento rápido e navegação simples.
- Código organizado por domínio.
- Componentes reutilizáveis para formulários, tabelas e estados de carregamento.
- Tratamento claro de loading, erro e lista vazia.
- Não armazenar regra financeira no frontend como fonte da verdade.
- Não expor dados sensíveis.
- Manter o frontend desacoplado de detalhes internos do backend.

## Estrutura de Pastas Sugerida

```txt
apps/web/
├── src/
│   ├── app/
│   │   ├── App.tsx
│   │   └── routes.tsx
│   ├── shared/
│   │   ├── api/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── utils/
│   │   └── types/
│   ├── features/
│   │   ├── dashboard/
│   │   ├── categories/
│   │   ├── cash-entries/
│   │   ├── cash-expenses/
│   │   └── cash-summary/
│   ├── main.tsx
│   └── styles.css
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

## Ordem de Implementação

1. Criar projeto React + TypeScript + Vite.
2. Configurar cliente HTTP da API.
3. Criar layout base responsivo.
4. Criar dashboard simples.
5. Criar listagem e formulário de categorias.
6. Criar listagem e formulário de entradas.
7. Criar listagem e formulário de saídas.
8. Criar telas de resumo diário e mensal.
9. Adicionar estados de loading, erro e vazio.
10. Revisar responsividade.
11. Revisar mensagens e formatação monetária/data.

## Endpoints Consumidos

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

### Cash Entries

```txt
GET    /cash-entries
GET    /cash-entries/{id}
POST   /cash-entries
PUT    /cash-entries/{id}
DELETE /cash-entries/{id}
```

### Cash Expenses

```txt
GET    /cash-expenses
GET    /cash-expenses/{id}
POST   /cash-expenses
PUT    /cash-expenses/{id}
DELETE /cash-expenses/{id}
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

## Fora do Escopo

- Login e autenticação.
- Usuários e permissões.
- Cadastro de empresa.
- Multiempresa.
- Estoque.
- Fiscal.
- Integrações bancárias.
- Gráficos avançados.
- Relatórios contábeis.
- Exportação PDF/Excel.
- ERP completo.
- App mobile nativo.
