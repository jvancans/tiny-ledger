package com.teya.ledger.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository repository;

	List<Account> list() {
		return repository.findAll();
	}

	public Account get(UUID id) {
		return repository.findById(id)
				.orElseThrow(() -> new AccountNotFound(id));
	}

	Account save(AccountRequestDto request) {
		return repository.save(Account.of(request.currency(), BigDecimal.ZERO));
	}

	void delete(UUID id) {
		repository.delete(get(id));
	}
}
