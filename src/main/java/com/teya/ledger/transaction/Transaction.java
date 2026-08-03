package com.teya.ledger.transaction;

import com.teya.ledger.account.Account;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
	@Id
	@UuidGenerator
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	private long timestampMillis;

	private TransactionType type;

	private BigDecimal amount;

	static Transaction of(Account account, TransactionType type, BigDecimal amount) {
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setTimestampMillis(Instant.now().toEpochMilli());
		transaction.setType(type);
		transaction.setAmount(amount);
		return transaction;
	}
}
