package br.com.recargapay.walletservice.service.command;

import br.com.recargapay.walletservice.dto.in.WalletRequest;
import br.com.recargapay.walletservice.dto.out.WalletResponse;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletCommandService {

    private final WalletRepository walletRepository;

    public Wallet createWallet(WalletRequest request) {
        Wallet wallet = Wallet.builder()
                .owner(request.owner())
                .balance(BigDecimal.ZERO)
                .build();

        walletRepository.save(wallet);

        return wallet;
    }
}
