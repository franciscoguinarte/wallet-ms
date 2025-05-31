package br.com.recargapay.walletservice.controller;

import br.com.recargapay.walletservice.dto.in.DepositRequest;
import br.com.recargapay.walletservice.dto.in.TransferRequest;
import br.com.recargapay.walletservice.dto.in.WithdrawRequest;
import br.com.recargapay.walletservice.dto.out.TransactionResponse;
import br.com.recargapay.walletservice.entity.Transaction;
import br.com.recargapay.walletservice.mapper.TransactionMapper;
import br.com.recargapay.walletservice.service.command.TransactionCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionCommandService transactionCommandService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody @Valid final DepositRequest request) {
        final Transaction transaction = transactionCommandService.deposit(request.sourceWalletId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(transaction));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody @Valid final WithdrawRequest request) {
        final Transaction transaction = transactionCommandService.withdraw(request.sourceWalletId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(transaction));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody @Valid final TransferRequest request) {
        final Transaction transaction = transactionCommandService.transfer(request.destinationWalletId(), request.sourceWalletId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(transaction));
    }
}