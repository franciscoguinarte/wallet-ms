package br.com.recargapay.walletservice.service.query;

import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.enumeration.TransactionDirection;
import br.com.recargapay.walletservice.enumeration.TransactionType;
import br.com.recargapay.walletservice.mapper.WalletBalanceMapper;
import br.com.recargapay.walletservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionQueryServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionQueryService transactionQueryService;

    @BeforeEach
    void setup() {
        transactionRepository = mock(TransactionRepository.class);
        transactionQueryService = new TransactionQueryService(transactionRepository);
    }

    @Test
    void getTransactionStatement_shouldReturnMappedTransactionList() {
        UUID walletId = UUID.randomUUID();
        LocalDateTime from = LocalDateTime.now().minusDays(10);
        LocalDateTime to = LocalDateTime.now();

        Wallet sourceWallet = Wallet.builder().id(walletId).build();
        Wallet destinationWallet = Wallet.builder().id(UUID.randomUUID()).build();

        Transaction tx1 = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .direction(TransactionDirection.INCOMING)
                .amount(BigDecimal.TEN)
                .sourceWallet(sourceWallet)
                .timestamp(LocalDateTime.now().minusDays(5))
                .balanceSourceAfter(BigDecimal.valueOf(110))
                .build();

        Transaction tx2 = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .direction(TransactionDirection.OUTGOING)
                .amount(BigDecimal.valueOf(20))
                .sourceWallet(sourceWallet)
                .destinationWallet(destinationWallet)
                .timestamp(LocalDateTime.now().minusDays(3))
                .balanceSourceAfter(BigDecimal.valueOf(90))
                .build();

        List<Transaction> transactions = List.of(tx1, tx2);

        when(transactionRepository.findAllByWalletIdAndTimestampBetween(walletId, from, to))
                .thenReturn(transactions);

        WalletHistoricalStatementResponse response1 = new WalletHistoricalStatementResponse(
                walletId,
                TransactionType.DEPOSIT,
                BigDecimal.TEN,
                BigDecimal.valueOf(110),
                tx1.getTimestamp(),
                TransactionDirection.INCOMING,
                null
        );

        WalletHistoricalStatementResponse response2 = new WalletHistoricalStatementResponse(
                walletId,
                TransactionType.TRANSFER,
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(90),
                tx2.getTimestamp(),
                TransactionDirection.OUTGOING,
                destinationWallet.getId()
        );

        try (MockedStatic<WalletBalanceMapper> mockedMapper = mockStatic(WalletBalanceMapper.class)) {
            mockedMapper.when(() -> WalletBalanceMapper.toResponse(tx1, walletId)).thenReturn(response1);
            mockedMapper.when(() -> WalletBalanceMapper.toResponse(tx2, walletId)).thenReturn(response2);

            List<WalletHistoricalStatementResponse> result =
                    transactionQueryService.getTransactionStatement(walletId, from, to);

            assertEquals(2, result.size());
            assertEquals(response1, result.get(0));
            assertEquals(response2, result.get(1));

            verify(transactionRepository).findAllByWalletIdAndTimestampBetween(walletId, from, to);
        }
    }
}
