package br.com.recargapay.walletservice.service.query;

import br.com.recargapay.walletservice.dto.out.WalletBalanceResponse;
import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.repository.WalletRepository;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletQueryService {

    private final WalletRepository walletRepository;

    public WalletBalanceResponse getCurrentBalance(UUID walletId) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));

        return new WalletBalanceResponse(
                walletId,
                wallet.getBalance(),
                wallet.getCreatedAt()
        );
    }
}
