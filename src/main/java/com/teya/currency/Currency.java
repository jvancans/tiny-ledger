package com.teya.currency;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "ISO 4217 numeric currency code", example = "978")
public enum Currency {
	EURO("EUR", 978);

	private final String isoCode;
	@JsonValue
	private final int isoNumber;

	Currency(String isoCode, int isoNumber) {
		this.isoCode = isoCode;
		this.isoNumber = isoNumber;
	}
}
