package com.teya.ledger.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

	@Mock
	private TransactionService service;

	@InjectMocks
	private TransactionController controller;

	@Test
	void getTransactions_ShouldReturnOk() {
		UUID accountId = UUID.randomUUID();
		when(service.getTransactions(accountId)).thenReturn(Collections.emptyList());

		ResponseEntity<TransactionsResponseDto> response = controller.getTransactions(accountId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().transactions().isEmpty());
		verify(service, times(1)).getTransactions(accountId);
	}

	@Test
	void createTransaction_ShouldReturnCreated() {
		UUID accountId = UUID.randomUUID();
		TransactionRequestDto request = new TransactionRequestDto(BigDecimal.TEN);
		Transaction transaction = new Transaction();
		transaction.setId(UUID.randomUUID());
		transaction.setAmount(BigDecimal.TEN);
		transaction.setTimestampMillis(System.currentTimeMillis());

		when(service.save(accountId, request)).thenReturn(transaction);

		ResponseEntity<TransactionDto> response = controller.createTransaction(accountId, request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(transaction.getAmount(), response.getBody().amount());
		verify(service, times(1)).save(accountId, request);
	}
}
