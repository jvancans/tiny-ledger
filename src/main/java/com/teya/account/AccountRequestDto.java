package com.teya.account;

import com.teya.currency.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Create account")
public record AccountRequestDto(Currency currency) {
}
