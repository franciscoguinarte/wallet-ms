package br.com.recargapay.walletservice.service.command;

import br.com.recargapay.walletservice.dto.in.WalletRequest;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletCommandServiceTest {

    private WalletRepository walletRepository;
    private WalletCommandService service;

    @BeforeEach
    void setup() {
        walletRepository = mock(WalletRepository.class);
        service = new WalletCommandService(walletRepository);
    }

    @Test
    void createWalletShouldCreateWalletWithZeroBalance() {
        String owner = "João Silva";
        WalletRequest request = new WalletRequest(owner);

        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Wallet result = service.createWallet(request);

        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        assertEquals(BigDecimal.ZERO, result.getBalance());

        verify(walletRepository).save(result);
    }
}
