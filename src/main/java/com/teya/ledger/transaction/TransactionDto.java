package com.teya.ledger.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(title = "Transaction details")
public record TransactionDto(
		@Schema(type = "string", format = "uuid", example = "ce58d887-2a59-4dc1-a83a-0d74ea642a71")
		UUID id,
		@Schema(description = "Timestamp of transaction in milliseconds since Epoch", example = "1785703675904")
		long timestampMillis,
		@Schema(description = "Amount in transaction. Withdrawal is reflected as negative value", examples = {"100.00", "-9.50"})
		BigDecimal amount) {
}
