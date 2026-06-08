# Exemplos de Uso da API

Exemplos fictícios usando `curl`.

Base URL local:

```txt
http://localhost:8080
```

## 1. Health Check

```bash
curl -X GET "http://localhost:8080/health"
```

## 2. Criar Categoria INCOME

```bash
curl -X POST "http://localhost:8080/categories" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Vendas",
    "type": "INCOME"
  }'
```

## 3. Criar Categoria EXPENSE

```bash
curl -X POST "http://localhost:8080/categories" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aluguel",
    "type": "EXPENSE"
  }'
```

## 4. Listar Categorias

```bash
curl -X GET "http://localhost:8080/categories"
```

Filtrar categorias ativas de entrada:

```bash
curl -X GET "http://localhost:8080/categories?type=INCOME&active=true"
```

Filtrar categorias ativas de saída:

```bash
curl -X GET "http://localhost:8080/categories?type=EXPENSE&active=true"
```

## 5. Criar Entrada

Use uma categoria do tipo `INCOME`.

```bash
curl -X POST "http://localhost:8080/cash-entries" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Venda no balcão",
    "amount": 1500.00,
    "entryDate": "2026-06-08",
    "categoryId": "COLE_O_UUID_AQUI",
    "paymentMethod": "PIX",
    "notes": "Exemplo fictício"
  }'
```

## 6. Criar Saída

Use uma categoria do tipo `EXPENSE`.

```bash
curl -X POST "http://localhost:8080/cash-expenses" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Pagamento de aluguel",
    "amount": 1200.00,
    "expenseDate": "2026-06-08",
    "categoryId": "COLE_O_UUID_AQUI",
    "paymentMethod": "BANK_TRANSFER",
    "notes": "Exemplo fictício"
  }'
```

## 7. Consultar Resumo Diário

```bash
curl -X GET "http://localhost:8080/cash-summary/daily?date=2026-06-08"
```

## 8. Consultar Resumo Mensal

```bash
curl -X GET "http://localhost:8080/cash-summary/monthly?year=2026&month=6"
```

## 9. Consultar Dashboard

Com data informada:

```bash
curl -X GET "http://localhost:8080/dashboard/summary?date=2026-06-08"
```

Sem data informada:

```bash
curl -X GET "http://localhost:8080/dashboard/summary"
```
