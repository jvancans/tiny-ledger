package com.teya.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(title = "Create transaction")
public record TransactionRequestDto(
		TransactionType type,
		BigDecimal amount) {
}
