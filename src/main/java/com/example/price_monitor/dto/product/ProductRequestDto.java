package com.example.price_monitor.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank
    private String productUrl;

    @NotBlank
    private String productName;

    @NotBlank
    private String productStore;

    @NotNull
    @Min(1)
    private BigDecimal targetPrice;

    @NotNull
    @Min(1)
    private BigDecimal currentPrice;
}
