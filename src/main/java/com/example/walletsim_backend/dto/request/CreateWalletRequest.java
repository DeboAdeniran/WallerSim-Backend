package com.example.walletsim_backend.dto.request;

import com.example.walletsim_backend.enums.CoinType;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class CreateWalletRequest {
    //coin(CoinType), label(String nullable)
    private CoinType coin;
    @Nullable
    private String label;
}
