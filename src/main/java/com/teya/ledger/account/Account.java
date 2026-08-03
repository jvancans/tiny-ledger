package com.teya.ledger.account;

import com.teya.ledger.currency.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "currency_iso_number")
	private Currency currency;

	@Column(name = "balance")
	private BigDecimal balance;

	public static Account of(Currency currency, BigDecimal balance) {
		Account account = new Account();
		account.setCurrency(currency);
		account.setBalance(balance);
		return account;
	}
}
