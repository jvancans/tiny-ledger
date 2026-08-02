package com.teya.ledger.account;

import java.util.UUID;

public class AccountNotFound extends RuntimeException {
	public AccountNotFound(UUID id) {
		super("account[" + id + "] not found");
	}
}
