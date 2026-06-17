# Modelo de Domínio

## Bounded Context Atual

O bounded context atual é **Fluxo de Caixa**.

## Objetivo do Domínio

O domínio existe para:

- registrar entradas, saídas e categorias;
- calcular valores recebidos;
- calcular resumo financeiro;
- apoiar decisões simples de caixa.

## Linguagem Ubíqua

- Categoria: classificação usada para organizar entradas e saídas.
- Entrada de caixa: lançamento financeiro que representa valor a receber ou recebido.
- Saída de caixa: lançamento financeiro que representa valor pago ou a pagar.
- Data da venda: data em que a entrada foi registrada ou a venda aconteceu.
- Data prevista de recebimento: data estimada em que o valor entra no caixa.
- Valor base: valor original da venda antes de taxas.
- Taxa da maquininha: valor cobrado pela operação no cartão.
- Taxa absorvida pela empresa: situação em que a empresa assume a taxa e recebe o valor base descontado.
- Taxa repassada ao cliente: situação em que o cliente paga o valor base acrescido da taxa.
- Valor pago pelo cliente: valor final cobrado do cliente.
- Valor recebido pela empresa: valor que entra no fluxo de caixa após considerar a taxa.
- Parcela informativa: informação sobre parcelamento que não altera o valor final da venda.
- Resumo diário: consolidação financeira de um dia.
- Resumo mensal: consolidação financeira de um mês.
- Lucro estimado: resultado financeiro estimado calculado a partir de entradas e saídas.

## Entidades Principais Atuais

- `Category`
- `CashEntry`
- `CashExpense`

## Regras de Domínio Importantes

- Entrada usa categoria `INCOME`.
- Saída usa categoria `EXPENSE`.
- Valores monetários devem ser positivos.
- Taxa de maquininha só se aplica a cartão de crédito e cartão de débito.
- PIX, dinheiro, transferência e outros métodos não usam taxa de maquininha no frontend.
- Parcela é informativa e não altera o valor final da venda.
- Resumos de entrada usam `expectedReceiptDate`.
- `totalIncome` considera `receivedAmount`.
- `estimatedProfit = totalIncome - totalExpense`.

## Diretrizes DDD

- Controllers não devem conter regra de negócio.
- Services devem orquestrar casos de uso.
- Regras financeiras devem ficar em métodos de domínio ou componentes de domínio.
- Nomes devem seguir a linguagem ubíqua.
- Evitar termos técnicos que escondam a regra do negócio.
- Evoluir o modelo de forma incremental, sem reescrever o projeto inteiro.

## Diretrizes TDD + DDD

- Escrever testes antes de implementar regra nova relevante.
- Preferir testes de domínio para regras financeiras puras.
- Usar testes de service para orquestração.
- Usar testes de repository para queries e migrations.
- Usar testes de controller para contrato HTTP.
- Usar testes de frontend para formulários, hooks e regras de interface.

## Fora do Escopo Atual

- Autenticação.
- Multiempresa.
- ERP completo.
- Estoque.
- Fiscal.
- Conciliação bancária.
- Agenda automática de recebíveis.
- Cálculo automático de taxa por operadora.
- Cadastro de maquininha.
