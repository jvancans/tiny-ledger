package com.teya.ledger.transaction;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Transactions")
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts/{accountId}/transactions")
public class TransactionController {

	private final TransactionService service;

	@GetMapping
	public ResponseEntity<TransactionsResponseDto> getTransactions(@PathVariable UUID accountId) {
		List<TransactionDto> transactions = service.getTransactions(accountId).stream().map(this::toDto).toList();
		return ResponseEntity.ok(new TransactionsResponseDto(transactions));
	}

	@PostMapping
	public ResponseEntity<TransactionDto> createTransaction(@PathVariable UUID accountId, @RequestBody TransactionRequestDto request) {
		return new ResponseEntity<>(toDto(service.save(accountId, request)), HttpStatus.CREATED);
	}

	private TransactionDto toDto(Transaction transaction) {
		return new TransactionDto(transaction.getId(),
				transaction.getTimestampMillis(),
				transaction.getAmount());
	}
}
