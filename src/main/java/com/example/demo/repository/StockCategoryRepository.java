package com.example.demo.repository;


import com.example.demo.models.StockCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockCategoryRepository  extends MongoRepository<StockCategory, Integer> {
    StockCategory findById(String id);

    void deleteById(String id);
}
