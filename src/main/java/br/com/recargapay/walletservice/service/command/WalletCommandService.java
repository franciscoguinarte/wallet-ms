package br.com.recargapay.walletservice.service.command;

import br.com.recargapay.walletservice.dto.in.WalletRequest;
import br.com.recargapay.walletservice.dto.out.WalletResponse;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletCommandService {

    private final WalletRepository walletRepository;

    public Wallet createWallet(WalletRequest request) {
        log.info("Creating wallet...");
        Wallet wallet = Wallet.builder()
                .owner(request.owner())
                .balance(BigDecimal.ZERO)
                .build();

        walletRepository.save(wallet);
        log.info("Wallet was created!");

        return wallet;
    }
}
