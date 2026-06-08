package com.dangosil.cashflow.cashentry.controller;

import com.dangosil.cashflow.cashentry.dto.CashEntryRequest;
import com.dangosil.cashflow.cashentry.dto.CashEntryResponse;
import com.dangosil.cashflow.cashentry.enums.PaymentMethod;
import com.dangosil.cashflow.cashentry.service.CashEntryService;
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
@RequestMapping("/cash-entries")
public class CashEntryController {

    private final CashEntryService cashEntryService;

    public CashEntryController(CashEntryService cashEntryService) {
        this.cashEntryService = cashEntryService;
    }

    @GetMapping
    public List<CashEntryResponse> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) Boolean active
    ) {
        return cashEntryService.findAll(startDate, endDate, categoryId, paymentMethod, active);
    }

    @GetMapping("/{id}")
    public CashEntryResponse findById(@PathVariable UUID id) {
        return cashEntryService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CashEntryResponse> create(@Valid @RequestBody CashEntryRequest request) {
        CashEntryResponse response = cashEntryService.create(request);
        return ResponseEntity
                .created(URI.create("/cash-entries/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    public CashEntryResponse update(@PathVariable UUID id, @Valid @RequestBody CashEntryRequest request) {
        return cashEntryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cashEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
