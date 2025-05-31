package br.com.recargapay.walletservice.dto.in;

import jakarta.validation.constraints.NotNull;

public record WalletRequest(
       @NotNull String owner
) {}