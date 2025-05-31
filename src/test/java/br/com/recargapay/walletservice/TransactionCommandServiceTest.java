package br.com.recargapay.walletservice;

import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.repository.TransactionRepository;
import br.com.recargapay.walletservice.repository.WalletRepository;
import br.com.recargapay.walletservice.service.command.TransactionCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionCommandServiceTest {

    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private TransactionCommandService transactionCommandService;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        transactionCommandService = new TransactionCommandService(transactionRepository, walletRepository);
    }

    @Test
    void shouldUpdateBalanceAndCreateDepositTransaction() {
        final UUID walletId = UUID.randomUUID();
        final Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        transactionCommandService.deposit(walletId, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), wallet.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldUpdateBalanceAndCreateWithdrawTransaction() {
        final UUID walletId = UUID.randomUUID();
        final Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        transactionCommandService.withdraw(walletId, BigDecimal.valueOf(40));

        assertEquals(BigDecimal.valueOf(60), wallet.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldTransferBalanceBetweenWallets() {
        final UUID fromId = UUID.randomUUID();
        final UUID toId = UUID.randomUUID();
        final Wallet source = new Wallet();
        final Wallet destination = new Wallet();

        source.setId(fromId);
        source.setBalance(BigDecimal.valueOf(100));
        destination.setId(toId);
        destination.setBalance(BigDecimal.valueOf(50));

        when(walletRepository.findByIdWithLock(fromId)).thenReturn(Optional.of(source));
        when(walletRepository.findByIdWithLock(toId)).thenReturn(Optional.of(destination));

        transactionCommandService.transfer(toId,fromId, BigDecimal.valueOf(30));

        assertEquals(BigDecimal.valueOf(70), source.getBalance());
        assertEquals(BigDecimal.valueOf(80), destination.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }
}
