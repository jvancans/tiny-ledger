package com.teya.ledger.transaction;

import com.teya.ledger.account.Account;
import com.teya.ledger.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class TransactionService {

	private final TransactionRepository repository;
	private final AccountService accountService;


	public List<Transaction> getTransactions(UUID accountId) {
		accountService.get(accountId); // ensure account exists
		return repository.findAllByAccountId(accountId);
	}

	public Transaction save(UUID accountId, TransactionRequestDto request) {
		Account account = accountService.get(accountId);
		BigDecimal transactionAmount = request.amount();

		accountService.updateAccountBalance(account, transactionAmount);

		return repository.save(Transaction.of(account, transactionAmount));
	}
}
