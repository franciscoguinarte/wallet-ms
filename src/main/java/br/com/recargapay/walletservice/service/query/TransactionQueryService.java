package br.com.recargapay.walletservice.service.query;

import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import br.com.recargapay.walletservice.mapper.WalletBalanceMapper;
import br.com.recargapay.walletservice.repository.TransactionRepository;
import br.com.recargapay.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<WalletHistoricalStatementResponse> getTransactionStatement(UUID walletId, LocalDateTime from, LocalDateTime to) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));

        List<Transaction> transactions = transactionRepository.findAllByWalletIdAndTimestampBetween(wallet.getId(), from, to);
        return transactions.stream().map(WalletBalanceMapper::toResponse).toList();
    }

}
