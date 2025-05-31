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
    public Transaction deposit(UUID walletId, BigDecimal amount) {
        log.info("Starting deposit...");
        Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));

        BigDecimal balanceAfter = wallet.getBalance().add(amount);
        wallet.setBalance(balanceAfter);

        Transaction deposit = TransactionFactory.createDeposit(wallet, amount, balanceAfter);
        transactionRepository.save(deposit);
        log.info("Deposit was successful!");

        return deposit;
    }

    @Transactional
    public Transaction withdraw(UUID walletId, BigDecimal amount) {
        log.info("Starting withdraw...");

        Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        BigDecimal balanceAfter = wallet.getBalance().subtract(amount);
        wallet.setBalance(balanceAfter);

        Transaction withdraw = TransactionFactory.createWithdraw(wallet, amount, balanceAfter);
        transactionRepository.save(withdraw);
        log.info("Withdraw was successful!");

        return withdraw;
    }

    @Transactional
    public Transaction transfer(UUID toWalletId, UUID fromWalletId , BigDecimal amount) {
        log.info("Starting transfer...");
        if (fromWalletId.equals(toWalletId)) {
            throw new InvalidTransferException("Cannot transfer to the same wallet");
        }

        Wallet destination = walletRepository.findByIdWithLock(toWalletId)
                .orElseThrow(() -> new WalletNotFoundException("Destination wallet not found"));
        Wallet source = walletRepository.findByIdWithLock(fromWalletId)
                .orElseThrow(() -> new WalletNotFoundException("Source wallet not found"));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        BigDecimal sourceBalance = source.getBalance().subtract(amount);
        BigDecimal destinationBalance = destination.getBalance().add(amount);

        source.setBalance(sourceBalance);
        destination.setBalance(destinationBalance);

        Transaction transfer = TransactionFactory.createTransfer(source, destination, amount, sourceBalance);
        transactionRepository.save(transfer);
        log.info("Transfer was successful!");

        return transfer;
    }
}
