package br.com.recargapay.walletservice.controller;

import br.com.recargapay.walletservice.dto.in.WalletRequest;
import br.com.recargapay.walletservice.dto.out.WalletBalanceResponse;
import br.com.recargapay.walletservice.dto.out.WalletHistoricalStatementResponse;
import br.com.recargapay.walletservice.entity.Wallet;
import br.com.recargapay.walletservice.service.command.WalletCommandService;
import br.com.recargapay.walletservice.service.query.TransactionQueryService;
import br.com.recargapay.walletservice.service.query.WalletQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Wallets", description = "Endpoints for wallet management and balance queries")
public class WalletController {

    private final WalletCommandService walletCommandService;
    private final WalletQueryService walletQueryService;
    private final TransactionQueryService transactionQueryService;

    @Operation(
            summary = "Create a new wallet",
            description = "Creates a new wallet with an initial configuration.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wallet created successfully", content = @Content(schema = @Schema(implementation = Wallet.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody @Valid final WalletRequest request) {
        final Wallet wallet = walletCommandService.createWallet(request);
        return ResponseEntity.ok(wallet);
    }

    @Operation(
            summary = "Get current wallet balance",
            description = "Returns the current balance of a wallet.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully", content = @Content(schema = @Schema(implementation = WalletBalanceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content)
            }
    )
    @GetMapping("/{walletId}/balance")
    public ResponseEntity<WalletBalanceResponse> getCurrentBalance(@PathVariable final UUID walletId) {
        final WalletBalanceResponse response = walletQueryService.getCurrentBalance(walletId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get historical balance statements",
            description = "Returns a list of transactions for the specified wallet between a date range.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statement retrieved successfully", content = @Content(schema = @Schema(implementation = WalletHistoricalStatementResponse.class))),
                    @ApiResponse(responseCode = "204", description = "No transactions found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid date parameters", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content)
            }
    )
    @GetMapping("/{walletId}/statement")
    public ResponseEntity<List<WalletHistoricalStatementResponse>> getBalanceBetween(
            @PathVariable final UUID walletId,
            @RequestParam("min") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime dateMin,
            @RequestParam("max") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime dateMax) {

        final List<WalletHistoricalStatementResponse> response = transactionQueryService.getTransactionStatement(
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
