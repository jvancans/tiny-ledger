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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

		assertTrue(result.isEmpty());
		verify(repository, times(1)).findAllByAccountId(accountId);
	}

	@Test
	void save_ShouldSaveTransactionAndUpdateAccountBalance() {
		UUID accountId = UUID.randomUUID();
		Account account = Account.of(Currency.EURO, BigDecimal.TEN);
		account.setId(accountId);

		TransactionRequestDto request = new TransactionRequestDto(BigDecimal.valueOf(5));
		when(accountService.get(accountId)).thenReturn(account);
		when(repository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Transaction result = service.save(accountId, request);

		assertEquals(BigDecimal.valueOf(15), account.getBalance());
		assertEquals(BigDecimal.valueOf(5), result.getAmount());
		assertEquals(account, result.getAccount());
		verify(accountService, times(1)).get(accountId);
		verify(repository, times(1)).save(any(Transaction.class));
	}
}
