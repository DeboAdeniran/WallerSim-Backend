package com.example.walletsim_backend.dto.request;

import com.example.walletsim_backend.enums.CoinType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SendQuoteRequest {
    //fromWalletId(UUID), toAddress(String), coin(CoinType), amount(BigDecimal), note(String nullable)
    private UUID fromWalletId;
    private String toAddress;
    private CoinType coin;
    private BigDecimal amount;
    @Column(nullable = true)
    private String note;
}
