package br.com.recargapay.walletservice.controller;

import br.com.recargapay.walletservice.dto.in.WalletRequest;
import br.com.recargapay.walletservice.dto.out.WalletBalanceResponse;
import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.service.command.WalletCommandService;
import br.com.recargapay.walletservice.service.query.TransactionQueryService;
import br.com.recargapay.walletservice.service.query.WalletQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletCommandService walletCommandService;
    private final WalletQueryService walletQueryService;
    private final TransactionQueryService transactionQueryService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody @Valid WalletRequest request) {
        Wallet wallet = walletCommandService.createWallet(request);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<WalletBalanceResponse> getCurrentBalance(@PathVariable UUID walletId) {
        WalletBalanceResponse response = walletQueryService.getCurrentBalance(walletId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{walletId}/statement")
    public ResponseEntity<List<WalletHistoricalStatementResponse>> getBalanceBetween(
            @PathVariable UUID walletId,
            @RequestParam("min") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateMin,
            @RequestParam("max") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateMax) {

        List<WalletHistoricalStatementResponse> response = transactionQueryService.getTransactionStatement(
                walletId,
                dateMin,
                dateMax
        );
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

}
