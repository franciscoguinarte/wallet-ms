package br.com.recargapay.walletservice.exception;

public class InvalidTransferException extends RuntimeException {

    public InvalidTransferException(final String message) {
        super(message);
    }
}
