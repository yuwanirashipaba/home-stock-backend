package com.example.demo.repository;

import com.example.demo.models.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;

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

    List<Stock> findByQuantityIsGreaterThan(int value);

    List<Stock> findByQuantityEquals(int value);

    @Query("{ $and: [ " +
            "{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'categoryName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'purchasedDate': { $regex: ?0, $options: 'i' } }, " +
            "{ 'expiredDate': { $regex: ?0, $options: 'i' } } " +
            "] }, " +
            "{ 'quantity': { $eq: 0 } } " +
            "] }")
    List<Stock> findByAnyFieldContainingAndOutOfStock(String substr);

    @Query("{ $and: [ " +
            "{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'categoryName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'purchasedDate': { $regex: ?0, $options: 'i' } }, " +
            "{ 'expiredDate': { $regex: ?0, $options: 'i' } } " +
            "] }, " +
            "{ 'quantity': { $gt: 0 } } " +
            "] }")
    List<Stock> findByAnyFieldContainingAndInStock(String substr);

    @Query(value = "{}", fields = "{ 'categoryName' : 1 }")
    List<Stock> findAllCategoryNames();

    @Aggregation(pipeline = {
        "{ $group: { _id: '$categoryName', count: { $sum: 1 } } }",
        "{ $project: { _id: 0, categoryName: '$_id', count: 1 } }"
    })
    List<CategoryCount> getCategoryCounts();

    @Aggregation(pipeline = {
        "{ $group: { _id: '$categoryName', totalQuantity: { $sum: '$quantity' } } }",
        "{ $project: { _id: 0, categoryName: '$_id', totalQuantity: 1 } }"
    })
    List<CategoryQuantity> getCategoryQuantities();

    interface CategoryCount {
        String getCategoryName();
        int getCount();
    }

    interface CategoryQuantity {
        String getCategoryName();
        int getTotalQuantity();
    }

    List<Stock> findByCategoryId(String category);
}
