package br.com.recargapay.walletservice.service.factory;

import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.enumeration.TransactionType;
import br.com.recargapay.walletservice.entity.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionFactory {

    public static Transaction createDeposit(Wallet sourceWallet, BigDecimal amount, BigDecimal balanceAfter) {
        return Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .sourceWallet(sourceWallet)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static Transaction createWithdraw(Wallet sourceWallet, BigDecimal amount, BigDecimal balanceAfter) {
        return Transaction.builder()
                .type(TransactionType.WITHDRAWAL)
                .sourceWallet(sourceWallet)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static Transaction createTransfer(Wallet sourceWallet, Wallet destinationWallet,
                                             BigDecimal amount, BigDecimal balanceAfter) {
        return Transaction.builder()
                .type(TransactionType.TRANSFER)
                .sourceWallet(sourceWallet)
                .destinationWallet(destinationWallet)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .timestamp(LocalDateTime.now())
                .build();
    }


}