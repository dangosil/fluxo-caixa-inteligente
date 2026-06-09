# Requisitos - Taxas de Maquininha em Entradas

## 1. Problema de Negocio

Vendas no cartao nem sempre geram para a empresa o mesmo valor informado na venda. Dependendo da forma de pagamento, da bandeira, das parcelas e da politica comercial, a taxa da maquininha pode ser absorvida pela empresa ou repassada ao cliente.

Para que o fluxo de caixa fique mais proximo do valor realmente recebido, as entradas pagas no cartao precisam registrar:

- valor base da venda;
- valor pago pelo cliente;
- taxa da maquininha;
- valor recebido pela empresa;
- prazo previsto de recebimento;
- dados basicos da operacao no cartao.

## 2. Exemplo Realista de Venda no Cartao

Uma venda tem valor base de R$ 100,00. A taxa informada da maquininha e R$ 3,00.

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

## 3. Conceitos

### Valor Base da Venda

Valor original da venda antes de considerar a taxa da maquininha.

### Taxa da Maquininha

Valor cobrado pela operadora ou informado manualmente para representar o custo da transacao no cartao.

### Taxa Absorvida pela Empresa

Situacao em que o cliente paga o valor base da venda e a empresa recebe o valor base descontado da taxa.

### Taxa Repassada ao Cliente

Situacao em que o cliente paga o valor base da venda acrescido da taxa e a empresa recebe o valor base da venda.

### Valor Pago pelo Cliente

Valor final cobrado do cliente na operacao de cartao.

### Valor Recebido pela Empresa

Valor que entra no fluxo de caixa da empresa apos considerar a taxa.

### Parcelas

Quantidade de parcelas da venda no cartao. Na primeira versao, esse dado e apenas informativo para a entrada registrada.

### Bandeira

Bandeira do cartao usada na venda, como Visa, Mastercard, Elo ou outra.

### Prazo de Recebimento

Data ou quantidade de dias prevista para a empresa receber o valor da operacao.

## 4. Regras da Primeira Versao

- A taxa sera informada manualmente.
- Nao havera calculo automatico por operadora.
- Nao havera cadastro de maquininha.
- Nao havera conciliacao bancaria.
- Nao havera geracao automatica de recebiveis.
- Nao havera agenda de parcelas.

## 5. Regras de Calculo

Quando a taxa for absorvida pela empresa:

```txt
customerPaidAmount = amount
receivedAmount = amount - feeAmount
```

Quando a taxa for repassada ao cliente:

```txt
customerPaidAmount = amount + feeAmount
receivedAmount = amount
```

## 6. Validacoes

- `feeAmount` nao pode ser negativo.
- `feeAmount` nao pode ser maior que `amount` quando a taxa for absorvida pela empresa.
- `installmentCount` deve ser maior ou igual a 1.
- `installmentAmount` deve ser opcional ou calculado no frontend/backend.
- `receivedAmount` nao pode ser negativo.

## 7. Impacto nos Resumos

- `totalIncome` deve considerar `receivedAmount`.
- `estimatedProfit` continua sendo calculado como `totalIncome - totalExpense`.

Assim, o lucro estimado continua representando o resultado estimado do fluxo de caixa registrado, nao lucro contabil formal.

## 8. Fora do Escopo

- Calculo automatico de taxa por bandeira.
- Multiplas maquininhas.
- Conciliacao bancaria.
- Agenda de recebiveis.
- Geracao automatica de parcelas futuras.
