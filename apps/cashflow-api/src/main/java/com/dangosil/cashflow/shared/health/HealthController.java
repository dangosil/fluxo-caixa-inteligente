package com.dangosil.cashflow.shared.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse("UP", "cashflow-api");
    }

    public record HealthResponse(String status, String application) {
    }
}
