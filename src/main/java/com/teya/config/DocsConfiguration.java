package com.teya.config;

import com.teya.currency.Currency;
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
}
