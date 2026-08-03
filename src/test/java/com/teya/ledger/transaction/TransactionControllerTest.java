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

import static org.assertj.core.api.Assertions.assertThat;
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

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().transactions()).isEmpty();
        verify(service).getTransactions(accountId);
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

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().amount()).isEqualByComparingTo(BigDecimal.TEN);
        verify(service).save(accountId, request);
    }
}
