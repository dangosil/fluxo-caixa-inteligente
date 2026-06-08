package com.dangosil.cashflow.cashexpense.service;

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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashExpenseService {

    private final CashExpenseRepository cashExpenseRepository;
    private final CategoryRepository categoryRepository;

    public CashExpenseService(CashExpenseRepository cashExpenseRepository, CategoryRepository categoryRepository) {
        this.cashExpenseRepository = cashExpenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CashExpenseResponse> findAll(
            LocalDate startDate,
            LocalDate endDate,
            UUID categoryId,
            PaymentMethod paymentMethod,
            Boolean active
    ) {
        boolean activeFilter = active == null || active;
        return cashExpenseRepository.findAllWithFilters(startDate, endDate, categoryId, paymentMethod, activeFilter)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CashExpenseResponse findById(UUID id) {
        return cashExpenseRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cash expense not found"));
    }

    @Transactional
    public CashExpenseResponse create(CashExpenseRequest request) {
        Category category = getValidExpenseCategory(request.categoryId());
        CashExpense cashExpense = new CashExpense(
                request.description(),
                request.amount(),
                request.expenseDate(),
                category,
                request.paymentMethod(),
                request.notes()
        );
        return toResponse(cashExpenseRepository.save(cashExpense));
    }

    @Transactional
    public CashExpenseResponse update(UUID id, CashExpenseRequest request) {
        CashExpense cashExpense = getCashExpense(id);
        Category category = getValidExpenseCategory(request.categoryId());
        cashExpense.update(
                request.description(),
                request.amount(),
                request.expenseDate(),
                category,
                request.paymentMethod(),
                request.notes()
        );
        return toResponse(cashExpense);
    }

    @Transactional
    public void delete(UUID id) {
        CashExpense cashExpense = getCashExpense(id);
        cashExpense.deactivate();
    }

    private CashExpense getCashExpense(UUID id) {
        return cashExpenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash expense not found"));
    }

    private Category getValidExpenseCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.isActive()) {
            throw new BusinessException("Category must be active");
        }

        if (category.getType() != CategoryType.EXPENSE) {
            throw new BusinessException("Cash expense category must be EXPENSE");
        }

        return category;
    }

    private CashExpenseResponse toResponse(CashExpense cashExpense) {
        Category category = cashExpense.getCategory();
        return new CashExpenseResponse(
                cashExpense.getId(),
                cashExpense.getDescription(),
                cashExpense.getAmount(),
                cashExpense.getExpenseDate(),
                category.getId(),
                category.getName(),
                cashExpense.getPaymentMethod(),
                cashExpense.getNotes(),
                cashExpense.isActive(),
                cashExpense.getCreatedAt(),
                cashExpense.getUpdatedAt()
        );
    }
}
