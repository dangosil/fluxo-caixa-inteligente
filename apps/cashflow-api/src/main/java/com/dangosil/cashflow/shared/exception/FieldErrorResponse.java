package com.dangosil.cashflow.shared.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}
