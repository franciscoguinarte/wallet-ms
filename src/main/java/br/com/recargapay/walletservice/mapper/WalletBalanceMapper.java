package br.com.recargapay.walletservice.mapper;

import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.enumeration.TransactionType;

public class WalletBalanceMapper {

    public static WalletHistoricalStatementResponse toResponse(Transaction transaction) {
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