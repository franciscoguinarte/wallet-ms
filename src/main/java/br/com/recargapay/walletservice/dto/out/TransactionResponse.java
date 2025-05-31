package br.com.recargapay.walletservice.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID transactionId,
        UUID walletId,
        String walletOwner,
        @JsonInclude(JsonInclude.Include.NON_NULL) UUID destinationWalletId,
        @JsonInclude(JsonInclude.Include.NON_NULL) String destinationWalletOwner,
        LocalDateTime timestamp,
        String transactionType,
        BigDecimal amount,
        BigDecimal balanceAfter

) {
}