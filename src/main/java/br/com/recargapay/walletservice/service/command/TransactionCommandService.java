package br.com.recargapay.walletservice.service.command;

import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.exception.InsufficientBalanceException;
import br.com.recargapay.walletservice.exception.InvalidTransferException;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import br.com.recargapay.walletservice.repository.TransactionRepository;
import br.com.recargapay.walletservice.repository.WalletRepository;
import br.com.recargapay.walletservice.service.factory.TransactionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionCommandService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public Transaction deposit(final UUID walletId, final BigDecimal amount) {
        log.info("Starting deposit...");
        final Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));

        final BigDecimal balanceAfter = wallet.getBalance().add(amount);
        wallet.setBalance(balanceAfter);

        final Transaction deposit = TransactionFactory.createDeposit(wallet, amount, balanceAfter);
        transactionRepository.save(deposit);
        log.info("Deposit was successful!");

        return deposit;
    }

    @Transactional
    public Transaction withdraw(final UUID walletId, final BigDecimal amount) {
        log.info("Starting withdraw...");

        final Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        final BigDecimal balanceAfter = wallet.getBalance().subtract(amount);
        wallet.setBalance(balanceAfter);

        final Transaction withdraw = TransactionFactory.createWithdraw(wallet, amount, balanceAfter);
        transactionRepository.save(withdraw);
        log.info("Withdraw was successful!");

        return withdraw;
    }

    @Transactional
    public Transaction transfer(final UUID toWalletId,final UUID fromWalletId, final BigDecimal amount) {
        log.info("Starting transfer...");
        if (fromWalletId.equals(toWalletId)) {
            throw new InvalidTransferException("Cannot transfer to the same wallet");
        }

        final Wallet destination = walletRepository.findByIdWithLock(toWalletId)
                .orElseThrow(() -> new WalletNotFoundException("Destination wallet not found"));
        final Wallet source = walletRepository.findByIdWithLock(fromWalletId)
                .orElseThrow(() -> new WalletNotFoundException("Source wallet not found"));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        final BigDecimal sourceBalance = source.getBalance().subtract(amount);
        final BigDecimal destinationBalance = destination.getBalance().add(amount);

        source.setBalance(sourceBalance);
        destination.setBalance(destinationBalance);

        final Transaction transfer = TransactionFactory.createTransfer(source, destination, amount);
        transactionRepository.save(transfer);
        log.info("Transfer was successful!");

        return transfer;
    }
}
