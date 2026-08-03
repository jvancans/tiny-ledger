package com.teya.ledger;

import com.jayway.jsonpath.JsonPath;
import com.teya.ledger.currency.Currency;
import com.teya.ledger.transaction.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LedgerApplicationTests {

	private static final String DEFAULT_ACCOUNT_ID = "ce58d887-2a59-4dc1-a83a-0d74ea642a71";
	private static final int EURO_ISO_NUMBER = Currency.EURO.getIsoNumber();
	private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = BigDecimal.valueOf(100.5);
	private static final BigDecimal EMPTY_ACCOUNT_BALANCE = BigDecimal.ZERO;
	private static final String CREATE_EURO_ACCOUNT_JSON = String.format("""
			{
			  "currency": %s
			}
			""", EURO_ISO_NUMBER);

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldGetDefaultAccountById() throws Exception {

		mockMvc.perform(get("/api/v1/accounts/{id}", DEFAULT_ACCOUNT_ID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(DEFAULT_ACCOUNT_ID))
				.andExpect(jsonPath("$.currency").value(EURO_ISO_NUMBER))
				.andExpect(jsonPath("$.balance").value(DEFAULT_ACCOUNT_BALANCE));
	}

	@Test
	void shouldReturnNotFoundResponseWhenGettingNonExistingAccount() throws Exception {

		mockMvc.perform(get("/api/v1/accounts/{id}", "be58d887-2a59-4dc1-a83a-0d74ea642a72"))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldListDefaultAccount() throws Exception {

		mockMvc.perform(get("/api/v1/accounts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accounts[*].id", hasItem(DEFAULT_ACCOUNT_ID)));
	}

	@Test
	void shouldCreateAccount() throws Exception {

		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(CREATE_EURO_ACCOUNT_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.currency").value(EURO_ISO_NUMBER))
				.andExpect(jsonPath("$.balance").value(EMPTY_ACCOUNT_BALANCE));
	}

	@Test
	void shouldDeleteAccount() throws Exception {

		String accountId = createNewEuroAccount();

		mockMvc.perform(delete("/api/v1/accounts/{id}", accountId))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/v1/accounts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accounts[*].id", not(hasItem(accountId))));
	}

	@Test
	void shouldReturnNotFoundResponseWhenDeletingNonExistingAccount() throws Exception {

		mockMvc.perform(delete("/api/v1/accounts/{id}", "be58d887-2a59-4dc1-a83a-0d74ea642a72"))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldCreateDepositTransactionAndIncreaseBalance() throws Exception {

		BigDecimal depositAmount = BigDecimal.valueOf(1000.99);
		String accountId = createNewEuroAccount();
		int transactionCode = TransactionType.DEPOSIT.getCode();

		mockMvc.perform(post("/api/v1/accounts/{id}/transactions", accountId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.format("""
								{
								    "type": %s,
								    "amount": %s
								}
								""", transactionCode, depositAmount)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.type").value(transactionCode))
				.andExpect(jsonPath("$.amount").value(depositAmount))
				.andExpect(jsonPath("$.timestampMillis").isNumber());

		mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(accountId))
				.andExpect(jsonPath("$.currency").value(EURO_ISO_NUMBER))
				.andExpect(jsonPath("$.balance").value(depositAmount));
	}

	@Test
	void shouldCreateWithdrawalTransactionAndIncreaseBalance() throws Exception {

		BigDecimal withdrawalAmount = BigDecimal.valueOf(999.99);
		String accountId = createNewEuroAccount();
		int transactionCode = TransactionType.WITHDRAWAL.getCode();

		mockMvc.perform(post("/api/v1/accounts/{id}/transactions", accountId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(String.format("""
								{
								    "type": %s,
								    "amount": %s
								}
								""", transactionCode, withdrawalAmount)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.type").value(transactionCode))
				.andExpect(jsonPath("$.amount").value(withdrawalAmount))
				.andExpect(jsonPath("$.timestampMillis").isNumber());

		mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(accountId))
				.andExpect(jsonPath("$.currency").value(EURO_ISO_NUMBER))
				.andExpect(jsonPath("$.balance").value(BigDecimal.ZERO.subtract(withdrawalAmount)));
	}

	private String createNewEuroAccount() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(CREATE_EURO_ACCOUNT_JSON))
				.andExpect(status().isCreated())
				.andReturn();
		return JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
	}
}
