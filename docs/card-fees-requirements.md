# Requisitos - Taxas de Maquininha em Entradas

## 1. Problema de Negócio

Vendas no cartão nem sempre geram para a empresa o mesmo valor informado na venda. Dependendo da forma de pagamento, da bandeira, das parcelas e da política comercial, a taxa da maquininha pode ser absorvida pela empresa ou repassada ao cliente.

Para que o fluxo de caixa fique mais próximo do valor realmente recebido, as entradas pagas no cartão precisam registrar:

- valor base da venda;
- valor pago pelo cliente;
- taxa da maquininha;
- valor recebido pela empresa;
- data prevista de recebimento;
- dados básicos da operação no cartão.

## 2. Datas da Entrada

`entryDate` representa a data da venda ou lançamento.

`expectedReceiptDate` representa a data prevista em que o dinheiro entra no caixa. Resumos financeiros e dashboard usam `expectedReceiptDate` para entradas.

Quando `expectedReceiptDate` não for informado, o backend usa `entryDate` como data prevista de recebimento.

## 3. Exemplo de Venda no Cartão

Uma venda tem valor base de R$ 100,00. A taxa manual da maquininha é R$ 3,00.

Se a empresa absorver a taxa:

- valor base da venda: R$ 100,00;
- taxa da maquininha: R$ 3,00;
- valor pago pelo cliente: R$ 100,00;
- valor recebido pela empresa: R$ 97,00.

Se a taxa for repassada ao cliente:

- valor base da venda: R$ 100,00;
- taxa da maquininha: R$ 3,00;
- valor pago pelo cliente: R$ 103,00;
- valor recebido pela empresa: R$ 100,00.

## 4. Conceitos

### Data da Venda

Data em que a venda aconteceu ou em que a entrada foi lançada. No backend, é representada por `entryDate`.

### Data Prevista de Recebimento

Data estimada em que o dinheiro entra no caixa. No backend, é representada por `expectedReceiptDate`.

### Valor Base da Venda

Valor original da venda antes de considerar a taxa da maquininha. No backend, é representado por `amount`.

### Taxa da Maquininha

Valor informado manualmente para representar o custo da transação no cartão. No backend, é representado por `feeAmount`.

### Taxa Absorvida pela Empresa

Situação em que o cliente paga o valor base da venda e a empresa recebe o valor base descontado da taxa. No backend, é representada por `feePayer = MERCHANT`.

### Taxa Repassada ao Cliente

Situação em que o cliente paga o valor base da venda acrescido da taxa e a empresa recebe o valor base da venda. No backend, é representada por `feePayer = CUSTOMER`.

### Valor Pago pelo Cliente

Valor final cobrado do cliente na operação de cartão.

### Valor Recebido pela Empresa

Valor que entra no fluxo de caixa da empresa após considerar a taxa.

### Parcelas

Quantidade de parcelas da venda no cartão. `installmentCount` representa a quantidade de parcelas.

`installmentAmount` é informativo ou aproximado e não altera o valor final da venda.

### Bandeira

Bandeira do cartão usada na venda, como Visa, Mastercard, Elo ou outra.

## 5. Regras da Primeira Versão

- A taxa é informada manualmente.
- Taxa de maquininha se aplica somente a cartão de crédito e cartão de débito.
- PIX, dinheiro, transferência e outros métodos não exibem taxa de maquininha no frontend.
- Parcela é informativa e não altera o valor final da venda.
- Resumos e dashboard usam `expectedReceiptDate` para entradas.

## 6. Regras de Cálculo

Quando `feePayer = MERCHANT`:

```txt
customerPaidAmount = amount
receivedAmount = amount - feeAmount
```

Quando `feePayer = CUSTOMER`:

```txt
customerPaidAmount = amount + feeAmount
receivedAmount = amount
```

## 7. Validações

- `feeAmount` não pode ser negativo.
- `feeAmount` não pode ser maior que `amount` quando a taxa for absorvida pela empresa.
- `installmentCount` deve ser maior ou igual a 1.
- `installmentAmount` deve ser opcional e informativo.
- `receivedAmount` não pode ser negativo.

## 8. Impacto nos Resumos

- `totalIncome` deve considerar `receivedAmount`.
- Resumos e dashboard devem considerar entradas pela data `expectedReceiptDate`.
- `estimatedProfit` continua sendo calculado como `totalIncome - totalExpense`.

Assim, o lucro estimado continua representando o resultado estimado do fluxo de caixa registrado, não lucro contábil formal.

## 9. Fora do Escopo

- Cálculo automático de taxa por bandeira ou operadora.
- Cálculo automático de dias úteis.
- Geração automática de parcelas futuras.
- Agenda de recebíveis.
- Conciliação bancária.
- Cadastro de maquininha.
