package com.dangosil.cashflow.cashexpense.controller;

import com.dangosil.cashflow.cashexpense.dto.CashExpenseRequest;
import com.dangosil.cashflow.cashexpense.dto.CashExpenseResponse;
import com.dangosil.cashflow.cashexpense.service.CashExpenseService;
import com.dangosil.cashflow.shared.enums.PaymentMethod;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cash-expenses")
public class CashExpenseController {

    private final CashExpenseService cashExpenseService;

    public CashExpenseController(CashExpenseService cashExpenseService) {
        this.cashExpenseService = cashExpenseService;
    }

    @GetMapping
    public List<CashExpenseResponse> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) Boolean active
    ) {
        return cashExpenseService.findAll(startDate, endDate, categoryId, paymentMethod, active);
    }

    @GetMapping("/{id}")
    public CashExpenseResponse findById(@PathVariable UUID id) {
        return cashExpenseService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CashExpenseResponse> create(@Valid @RequestBody CashExpenseRequest request) {
        CashExpenseResponse response = cashExpenseService.create(request);
        return ResponseEntity
                .created(URI.create("/cash-expenses/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    public CashExpenseResponse update(@PathVariable UUID id, @Valid @RequestBody CashExpenseRequest request) {
        return cashExpenseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cashExpenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
