package br.com.recargapay.walletservice.service.query;

import br.com.recargapay.walletservice.dto.out.WalletBalanceResponse;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import br.com.recargapay.walletservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletQueryServiceTest {

    private WalletRepository walletRepository;
    private WalletQueryService service;

    private UUID walletId;

    @BeforeEach
    void setup() {
        walletRepository = mock(WalletRepository.class);
        service = new WalletQueryService(walletRepository);
        walletId = UUID.randomUUID();
    }

    @Test
    void getCurrentBalanceShouldReturnWalletBalanceResponse() {
        Wallet wallet = Wallet.builder()
                .id(walletId)
                .balance(BigDecimal.valueOf(200))
                .build();

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletBalanceResponse response = service.getCurrentBalance(walletId);

        assertNotNull(response);
        assertEquals(walletId, response.walletId());
        assertEquals(wallet.getBalance(), response.balance());

        verify(walletRepository).findById(walletId);
    }

    @Test
    void getCurrentBalanceShouldThrowWalletNotFoundExceptionWhenWalletDoesNotExist() {

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class, () -> service.getCurrentBalance(walletId));
        verify(walletRepository).findById(walletId);
    }
}
