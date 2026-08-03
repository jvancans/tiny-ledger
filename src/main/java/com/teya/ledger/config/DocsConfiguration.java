package com.teya.ledger.config;

import com.teya.ledger.currency.Currency;
import com.teya.ledger.transaction.TransactionType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.List;

@Configuration
public class DocsConfiguration {

	@Bean
	public OpenApiCustomizer currencySchemaCustomizer() {
		return openApi -> {
			setCurrencyEnumOptions(openApi, "AccountDto");
			setCurrencyEnumOptions(openApi, "AccountRequestDto");

		};
	}

	@Bean
	public OpenApiCustomizer transactionTypeSchemaCustomizer() {
		return openApi -> {
			setTransactionTypeEnumOptions(openApi, "TransactionDto");
			setTransactionTypeEnumOptions(openApi, "TransactionRequestDto");

		};
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void setCurrencyEnumOptions(OpenAPI openApi, String schemaName) {
		Schema<?> account = openApi.getComponents().getSchemas().get(schemaName);

		Schema currencySchema = (Schema<?>) account.getProperties().get("currency");
		List<String> enumOptions = EnumSet.allOf(Currency.class)
				.stream()
				.map(DocsConfiguration::buildCurrencyEnumDescription)
				.toList();
		currencySchema.setEnum(enumOptions);
	}

	private static String buildCurrencyEnumDescription(Currency currency) {
		return currency.getIsoCode() + " -> " + currency.getIsoNumber();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void setTransactionTypeEnumOptions(OpenAPI openApi, String schemaName) {
		Schema<?> transaction = openApi.getComponents().getSchemas().get(schemaName);

		Schema currencySchema = (Schema<?>) transaction.getProperties().get("type");
		List<String> enumOptions = EnumSet.allOf(TransactionType.class)
				.stream()
				.map(DocsConfiguration::buildTransactionTypeEnumDescription)
				.toList();
		currencySchema.setEnum(enumOptions);
	}

	private static String buildTransactionTypeEnumDescription(TransactionType type) {
		return type.name() + " -> " + type.getCode();
	}
}
