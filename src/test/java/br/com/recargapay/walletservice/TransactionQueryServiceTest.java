//package br.com.recargapay.walletservice;
//
//import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
//import br.com.recargapay.walletservice.entity.Transaction;
//import br.com.recargapay.walletservice.enumeration.TransactionType;
//import br.com.recargapay.walletservice.entity.Wallet;
//import br.com.recargapay.walletservice.repository.TransactionRepository;
//import br.com.recargapay.walletservice.repository.WalletRepository;
//import br.com.recargapay.walletservice.service.query.TransactionQueryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TransactionQueryServiceTest {
//
//    private WalletRepository walletRepository;
//    private TransactionRepository transactionRepository;
//    private TransactionQueryService transactionQueryService;
//
//    @BeforeEach
//    void setUp() {
//        walletRepository = mock(WalletRepository.class);
//        transactionRepository = mock(TransactionRepository.class);
//        transactionQueryService = new TransactionQueryService(transactionRepository);
//    }
//
//    @Test
//    void shouldReturnMappedTransactions() {
//        final UUID sourceWalletId = UUID.randomUUID();
//        final UUID destinationWalletId = UUID.randomUUID();
//        final Wallet sourceWallet = new Wallet();
//        final Wallet destinationWallet = new Wallet();
//
//        sourceWallet.setId(sourceWalletId);
//        destinationWallet.setId(destinationWalletId);
//
//       final Transaction transaction = Transaction.builder()
//                .timestamp(LocalDateTime.now())
//                .type(TransactionType.DEPOSIT)
//                .sourceWallet(sourceWallet)
//                .destinationWallet(destinationWallet)
//                .amount(BigDecimal.valueOf(100))
//                .balanceAfter(BigDecimal.valueOf(200))
//                .build();
//
//        when(walletRepository.findById(sourceWalletId)).thenReturn(Optional.of(sourceWallet));
//        when(transactionRepository.findAllByWalletIdAndTimestampBetween(eq(sourceWallet.getId()), any(), any())).thenReturn(List.of(transaction));
//
//        final List<WalletHistoricalStatementResponse> dtos = transactionQueryService.getTransactionStatement(sourceWalletId,
//                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
//
//        assertEquals(1, dtos.size());
//    }
//}
