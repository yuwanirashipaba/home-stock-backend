package com.example.demo.repository;

import com.example.demo.models.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StockRepository extends MongoRepository<Stock, Integer> {
    Stock findById(String id);

    void deleteById(String id);

    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'categoryName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'purchasedDate': { $regex: ?0, $options: 'i' } }, " +
            "{ 'expiredDate': { $regex: ?0, $options: 'i' } }, " +
            "] }")
    List<Stock> findByAnyFieldContaining(String substr);
}
