package br.com.recargapay.walletservice.handler;

import br.com.recargapay.walletservice.dto.out.ErrorResponse;
import br.com.recargapay.walletservice.exception.InsufficientBalanceException;
import br.com.recargapay.walletservice.exception.InvalidTransferException;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransfer(final InvalidTransferException ex) {
        log.error("Invalid transfer error: {}", ex.getMessage());
        return buildErrorResponse("Invalid transfer", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(final InsufficientBalanceException ex) {
        log.error("Insufficient balance error: {}", ex.getMessage());
        return buildErrorResponse("Insufficient balance", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletNotFound(final WalletNotFoundException ex) {
        log.error("Wallet not found: {}", ex.getMessage());
        return buildErrorResponse("Wallet not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(final MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.error("Validation failed: {}", message);
        return buildErrorResponse("Validation Error", message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(final Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse("Internal error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(final String error,final String message,final HttpStatus status) {
        final ErrorResponse response = new ErrorResponse(
                error,
                message,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }
}
