package com.teya.ledger.account;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Accounts")
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

	private final AccountService service;

	@GetMapping
	public ResponseEntity<AccountsResponseDto> listAccounts() {
		List<AccountDto> accounts = service.list()
				.stream()
				.map(this::toDto)
				.toList();

		return ResponseEntity
				.ok(new AccountsResponseDto(accounts));
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> getAccount(@PathVariable UUID id) {
		return ResponseEntity.ok(toDto(service.get(id)));
	}

	@PostMapping
	public ResponseEntity<AccountDto> createAccount(@RequestBody AccountRequestDto request) {
		Account created = service.save(request);
		AccountDto account = toDto(created);
		return new ResponseEntity<>(account, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeAccount(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	private AccountDto toDto(Account account) {
		return new AccountDto(account.getId(), account.getCurrency(), account.getBalance());
	}
}
