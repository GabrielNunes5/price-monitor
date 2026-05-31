package com.example.price_monitor.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Document(collection = "products")
@Data
@NoArgsConstructor
public class Product {

    @Id
    private String productId;

    private String userId;
    private String productUrl;
    private String productName;
    private String productStore;

    private BigDecimal targetPrice;
    private BigDecimal currentPrice;

    private Boolean active = true;

    @CreatedDate
    private LocalDateTime createdAt;

}
