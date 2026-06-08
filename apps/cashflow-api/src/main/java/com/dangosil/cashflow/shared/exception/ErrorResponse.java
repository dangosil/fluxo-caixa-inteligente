package com.dangosil.cashflow.shared.exception;

import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        List<FieldErrorResponse> details
) {
}
