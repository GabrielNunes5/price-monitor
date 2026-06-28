package com.example.price_monitor.service;

import com.example.price_monitor.entity.PriceHistory;
import com.example.price_monitor.repository.PriceHistoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;


    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public void register(String productId, BigDecimal price){
        PriceHistory entry = new PriceHistory();
        entry.setProductId(productId);
        entry.setPrice(price);
        entry.setCollectedAt(LocalDateTime.now());
        priceHistoryRepository.save(entry);
    }
}
