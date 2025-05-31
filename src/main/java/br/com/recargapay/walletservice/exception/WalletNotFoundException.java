package br.com.recargapay.walletservice.exception;

public class WalletNotFoundException extends RuntimeException {

  public WalletNotFoundException(String walletId) {
    super("Wallet id " + walletId + " not found");
  }

}