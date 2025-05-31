package br.com.recargapay.walletservice.mapper;

import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.enumeration.TransactionType;

public final class WalletBalanceMapper {

    private WalletBalanceMapper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static WalletHistoricalStatementResponse toResponse(final Transaction transaction) {
        return new WalletHistoricalStatementResponse(
                transaction.getSourceWallet().getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getTimestamp(),
                transaction.getType().equals(TransactionType.TRANSFER) ? transaction.getDestinationWallet().getId() : null
        );
    }
}