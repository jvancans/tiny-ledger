package com.teya.ledger.account;

import com.teya.ledger.currency.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

	@Mock
	private AccountService service;

	@InjectMocks
	private AccountController controller;

	@Test
	void listAccounts_ShouldReturnOk() {
		Account account = Account.of(Currency.EURO, BigDecimal.TEN);
		account.setId(UUID.randomUUID());
		when(service.list()).thenReturn(List.of(account));

		ResponseEntity<AccountsResponseDto> response = controller.listAccounts();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().accounts()).hasSize(1);
		assertThat(response.getBody().accounts().get(0).id()).isEqualTo(account.getId());
		verify(service).list();
	}

	@Test
	void getAccount_ShouldReturnOk() {
		UUID id = UUID.randomUUID();
		Account account = Account.of(Currency.EURO, BigDecimal.TEN);
		account.setId(id);
		when(service.get(id)).thenReturn(account);

		ResponseEntity<AccountDto> response = controller.getAccount(id);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().id()).isEqualTo(id);
		verify(service).get(id);
	}

	@Test
	void createAccount_ShouldReturnCreated() {
		AccountRequestDto request = new AccountRequestDto(Currency.EURO);
		Account account = Account.of(Currency.EURO, BigDecimal.ZERO);
		account.setId(UUID.randomUUID());
		when(service.save(request)).thenReturn(account);

		ResponseEntity<AccountDto> response = controller.createAccount(request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().id()).isEqualTo(account.getId());
		verify(service).save(request);
	}

	@Test
	void removeAccount_ShouldReturnNoContent() {
		UUID id = UUID.randomUUID();

		ResponseEntity<Void> response = controller.removeAccount(id);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(service).delete(id);
	}
}
