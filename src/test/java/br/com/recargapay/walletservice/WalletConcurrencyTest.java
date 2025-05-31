package br.com.recargapay.walletservice;

import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.repository.WalletRepository;
import br.com.recargapay.walletservice.service.command.TransactionCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@ActiveProfiles("test")
class WalletConcurrencyTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionCommandService transactionCommandService;

    private UUID walletId;

    @BeforeEach
    void setup() {
        Wallet wallet = Wallet.builder()
                .owner("test-user")
                .build();

        Wallet savedWallet = walletRepository.saveAndFlush(wallet);
        this.walletId = savedWallet.getId();

        System.out.println("[Setup] Wallet criada com ID: " + walletId + " e saldo inicial: " + savedWallet.getBalance());
    }

    @Test
    void shouldHandleConcurrentDepositsCorrectly() throws InterruptedException {
        int numberOfThreads = 10;
        BigDecimal depositAmount = new BigDecimal("10.00");

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        System.out.println("[Teste] Iniciando " + numberOfThreads + " threads para depósitos concorrentes...");

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadNumber = i + 1;
            executorService.submit(() -> {
                System.out.println("[Thread " + threadNumber + "] Iniciando depósito de " + depositAmount);
                try {
                    transactionCommandService.deposit(walletId, depositAmount);
                    System.out.println("[Thread " + threadNumber + "] Depósito concluído");
                } catch (Exception e) {
                    System.err.println("[Thread " + threadNumber + "] Erro no depósito: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Wallet updated = walletRepository.findById(walletId).orElseThrow();
        BigDecimal expected = depositAmount.multiply(BigDecimal.valueOf(numberOfThreads));

        System.out.println("[Teste] Todos depósitos finalizados. Saldo esperado: " + expected + " | Saldo atual: " + updated.getBalance());

        assertEquals(0, expected.compareTo(updated.getBalance()));
    }
}

