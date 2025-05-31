package br.com.recargapay.walletservice.mapper;

import br.com.recargapay.walletservice.dto.out.TransactionResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.enumeration.TransactionType;

public final class TransactionMapper {

    private TransactionMapper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static TransactionResponse toResponse(final Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getSourceWallet().getId(),
                transaction.getSourceWallet().getOwner(),
                transaction.getType().equals(TransactionType.TRANSFER) ? transaction.getDestinationWallet().getId() : null,
                transaction.getType().equals(TransactionType.TRANSFER) ? transaction.getDestinationWallet().getOwner() : null,
                transaction.getTimestamp(),
                transaction.getType().name(),
                transaction.getAmount(),
                transaction.getBalanceAfter()

        );
    }

}