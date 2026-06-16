package com.dangosil.cashflow.cashentry.service;

import com.dangosil.cashflow.cashentry.dto.CashEntryRequest;
import com.dangosil.cashflow.cashentry.dto.CashEntryResponse;
import com.dangosil.cashflow.cashentry.entity.CashEntry;
import com.dangosil.cashflow.cashentry.enums.FeePayer;
import com.dangosil.cashflow.cashentry.repository.CashEntryRepository;
import com.dangosil.cashflow.category.entity.Category;
import com.dangosil.cashflow.category.enums.CategoryType;
import com.dangosil.cashflow.category.repository.CategoryRepository;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import com.dangosil.cashflow.shared.exception.BusinessException;
import com.dangosil.cashflow.shared.exception.ResourceNotFoundException;
import java.math.BigDecimal;
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
        FeeFields feeFields = resolveAndValidateFeeFields(request);
        CashEntry cashEntry = new CashEntry(
                request.description(),
                request.amount(),
                request.entryDate(),
                category,
                request.paymentMethod(),
                request.notes(),
                resolveExpectedReceiptDate(request),
                feeFields.feeAmount(),
                feeFields.feePayer(),
                request.cardBrand(),
                feeFields.installmentCount(),
                request.installmentAmount()
        );
        return toResponse(cashEntryRepository.save(cashEntry));
    }

    @Transactional
    public CashEntryResponse update(UUID id, CashEntryRequest request) {
        CashEntry cashEntry = getCashEntry(id);
        Category category = getValidIncomeCategory(request.categoryId());
        FeeFields feeFields = resolveAndValidateFeeFields(request);
        cashEntry.update(
                request.description(),
                request.amount(),
                request.entryDate(),
                category,
                request.paymentMethod(),
                request.notes(),
                resolveExpectedReceiptDate(request),
                feeFields.feeAmount(),
                feeFields.feePayer(),
                request.cardBrand(),
                feeFields.installmentCount(),
                request.installmentAmount()
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

    private FeeFields resolveAndValidateFeeFields(CashEntryRequest request) {
        BigDecimal feeAmount = request.feeAmount() == null ? BigDecimal.ZERO : request.feeAmount();
        FeePayer feePayer = request.feePayer() == null ? FeePayer.MERCHANT : request.feePayer();
        int installmentCount = request.installmentCount() == null ? 1 : request.installmentCount();

        if (feeAmount.signum() < 0) {
            throw new BusinessException("Fee amount cannot be negative");
        }

        if (request.installmentAmount() != null && request.installmentAmount().signum() < 0) {
            throw new BusinessException("Installment amount cannot be negative");
        }

        if (installmentCount < 1) {
            throw new BusinessException("Installment count must be greater than or equal to 1");
        }

        if (feePayer == FeePayer.MERCHANT && feeAmount.compareTo(request.amount()) > 0) {
            throw new BusinessException("Fee amount cannot be greater than amount when fee payer is MERCHANT");
        }

        BigDecimal receivedAmount = feePayer == FeePayer.MERCHANT
                ? request.amount().subtract(feeAmount)
                : request.amount();

        if (receivedAmount.signum() < 0) {
            throw new BusinessException("Received amount cannot be negative");
        }

        return new FeeFields(feeAmount, feePayer, installmentCount);
    }

    private LocalDate resolveExpectedReceiptDate(CashEntryRequest request) {
        return request.expectedReceiptDate() == null ? request.entryDate() : request.expectedReceiptDate();
    }

    private CashEntryResponse toResponse(CashEntry cashEntry) {
        Category category = cashEntry.getCategory();
        return new CashEntryResponse(
                cashEntry.getId(),
                cashEntry.getDescription(),
                cashEntry.getAmount(),
                cashEntry.getEntryDate(),
                cashEntry.getExpectedReceiptDate(),
                category.getId(),
                category.getName(),
                cashEntry.getPaymentMethod(),
                cashEntry.getNotes(),
                cashEntry.getFeeAmount(),
                cashEntry.getFeePayer(),
                cashEntry.getCardBrand(),
                cashEntry.getInstallmentCount(),
                cashEntry.getInstallmentAmount(),
                cashEntry.getCustomerPaidAmount(),
                cashEntry.getReceivedAmount(),
                cashEntry.isActive(),
                cashEntry.getCreatedAt(),
                cashEntry.getUpdatedAt()
        );
    }

    private record FeeFields(BigDecimal feeAmount, FeePayer feePayer, int installmentCount) {
    }
}
