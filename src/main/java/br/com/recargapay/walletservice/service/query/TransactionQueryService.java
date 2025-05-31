package br.com.recargapay.walletservice.service.query;

import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.mapper.WalletBalanceMapper;
import br.com.recargapay.walletservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<WalletHistoricalStatementResponse> getTransactionStatement(final UUID walletId, final LocalDateTime from, final LocalDateTime to) {
        log.info("Searching for transaction statement...");

        final List<Transaction> transactions = transactionRepository.findAllByWalletIdAndTimestampBetween(walletId, from, to);
        return transactions.stream().map(WalletBalanceMapper::toResponse).toList();
    }

}
