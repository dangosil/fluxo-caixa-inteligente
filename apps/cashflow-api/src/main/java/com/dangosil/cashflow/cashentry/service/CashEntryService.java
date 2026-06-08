package com.dangosil.cashflow.cashentry.service;

import com.dangosil.cashflow.cashentry.dto.CashEntryRequest;
import com.dangosil.cashflow.cashentry.dto.CashEntryResponse;
import com.dangosil.cashflow.cashentry.entity.CashEntry;
import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import com.dangosil.cashflow.shared.exception.BusinessException;
import com.dangosil.cashflow.shared.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashEntryService {

    private final CashEntryRepository cashEntryRepository;
    private final CategoryRepository categoryRepository;

    public CashEntryService(CashEntryRepository cashEntryRepository, CategoryRepository categoryRepository) {
        this.cashEntryRepository = cashEntryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CashEntryResponse> findAll(
            LocalDate startDate,
            LocalDate endDate,
            UUID categoryId,
            PaymentMethod paymentMethod,
            Boolean active
    ) {
        boolean activeFilter = active == null || active;
        return cashEntryRepository.findAllWithFilters(startDate, endDate, categoryId, paymentMethod, activeFilter)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CashEntryResponse findById(UUID id) {
        return cashEntryRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cash entry not found"));
    }

    @Transactional
    public CashEntryResponse create(CashEntryRequest request) {
        Category category = getValidIncomeCategory(request.categoryId());
        CashEntry cashEntry = new CashEntry(
                request.description(),
                request.amount(),
                request.entryDate(),
                category,
                request.paymentMethod(),
                request.notes()
        );
        return toResponse(cashEntryRepository.save(cashEntry));
    }

    @Transactional
    public CashEntryResponse update(UUID id, CashEntryRequest request) {
        CashEntry cashEntry = getCashEntry(id);
        Category category = getValidIncomeCategory(request.categoryId());
        cashEntry.update(
                request.description(),
                request.amount(),
                request.entryDate(),
                category,
                request.paymentMethod(),
                request.notes()
        );
        return toResponse(cashEntry);
    }

    @Transactional
    public void delete(UUID id) {
        CashEntry cashEntry = getCashEntry(id);
        cashEntry.deactivate();
    }

    private CashEntry getCashEntry(UUID id) {
        return cashEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash entry not found"));
    }

    private Category getValidIncomeCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.isActive()) {
            throw new BusinessException("Category must be active");
        }

        if (category.getType() != CategoryType.INCOME) {
            throw new BusinessException("Cash entry category must be INCOME");
        }

        return category;
    }

    private CashEntryResponse toResponse(CashEntry cashEntry) {
        Category category = cashEntry.getCategory();
        return new CashEntryResponse(
                cashEntry.getId(),
                cashEntry.getDescription(),
                cashEntry.getAmount(),
                cashEntry.getEntryDate(),
                category.getId(),
                category.getName(),
                cashEntry.getPaymentMethod(),
                cashEntry.getNotes(),
                cashEntry.isActive(),
                cashEntry.getCreatedAt(),
                cashEntry.getUpdatedAt()
        );
    }
}
