package br.com.recargapay.walletservice.service.command;

import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.exception.InsufficientBalanceException;
import br.com.recargapay.walletservice.exception.InvalidTransferException;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import br.com.recargapay.walletservice.repository.TransactionRepository;
import br.com.recargapay.walletservice.repository.WalletRepository;
import br.com.recargapay.walletservice.service.factory.TransactionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionCommandServiceTest {

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private TransactionCommandService service;

    private final UUID walletId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        transactionRepository = mock(TransactionRepository.class);
        walletRepository = mock(WalletRepository.class);
        service = new TransactionCommandService(transactionRepository, walletRepository);
    }

    @Test
    void depositShouldIncreaseBalanceAndSaveTransaction() {
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        BigDecimal amount = BigDecimal.valueOf(50);
        BigDecimal expectedBalance = BigDecimal.valueOf(150);
        Transaction expectedTransaction = new Transaction();

        try (MockedStatic<TransactionFactory> factoryMock = mockStatic(TransactionFactory.class)) {
            factoryMock.when(() ->
                    TransactionFactory.createDeposit(wallet, amount, expectedBalance)
            ).thenReturn(expectedTransaction);

            Transaction result = service.deposit(walletId, amount);

            assertEquals(expectedBalance, wallet.getBalance());
            assertEquals(expectedTransaction, result);
            verify(transactionRepository).save(expectedTransaction);
        }
    }

    @Test
    void withdrawShouldDecreaseBalanceAndSaveTransaction() {
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        BigDecimal amount = BigDecimal.valueOf(40);
        BigDecimal expectedBalance = BigDecimal.valueOf(60);
        Transaction expectedTransaction = new Transaction();

        try (MockedStatic<TransactionFactory> factoryMock = mockStatic(TransactionFactory.class)) {
            factoryMock.when(() ->
                    TransactionFactory.createWithdraw(wallet, amount, expectedBalance)
            ).thenReturn(expectedTransaction);

            Transaction result = service.withdraw(walletId, amount);

            assertEquals(expectedBalance, wallet.getBalance());
            assertEquals(expectedTransaction, result);
            verify(transactionRepository).save(expectedTransaction);
        }
    }

    @Test
    void withdrawShouldThrowExceptionWhenBalanceIsInsufficient() {
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(30));

        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientBalanceException.class,
                () -> service.withdraw(walletId, BigDecimal.valueOf(50)));
    }

    @Test
    void transferShouldMoveFundsBetweenWalletsAndSaveTransaction() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        Wallet source = new Wallet();
        source.setId(sourceId);
        source.setBalance(BigDecimal.valueOf(200));

        Wallet destination = new Wallet();
        destination.setId(destinationId);
        destination.setBalance(BigDecimal.valueOf(100));

        when(walletRepository.findByIdWithLock(sourceId)).thenReturn(Optional.of(source));
        when(walletRepository.findByIdWithLock(destinationId)).thenReturn(Optional.of(destination));

        BigDecimal amount = BigDecimal.valueOf(50);
        BigDecimal expectedSourceBalance = BigDecimal.valueOf(150);
        BigDecimal expectedDestinationBalance = BigDecimal.valueOf(150);
        Transaction expectedTransaction = new Transaction();

        try (MockedStatic<TransactionFactory> factoryMock = mockStatic(TransactionFactory.class)) {
            factoryMock.when(() ->
                    TransactionFactory.createTransfer(source, destination, amount)
            ).thenReturn(expectedTransaction);

            Transaction result = service.transfer(destinationId, sourceId, amount);

            assertEquals(expectedSourceBalance, source.getBalance());
            assertEquals(expectedDestinationBalance, destination.getBalance());
            verify(transactionRepository).save(expectedTransaction);
            assertEquals(expectedTransaction, result);
        }
    }

    @Test
    void transferShouldThrowWhenSameWallet() {
        UUID sameId = UUID.randomUUID();
        assertThrows(InvalidTransferException.class,
                () -> service.transfer(sameId, sameId, BigDecimal.TEN));
    }

    @Test
    void depositShouldThrowWhenWalletNotFound() {
        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
                () -> service.deposit(walletId, BigDecimal.TEN));
    }

    @Test
    void withdrawShouldThrowWhenWalletNotFound() {
        when(walletRepository.findByIdWithLock(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
                () -> service.withdraw(walletId, BigDecimal.TEN));
    }

    @Test
    void transferShouldThrowWhenSourceWalletNotFound() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        when(walletRepository.findByIdWithLock(destinationId)).thenReturn(Optional.of(new Wallet()));
        when(walletRepository.findByIdWithLock(sourceId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
                () -> service.transfer(destinationId, sourceId, BigDecimal.TEN));
    }

    @Test
    void transferShouldThrowWhenDestinationWalletNotFound() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        when(walletRepository.findByIdWithLock(destinationId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
                () -> service.transfer(destinationId, sourceId, BigDecimal.TEN));
    }

    @Test
    void transferShouldThrowWhenInsufficientBalance() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();

        Wallet source = new Wallet();
        source.setId(sourceId);
        source.setBalance(BigDecimal.valueOf(20));

        Wallet destination = new Wallet();
        destination.setId(destinationId);
        destination.setBalance(BigDecimal.valueOf(100));

        when(walletRepository.findByIdWithLock(sourceId)).thenReturn(Optional.of(source));
        when(walletRepository.findByIdWithLock(destinationId)).thenReturn(Optional.of(destination));

        assertThrows(InsufficientBalanceException.class,
                () -> service.transfer(destinationId, sourceId, BigDecimal.valueOf(50)));
    }
}
