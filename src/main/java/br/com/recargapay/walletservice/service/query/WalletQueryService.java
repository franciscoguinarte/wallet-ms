package br.com.recargapay.walletservice.service.query;

import br.com.recargapay.walletservice.dto.out.WalletBalanceResponse;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.repository.WalletRepository;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletQueryService {

    private final WalletRepository walletRepository;

    public WalletBalanceResponse getCurrentBalance(final UUID walletId) {
        log.info("Getting current balance ... ");
        final Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId.toString()));

        return new WalletBalanceResponse(
                walletId,
                wallet.getBalance(),
                wallet.getCreatedAt()
        );
    }
}
