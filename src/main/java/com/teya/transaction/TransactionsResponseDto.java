package com.teya.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(title = "List account transactions")
public record TransactionsResponseDto(List<TransactionDto> transactions) {
}
