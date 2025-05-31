package br.com.recargapay.walletservice.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("You don't have enough balance to perform this operation");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
