package com.northstar.payments.settlement.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/settlements")
public class SettlementController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    SettlementAccepted create(@Valid @RequestBody SettlementRequest request) {
        return new SettlementAccepted(UUID.randomUUID().toString(), "ACCEPTED", Instant.now());
    }

    record SettlementRequest(@NotBlank String merchantId, @Positive long amountMinor, @NotBlank String currency) { }
    record SettlementAccepted(String settlementId, String status, Instant acceptedAt) { }
}
