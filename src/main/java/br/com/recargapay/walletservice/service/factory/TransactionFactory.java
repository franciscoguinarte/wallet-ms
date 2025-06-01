package br.com.recargapay.walletservice.service.factory;

import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.enumeration.TransactionDirection;
import br.com.recargapay.walletservice.enumeration.TransactionType;
import br.com.recargapay.walletservice.entity.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TransactionFactory {

    private TransactionFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }
    public static Transaction createDeposit(final Wallet sourceWallet, final BigDecimal amount, final BigDecimal balanceAfter) {
        return Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .direction(TransactionDirection.INCOMING)
                .sourceWallet(sourceWallet)
                .amount(amount)
                .balanceSourceAfter(balanceAfter)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static Transaction createWithdraw(final Wallet sourceWallet, final BigDecimal amount, final BigDecimal balanceAfter) {
        return Transaction.builder()
                .type(TransactionType.WITHDRAWAL)
                .direction(TransactionDirection.OUTGOING)
                .sourceWallet(sourceWallet)
                .amount(amount)
                .balanceSourceAfter(balanceAfter)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static Transaction createTransfer(final Wallet sourceWallet, final  Wallet destinationWallet,
                                             final  BigDecimal amount) {
        return Transaction.builder()
                .type(TransactionType.TRANSFER)
                .direction(TransactionDirection.OUTGOING)
                .sourceWallet(sourceWallet)
                .destinationWallet(destinationWallet)
                .amount(amount)
                .balanceSourceAfter(sourceWallet.getBalance())
                .balanceDestinationAfter(destinationWallet.getBalance())
                .timestamp(LocalDateTime.now())
                .build();
    }


}