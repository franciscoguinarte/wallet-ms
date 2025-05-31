package br.com.recargapay.walletservice.handler;

import br.com.recargapay.walletservice.dto.out.ErrorResponse;
import br.com.recargapay.walletservice.exception.InsufficientBalanceException;
import br.com.recargapay.walletservice.exception.InvalidTransferException;
import br.com.recargapay.walletservice.exception.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InvalidTransferException ex) {
        return buildErrorResponse("Invalid transfer", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientBalanceException ex) {
        return buildErrorResponse("Insufficient balance", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletNotFound(WalletNotFoundException ex) {
        return buildErrorResponse("Wallet not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildErrorResponse("Validation Error", message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildErrorResponse("Internal error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String error, String message, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(
                error,
                message,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }
}
