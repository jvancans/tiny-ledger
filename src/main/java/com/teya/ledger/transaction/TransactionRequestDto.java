package com.teya.ledger.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

@Schema(title = "Create transaction")
public record TransactionRequestDto(@Schema(examples = {"1000.99", "-99.55"})
									@Digits(integer = 10, fraction = 2) BigDecimal amount) {
}
