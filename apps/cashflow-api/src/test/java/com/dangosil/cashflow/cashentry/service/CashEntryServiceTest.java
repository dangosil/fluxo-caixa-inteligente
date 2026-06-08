package com.dangosil.cashflow.cashentry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dangosil.cashflow.cashentry.dto.CashEntryRequest;
import com.dangosil.cashflow.cashentry.dto.CashEntryResponse;
import com.dangosil.cashflow.cashentry.entity.CashEntry;
import com.dangosil.cashflow.cashentry.enums.PaymentMethod;
import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
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
class CashEntryServiceTest {

    @Mock
    private CashEntryRepository cashEntryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CashEntryService cashEntryService;

    @Test
    void shouldCreateValidCashEntry() {
        Category category = new Category("Venda de produto", CategoryType.INCOME);
        CashEntryRequest request = validRequest(category.getId());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(cashEntryRepository.save(any(CashEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CashEntryResponse response = cashEntryService.create(request);

        assertThat(response.description()).isEqualTo("Venda de produto");
        assertThat(response.amount()).isEqualByComparingTo("1500.00");
        assertThat(response.entryDate()).isEqualTo(LocalDate.of(2026, 6, 8));
        assertThat(response.categoryId()).isEqualTo(category.getId());
        assertThat(response.categoryName()).isEqualTo("Venda de produto");
        assertThat(response.paymentMethod()).isEqualTo(PaymentMethod.PIX);
        assertThat(response.active()).isTrue();
    }

    @Test
    void shouldReturnErrorWhenCategoryDoesNotExist() {
        UUID categoryId = UUID.randomUUID();
        CashEntryRequest request = validRequest(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cashEntryService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }

    @Test
    void shouldReturnErrorWhenCategoryIsExpense() {
        Category category = new Category("Aluguel", CategoryType.EXPENSE);
        CashEntryRequest request = validRequest(category.getId());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> cashEntryService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cash entry category must be INCOME");
    }

    @Test
    void shouldReturnErrorWhenCashEntryDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(cashEntryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cashEntryService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cash entry not found");
    }

    @Test
    void shouldDeactivateCashEntry() {
        Category category = new Category("Venda de produto", CategoryType.INCOME);
        CashEntry cashEntry = new CashEntry(
                "Venda de produto",
                new BigDecimal("1500.00"),
                LocalDate.of(2026, 6, 8),
                category,
                PaymentMethod.PIX,
                null
        );

        when(cashEntryRepository.findById(cashEntry.getId())).thenReturn(Optional.of(cashEntry));

        cashEntryService.delete(cashEntry.getId());

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(cashEntryRepository).findById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(cashEntry.getId());
        assertThat(cashEntry.isActive()).isFalse();
    }

    private CashEntryRequest validRequest(UUID categoryId) {
        return new CashEntryRequest(
                "Venda de produto",
                new BigDecimal("1500.00"),
                LocalDate.of(2026, 6, 8),
                categoryId,
                PaymentMethod.PIX,
                "Recebimento registrado no caixa"
        );
    }
}
