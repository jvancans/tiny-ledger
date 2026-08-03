package com.teya.ledger;

import com.jayway.jsonpath.JsonPath;
import com.teya.ledger.currency.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
class LedgerApplicationTests {

	private static final String DEFAULT_ACCOUNT_ID = "ce58d887-2a59-4dc1-a83a-0d74ea642a71";
	private static final String UNKNOWN_ACCOUNT_ID = "be58d887-2a59-4dc1-a83a-0d74ea642a72";
	private static final int EURO_ISO_NUMBER = Currency.EURO.getIsoNumber();
	private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = BigDecimal.valueOf(100.5);
	private static final BigDecimal EMPTY_ACCOUNT_BALANCE = BigDecimal.ZERO;
	private static final String CREATE_EURO_ACCOUNT_JSON = """
			{ "currency": %s }
			""".formatted(EURO_ISO_NUMBER);

	@Autowired
	private RestTestClient client;

	@BeforeEach
	void setUp() {
		client = client.mutate()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	@Test
	void shouldGetDefaultAccountById() {
		client.get().uri("/api/v1/accounts/{id}", DEFAULT_ACCOUNT_ID)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(DEFAULT_ACCOUNT_ID)
				.jsonPath("$.currency").isEqualTo(EURO_ISO_NUMBER)
				.jsonPath("$.balance").isEqualTo(DEFAULT_ACCOUNT_BALANCE);
	}

	@Test
	void shouldReturnNotFoundResponseWhenGettingNonExistingAccount() {
		client.get().uri("/api/v1/accounts/{id}", UNKNOWN_ACCOUNT_ID)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void shouldListDefaultAccount() {
		assertThat(getAccountIds()).contains(DEFAULT_ACCOUNT_ID);
	}

	@Test
	void shouldCreateAccount() {
		client.post().uri("/api/v1/accounts")
				.body(CREATE_EURO_ACCOUNT_JSON)
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$.id").exists()
				.jsonPath("$.currency").isEqualTo(EURO_ISO_NUMBER)
				.jsonPath("$.balance").isEqualTo(EMPTY_ACCOUNT_BALANCE);
	}

	@Test
	void shouldDeleteAccount() {
		String accountId = createNewEuroAccount();

		client.delete().uri("/api/v1/accounts/{id}", accountId)
				.exchange()
				.expectStatus().isNoContent();

		assertThat(getAccountIds()).doesNotContain(accountId);
	}

	@Test
	void shouldReturnNotFoundResponseWhenDeletingNonExistingAccount() {
		client.delete().uri("/api/v1/accounts/{id}", UNKNOWN_ACCOUNT_ID)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void shouldCreateDepositTransactionAndIncreaseBalance() {
		assertTransactionUpdatesBalance(BigDecimal.valueOf(1000.99));
	}

	@Test
	void shouldCreateWithdrawalTransactionAndIncreaseBalance() {
		assertTransactionUpdatesBalance(BigDecimal.valueOf(-999.99));
	}

	private void assertTransactionUpdatesBalance(BigDecimal amount) {
		String accountId = createNewEuroAccount();

		client.post().uri("/api/v1/accounts/{id}/transactions", accountId)
				.body("""
						{ "amount": %s }
						""".formatted(amount))
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$.id").exists()
				.jsonPath("$.amount").isEqualTo(amount)
				.jsonPath("$.timestampMillis").exists();

		client.get().uri("/api/v1/accounts/{id}", accountId)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(accountId)
				.jsonPath("$.currency").isEqualTo(EURO_ISO_NUMBER)
				.jsonPath("$.balance").isEqualTo(amount);
	}

	private String createNewEuroAccount() {
		String responseBody = client.post().uri("/api/v1/accounts")
				.body(CREATE_EURO_ACCOUNT_JSON)
				.exchange()
				.expectStatus().isCreated()
				.returnResult(String.class)
				.getResponseBody();
		return JsonPath.read(responseBody, "$.id");
	}

	private List<String> getAccountIds() {
		String responseBody = client.get().uri("/api/v1/accounts")
				.exchange()
				.expectStatus().isOk()
				.returnResult(String.class)
				.getResponseBody();
		return JsonPath.read(responseBody, "$.accounts[*].id");
	}
}