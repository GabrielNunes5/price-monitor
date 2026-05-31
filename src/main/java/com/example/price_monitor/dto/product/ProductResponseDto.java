package com.example.price_monitor.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private String productId;
    private String productUrl;
    private String productName;
    private String productStore;
    private BigDecimal targetPrice;
    private BigDecimal currentPrice;
    private Boolean active = true;
    private LocalDateTime createdAt;
}
