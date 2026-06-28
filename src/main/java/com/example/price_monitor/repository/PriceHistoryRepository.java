package com.example.price_monitor.repository;

import com.example.price_monitor.entity.PriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepository extends MongoRepository<PriceHistory, String> {

}
