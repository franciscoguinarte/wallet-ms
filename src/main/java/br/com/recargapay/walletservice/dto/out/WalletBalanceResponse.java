package br.com.recargapay.walletservice.dto.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletBalanceResponse(
        UUID walletId,
        BigDecimal balance,
        LocalDateTime timestamp
) {}