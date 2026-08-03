package com.teya.ledger.config;

import com.teya.ledger.account.AccountNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccountNotFound.class)
	public ResponseEntity<ErrorResponseDto> handleNotFound(AccountNotFound ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponseDto(ex.getMessage(), Instant.now().toEpochMilli()));
	}
}
