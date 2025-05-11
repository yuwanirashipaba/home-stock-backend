package com.example.demo.service;

import com.example.demo.models.Stock;
import com.example.demo.models.StockCategory;
import com.example.demo.repository.StockCategoryRepository;
import com.example.demo.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockCategoryService {
    private StockCategoryRepository stockCategoryRepository;
    private StockRepository stockRepository;

    public StockCategoryService(StockCategoryRepository stockCategoryRepository, StockRepository stockRepository) {
        this.stockCategoryRepository = stockCategoryRepository;
        this.stockRepository = stockRepository;
    }

    public ResponseEntity<StockCategory> findStockCategoryById(String id) {
        StockCategory stockCategory = stockCategoryRepository.findById(id);
        if (stockCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stockCategory, HttpStatus.OK);
    }

    public ResponseEntity<StockCategory> addStockCategory(StockCategory stockCategory) {
        StockCategory savedStockCategory = stockCategoryRepository.save(stockCategory);
        return new ResponseEntity<>(savedStockCategory, HttpStatus.CREATED);
    }

    public ResponseEntity<StockCategory> updateStockCategoryById(String id, StockCategory updatedStockCategory) {
        StockCategory stockCategory = stockCategoryRepository.findById(id);
        if (stockCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        stockCategory.setName(updatedStockCategory.getName());
        StockCategory savedStockCategory = stockCategoryRepository.save(stockCategory);
        List<Stock> stocksByCategory = stockRepository.findByCategoryId(id);
        for (Stock stock: stocksByCategory) {
            stock.setCategoryName(updatedStockCategory.getName());
            stockRepository.save(stock);
        }
        return new ResponseEntity<>(savedStockCategory, HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteStockCategoryById(String id) {
        stockCategoryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<StockCategory>> findAllStockCategories() {
        return new ResponseEntity<>(stockCategoryRepository.findAll(), HttpStatus.OK);
    }
    
}
