package com.teya.ledger.transaction;

import com.teya.ledger.account.Account;
import com.teya.ledger.account.AccountService;
import com.teya.ledger.currency.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService service;

    @Test
    void getTransactions_ShouldReturnTransactionsForAccount() {
        UUID accountId = UUID.randomUUID();
        when(repository.findAllByAccountId(accountId)).thenReturn(Collections.emptyList());

        List<Transaction> result = service.getTransactions(accountId);

        assertThat(result).isEmpty();
		verify(accountService).get(accountId);
        verify(repository).findAllByAccountId(accountId);
    }

    @Test
    void save_ShouldSaveTransactionAndUpdateAccountBalance() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setCurrency(Currency.EURO);
        account.setBalance(BigDecimal.TEN);
        account.setId(accountId);

        TransactionRequestDto request = new TransactionRequestDto(BigDecimal.valueOf(5));
        when(accountService.get(accountId)).thenReturn(account);
        when(repository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = service.save(accountId, request);

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(15));
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(result.getAccount()).isEqualTo(account);
        verify(accountService).get(accountId);
        verify(repository).save(any(Transaction.class));
    }
}
