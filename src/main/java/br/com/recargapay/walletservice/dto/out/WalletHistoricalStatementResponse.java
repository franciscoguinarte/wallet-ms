package br.com.recargapay.walletservice.dto.out;



import br.com.recargapay.walletservice.enumeration.TransactionDirection;
import br.com.recargapay.walletservice.enumeration.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletHistoricalStatementResponse(
        UUID walletId,
        TransactionType transactionType,
        BigDecimal amout,
        BigDecimal balance,
        LocalDateTime timestamp,
        TransactionDirection direction,
        @JsonInclude(JsonInclude.Include.NON_NULL) UUID destinationWalletId
) {}