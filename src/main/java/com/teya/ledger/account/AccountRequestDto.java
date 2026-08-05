package com.teya.ledger.account;

import com.teya.ledger.currency.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(title = "Create account")
public record AccountRequestDto(@NotNull Currency currency) {
}
