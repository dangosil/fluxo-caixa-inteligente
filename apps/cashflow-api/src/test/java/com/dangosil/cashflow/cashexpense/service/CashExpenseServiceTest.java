package com.dangosil.cashflow.cashexpense.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dangosil.cashflow.cashexpense.dto.CashExpenseRequest;
import com.dangosil.cashflow.cashexpense.dto.CashExpenseResponse;
import com.dangosil.cashflow.cashexpense.entity.CashExpense;
import com.dangosil.cashflow.cashexpense.repository.CashExpenseRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import com.dangosil.cashflow.shared.exception.BusinessException;
import com.dangosil.cashflow.shared.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CashExpenseServiceTest {

    @Mock
    private CashExpenseRepository cashExpenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CashExpenseService cashExpenseService;

    @Test
    void shouldCreateValidCashExpense() {
        Category category = new Category("Aluguel", CategoryType.EXPENSE);
        CashExpenseRequest request = validRequest(category.getId());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(cashExpenseRepository.save(any(CashExpense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CashExpenseResponse response = cashExpenseService.create(request);

        assertThat(response.description()).isEqualTo("Aluguel");
        assertThat(response.amount()).isEqualByComparingTo("1200.00");
        assertThat(response.expenseDate()).isEqualTo(LocalDate.of(2026, 6, 8));
        assertThat(response.categoryId()).isEqualTo(category.getId());
        assertThat(response.categoryName()).isEqualTo("Aluguel");
        assertThat(response.paymentMethod()).isEqualTo(PaymentMethod.BANK_TRANSFER);
        assertThat(response.active()).isTrue();
    }

    @Test
    void shouldReturnErrorWhenCategoryDoesNotExist() {
        UUID categoryId = UUID.randomUUID();
        CashExpenseRequest request = validRequest(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cashExpenseService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }

    @Test
    void shouldReturnErrorWhenCategoryIsIncome() {
        Category category = new Category("Venda de produto", CategoryType.INCOME);
        CashExpenseRequest request = validRequest(category.getId());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> cashExpenseService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cash expense category must be EXPENSE");
    }

    @Test
    void shouldReturnErrorWhenCashExpenseDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(cashExpenseRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cashExpenseService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cash expense not found");
    }

    @Test
    void shouldDeactivateCashExpense() {
        Category category = new Category("Aluguel", CategoryType.EXPENSE);
        CashExpense cashExpense = new CashExpense(
                "Aluguel",
                new BigDecimal("1200.00"),
                LocalDate.of(2026, 6, 8),
                category,
                PaymentMethod.BANK_TRANSFER,
                null
        );

        when(cashExpenseRepository.findById(cashExpense.getId())).thenReturn(Optional.of(cashExpense));

        cashExpenseService.delete(cashExpense.getId());

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(cashExpenseRepository).findById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(cashExpense.getId());
        assertThat(cashExpense.isActive()).isFalse();
    }

    private CashExpenseRequest validRequest(UUID categoryId) {
        return new CashExpenseRequest(
                "Aluguel",
                new BigDecimal("1200.00"),
                LocalDate.of(2026, 6, 8),
                categoryId,
                PaymentMethod.BANK_TRANSFER,
                "Pagamento mensal"
        );
    }
}
