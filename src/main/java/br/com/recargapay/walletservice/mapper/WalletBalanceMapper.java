package br.com.recargapay.walletservice.mapper;

import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.enumeration.TransactionDirection;
import br.com.recargapay.walletservice.enumeration.TransactionType;

import java.util.UUID;

public final class WalletBalanceMapper {

    private WalletBalanceMapper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static WalletHistoricalStatementResponse toResponse(final Transaction transaction, UUID walletId) {
        final TransactionDirection direction = walletId.equals(transaction.getSourceWallet().getId())
                ? TransactionDirection.OUTGOING
                : TransactionDirection.INCOMING;

        if (direction == TransactionDirection.OUTGOING && transaction.getType() == TransactionType.TRANSFER) {
            return new WalletHistoricalStatementResponse(
                    transaction.getSourceWallet().getId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getBalanceSourceAfter(),
                    transaction.getTimestamp(),
                    TransactionDirection.OUTGOING,
                    transaction.getDestinationWallet().getId()
            );
        }
        if (direction == TransactionDirection.INCOMING && transaction.getType() == TransactionType.TRANSFER) {
            return new WalletHistoricalStatementResponse(
                    transaction.getDestinationWallet().getId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getBalanceDestinationAfter(),
                    transaction.getTimestamp(),
                    TransactionDirection.INCOMING,
                    null
            );
        }
        return new WalletHistoricalStatementResponse(
                transaction.getSourceWallet().getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceSourceAfter(),
                transaction.getTimestamp(),
                transaction.getDirection(),
                null
        );

    }
}