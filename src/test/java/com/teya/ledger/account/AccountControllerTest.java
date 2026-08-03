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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().accounts().size());
		assertEquals(account.getId(), response.getBody().accounts().get(0).id());
		verify(service, times(1)).list();
	}

	@Test
	void getAccount_ShouldReturnOk() {
		UUID id = UUID.randomUUID();
		Account account = Account.of(Currency.EURO, BigDecimal.TEN);
		account.setId(id);
		when(service.get(id)).thenReturn(account);

		ResponseEntity<AccountDto> response = controller.getAccount(id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().id());
		verify(service, times(1)).get(id);
	}

	@Test
	void createAccount_ShouldReturnCreated() {
		AccountRequestDto request = new AccountRequestDto(Currency.EURO);
		Account account = Account.of(Currency.EURO, BigDecimal.ZERO);
		account.setId(UUID.randomUUID());
		when(service.save(request)).thenReturn(account);

		ResponseEntity<AccountDto> response = controller.createAccount(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(account.getId(), response.getBody().id());
		verify(service, times(1)).save(request);
	}

	@Test
	void removeAccount_ShouldReturnNoContent() {
		UUID id = UUID.randomUUID();

		ResponseEntity<Void> response = controller.removeAccount(id);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(service, times(1)).delete(id);
	}
}
