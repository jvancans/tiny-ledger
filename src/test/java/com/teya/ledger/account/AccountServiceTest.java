package com.teya.ledger.account;

import com.teya.ledger.currency.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private AccountRepository repository;

	@InjectMocks
	private AccountService service;

	@Test
	void list_ShouldReturnAllAccounts() {
		Account account = Account.of(Currency.EURO, BigDecimal.TEN);
		when(repository.findAll()).thenReturn(List.of(account));

		List<Account> result = service.list();

		assertThat(result).containsExactly(account);
		verify(repository).findAll();
	}

	@Test
	void get_ShouldReturnAccount_WhenExists() {
		UUID id = UUID.randomUUID();
		Account account = Account.of(Currency.EURO, BigDecimal.TEN);
		when(repository.findById(id)).thenReturn(Optional.of(account));

		Account result = service.get(id);

		assertThat(result).isEqualTo(account);
		verify(repository).findById(id);
	}

	@Test
	void shouldUpdateAccountBalance() {
		Account account = Account.of(Currency.EURO, BigDecimal.ZERO);
		BigDecimal transactionAmount = BigDecimal.TEN;

		service.updateAccountBalance(account, transactionAmount);

		assertThat(account.getBalance()).isEqualTo(transactionAmount);
		verify(repository).save(account);
	}

	@Test
	void get_ShouldThrowAccountNotFound_WhenNotExists() {
		UUID id = UUID.randomUUID();
		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(AccountNotFound.class, () -> service.get(id));
		verify(repository).findById(id);
	}

	@Test
	void save_ShouldSaveAccount() {
		AccountRequestDto request = new AccountRequestDto(Currency.EURO);
		Account account = Account.of(Currency.EURO, BigDecimal.ZERO);
		when(repository.save(any(Account.class))).thenReturn(account);

		Account result = service.save(request);

		assertThat(result).isEqualTo(account);
		verify(repository).save(any(Account.class));
	}

	@Test
	void delete_ShouldDeleteAccount() {
		UUID id = UUID.randomUUID();
		Account account = Account.of(Currency.EURO, BigDecimal.TEN);
		when(repository.findById(id)).thenReturn(Optional.of(account));

		service.delete(id);

		verify(repository).findById(id);
		verify(repository).delete(account);
	}
}
