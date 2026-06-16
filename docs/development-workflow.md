# Fluxo de Desenvolvimento

## Princípio geral

O desenvolvimento deve ser guiado por testes e por alterações pequenas.

- Testar regras de negócio antes de implementar quando a regra for nova ou relevante.
- Manter commits pequenos, coesos e fáceis de revisar.
- Evitar funcionalidades fora do escopo definido para a tarefa atual.

## Fluxo TDD

### Red

Escrever um teste que descreve o comportamento esperado e falha pelo motivo correto.

### Green

Implementar o mínimo necessário para o teste passar, sem ampliar o escopo.

### Refactor

Melhorar nomes, estrutura e duplicações sem mudar o comportamento validado pelos testes.

## Backend

- Usar testes de service para regras de negócio.
- Usar testes de controller para contrato HTTP, validação de payload e status.
- Usar testes de integração de repository para queries, persistência e migrations.
- Usar testes de summary e dashboard para regras financeiras.
- Rodar os testes com:

```powershell
.\mvnw.cmd test
```

## Frontend

- Por enquanto, validar alterações com `npm run build` e smoke test manual.
- Quando houver setup de testes, usar Vitest e React Testing Library.
- Testar formulários, hooks e regras de UI que afetem comportamento do usuário.

## Regras de escopo

- Não implementar login.
- Não implementar multiempresa.
- Não transformar o produto em ERP completo.
- Não alterar backend em task frontend.
- Não alterar frontend em task backend.

## Checklist antes de commit

- Testes ou build passando conforme a área alterada.
- `git status` revisado.
- Sem dados sensíveis.
- Alteração coerente com a task.
