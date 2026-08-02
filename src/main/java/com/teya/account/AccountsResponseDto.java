package com.teya.account;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(title = "Account list")
public record AccountsResponseDto(List<AccountDto> accounts) {
}
