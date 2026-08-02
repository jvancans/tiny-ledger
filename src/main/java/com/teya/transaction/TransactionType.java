package com.teya.transaction;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Indicates fund direction to/from account", example = "1")
public enum TransactionType {
	DEPOSIT(1),
	WITHDRAWAL(2);

	@Getter
	@JsonValue
	private final int code;

	TransactionType(int code) {
		this.code = code;
	}
}
