package com.teya.ledger.currency;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Currency currency) {
		if (currency == null) {
			return null;
		}
		return currency.getIsoNumber();
	}

	@Override
	public Currency convertToEntityAttribute(Integer isoNumber) {
		if (isoNumber == null) {
			return null;
		}

		return Stream.of(Currency.values())
				.filter(currency -> isoNumber.equals(currency.getIsoNumber()))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
