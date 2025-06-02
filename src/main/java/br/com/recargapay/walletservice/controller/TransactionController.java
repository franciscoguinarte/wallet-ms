package br.com.recargapay.walletservice.controller;

import br.com.recargapay.walletservice.dto.in.DepositRequest;
import br.com.recargapay.walletservice.dto.in.TransferRequest;
import br.com.recargapay.walletservice.dto.in.WithdrawRequest;
import br.com.recargapay.walletservice.dto.out.TransactionResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.mapper.TransactionMapper;
import br.com.recargapay.walletservice.service.command.TransactionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Endpoints for managing deposits, withdrawals, and transfers")
public class TransactionController {

    private final TransactionCommandService transactionCommandService;

    @Operation(
            summary = "Make a deposit",
            description = "Deposits a specified amount into a wallet.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Deposit successful", content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content)
            }
    )
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody @Valid final DepositRequest request) {
        final Transaction transaction = transactionCommandService.deposit(request.sourceWalletId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(transaction));
    }

    @Operation(
            summary = "Make a withdrawal",
            description = "Withdraws a specified amount from a wallet.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Withdrawal successful", content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient balance", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content)
            }
    )
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody @Valid final WithdrawRequest request) {
        final Transaction transaction = transactionCommandService.withdraw(request.sourceWalletId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(transaction));
    }

    @Operation(
            summary = "Transfer funds between wallets",
            description = "Transfers a specified amount from one wallet to another.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transfer successful", content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient balance", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content)
            }
    )
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody @Valid final TransferRequest request) {
        final Transaction transaction = transactionCommandService.transfer(request.destinationWalletId(), request.sourceWalletId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(transaction));
    }
}