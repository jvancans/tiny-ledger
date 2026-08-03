package com.teya.ledger.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(title = "Create transaction")
public record TransactionRequestDto(@Schema(examples = {"1000.99", "-99.55"}) BigDecimal amount) {
}
