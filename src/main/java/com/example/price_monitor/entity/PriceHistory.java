package com.example.price_monitor.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "price_history")
public class PriceHistory {

    @Id
    private String id;

    private String productId;

    private BigDecimal price;

    private LocalDateTime collectedAt;
}
