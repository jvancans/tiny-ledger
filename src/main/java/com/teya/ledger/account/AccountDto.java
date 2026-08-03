package com.teya.ledger.account;

import com.teya.ledger.currency.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(title = "Account details")
public record AccountDto(
		@Schema(type = "string", format = "uuid", example = "ce58d887-2a59-4dc1-a83a-0d74ea642a71")
		UUID id,
		Currency currency,
		@Schema(description = "Total account balance", example = "100.00")
		BigDecimal balance) {
}
