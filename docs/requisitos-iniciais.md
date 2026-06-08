# Requisitos iniciais — Sistema de fluxo de caixa

## 1. Origem dos requisitos

Requisitos iniciais definidos para um cliente piloto.

## 2. Interpretação do problema

A empresa cliente quer transformar um controle manual, feito em PDF, planilha ou anotações, em um sistema integrado.

A primeira versão deve ser simples, objetiva e útil no dia a dia.

O escopo inicial não é um ERP completo. A primeira versão deve controlar:

- entradas;
- saídas;
- lucro estimado diário;
- lucro estimado mensal.

## 3. Objetivo da primeira versão

A primeira versão deve responder rapidamente:

- quanto entrou hoje;
- quanto saiu hoje;
- qual foi o lucro estimado hoje;
- quanto entrou no mês;
- quanto saiu no mês;
- qual foi o lucro estimado do mês;
- quais registros formam esses valores.

## 4. Definição de lucro estimado

Para o cliente, será usado o termo:

```txt
Lucro estimado
```

No sistema, a fórmula será:

```txt
Lucro estimado = Total de entradas - Total de saídas
```

Exemplo:

```txt
Entradas: R$ 2.000,00
Saídas: R$ 750,00
Lucro estimado: R$ 1.250,00
```

## 5. Observação importante sobre lucro

Esse valor não representa lucro contábil formal.

Ele representa o resultado financeiro estimado com base nas entradas e saídas registradas no sistema.

Mais para frente, o sistema poderá evoluir para relatórios mais completos, como lucro bruto, lucro líquido, custos, despesas e DRE simplificada.

## 6. Escopo da primeira versão

### 6.1 Cadastro de entradas

O sistema deve permitir cadastrar valores que entram na empresa.

Campos iniciais:

- descrição;
- valor;
- data;
- categoria;
- forma de recebimento;
- observação opcional.

Exemplos de entradas:

- venda de veículo elétrico;
- venda de peça;
- venda de acessório;
- serviço de assistência;
- Pix recebido;
- dinheiro recebido;
- transferência recebida;
- venda no cartão.

### 6.2 Cadastro de saídas

O sistema deve permitir cadastrar valores que saem da empresa.

Campos iniciais:

- descrição;
- valor;
- data;
- categoria;
- forma de pagamento;
- observação opcional.

Exemplos de saídas:

- compra de veículos;
- compra de peças;
- aluguel;
- energia;
- internet;
- salário;
- comissão;
- marketing;
- contador;
- imposto;
- taxa de cartão;
- frete;
- manutenção da loja;
- outras despesas.

### 6.3 Categorias

O sistema deve permitir organizar entradas e saídas por categoria.

Categorias sugeridas de entrada:

- Venda de veículo.
- Venda de peça.
- Serviço de assistência.
- Acessório.
- Outros recebimentos.

Categorias sugeridas de saída:

- Compra de veículos.
- Compra de peças.
- Frete.
- Aluguel.
- Energia.
- Internet.
- Salários.
- Comissão.
- Marketing.
- Contador.
- Impostos.
- Taxas financeiras.
- Manutenção da loja.
- Outras despesas.

### 6.4 Resumo diário

O sistema deve mostrar, para um dia específico:

- total de entradas do dia;
- total de saídas do dia;
- lucro estimado do dia;
- lista de entradas do dia;
- lista de saídas do dia.

### 6.5 Resumo mensal

O sistema deve mostrar, para um mês específico:

- total de entradas do mês;
- total de saídas do mês;
- lucro estimado do mês;
- lista de entradas do mês;
- lista de saídas do mês.

### 6.6 Dashboard simples

A tela inicial deve exibir:

- entradas do dia;
- saídas do dia;
- lucro estimado do dia;
- entradas do mês;
- saídas do mês;
- lucro estimado do mês.

## 7. Significado de sistema integrado

Neste MVP, "integrado" significa:

- os lançamentos ficam salvos em banco de dados;
- os totais são calculados automaticamente;
- o resumo diário usa os lançamentos cadastrados;
- o resumo mensal usa os lançamentos cadastrados;
- o dono não precisa recalcular manualmente;
- os dados podem ser acessados online.

Não significa, nesta primeira versão:

- integração automática com banco;
- integração com maquininha de cartão;
- integração fiscal;
- emissão de nota;
- integração com estoque.

Essas possibilidades ficam para fases futuras.

## 8. Regras de negócio

### 8.1 Entrada

- Deve ter valor maior que zero.
- Deve ter data.
- Deve ter descrição.
- Deve ter categoria de entrada.
- Deve ter forma de recebimento.

### 8.2 Saída

- Deve ter valor maior que zero.
- Deve ter data.
- Deve ter descrição.
- Deve ter categoria de saída.
- Deve ter forma de pagamento.

### 8.3 Categoria

- Categoria deve ter nome.
- Categoria deve indicar se é de entrada ou de saída.
- Categoria pode ser desativada no futuro.

### 8.4 Cálculos

Resumo diário:

```txt
Total de entradas do dia = soma das entradas com a data selecionada
Total de saídas do dia = soma das saídas com a data selecionada
Lucro estimado do dia = total de entradas do dia - total de saídas do dia
```

Resumo mensal:

```txt
Total de entradas do mês = soma das entradas do mês selecionado
Total de saídas do mês = soma das saídas do mês selecionado
Lucro estimado do mês = total de entradas do mês - total de saídas do mês
```

## 9. Telas esperadas futuramente

Para o cliente, as telas podem ser apresentadas assim:

### 9.1 Tela inicial

Resumo simples do dia e do mês.

### 9.2 Entradas

Tela para cadastrar e visualizar entradas.

### 9.3 Saídas

Tela para cadastrar e visualizar saídas.

### 9.4 Resumo diário

Tela para escolher uma data e ver o resultado do dia.

### 9.5 Resumo mensal

Tela para escolher mês/ano e ver o resultado do mês.

### 9.6 Categorias

Tela para organizar os tipos de entradas e despesas.

## 10. API necessária para o MVP

### Categorias

```txt
GET    /categories
POST   /categories
PUT    /categories/{id}
DELETE /categories/{id}
```

### Entradas

```txt
GET    /cash-entries
POST   /cash-entries
GET    /cash-entries/{id}
PUT    /cash-entries/{id}
DELETE /cash-entries/{id}
```

### Saídas

```txt
GET    /cash-expenses
POST   /cash-expenses
GET    /cash-expenses/{id}
PUT    /cash-expenses/{id}
DELETE /cash-expenses/{id}
```

### Resumos

```txt
GET /cash-summary/daily?date=2026-06-08
GET /cash-summary/monthly?year=2026&month=6
```

### Dashboard

```txt
GET /dashboard/summary?date=2026-06-08
```

Observação: tecnicamente, a API pode receber `companyId` desde o início para preparar crescimento futuro.

## 11. Critérios de aceite para a primeira versão

A primeira versão será considerada entregue quando:

- for possível cadastrar uma entrada;
- for possível cadastrar uma saída;
- for possível editar uma entrada;
- for possível editar uma saída;
- for possível excluir ou desativar uma entrada;
- for possível excluir ou desativar uma saída;
- for possível consultar entradas por período;
- for possível consultar saídas por período;
- o sistema calcular o total de entradas diário;
- o sistema calcular o total de saídas diário;
- o sistema calcular o lucro estimado diário;
- o sistema calcular o total de entradas mensal;
- o sistema calcular o total de saídas mensal;
- o sistema calcular o lucro estimado mensal;
- o dashboard exibir o resumo principal;
- os cálculos tiverem testes automatizados.

## 12. Exemplo genérico de uso

### Lançamentos do dia

Entrada:

```txt
Venda de veículo — R$ 8.000,00 — Pix
```

Entrada:

```txt
Venda de peça — R$ 350,00 — Dinheiro
```

Saída:

```txt
Compra de peça — R$ 1.200,00 — Transferência
```

Saída:

```txt
Energia — R$ 450,00 — Pix
```

Resultado do dia:

```txt
Entradas: R$ 8.350,00
Saídas: R$ 1.650,00
Lucro estimado: R$ 6.700,00
```

## 13. Backlog futuro após validação

Depois que o cliente piloto validar a primeira versão, avaliar:

- login;
- permissões;
- contas a receber;
- contas a pagar;
- venda parcelada;
- taxas de cartão;
- fluxo de caixa futuro;
- gráficos;
- exportação PDF;
- exportação Excel;
- importação CSV;
- relatórios por categoria;
- alertas;
- deploy em nuvem;
- frontend web completo.

## 14. Decisão de produto

O sistema deve ser simples para a empresa cliente e robusto por dentro.

A empresa cliente não precisa entender arquitetura, banco ou API.

A experiência precisa deixar claro que o sistema resolve:

```txt
O que entrou?
O que saiu?
Quanto sobrou?
Como foi o dia?
Como foi o mês?
```
