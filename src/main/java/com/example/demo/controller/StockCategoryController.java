package com.example.demo.controller;

import com.example.demo.models.StockCategory;
import com.example.demo.service.StockCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/category")
@RestController
public class StockCategoryController {
    private final StockCategoryService stockCategoryService;


    public StockCategoryController(StockCategoryService stockCategoryService) {
        this.stockCategoryService = stockCategoryService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<StockCategory> findStockCategoryById(@PathVariable String id) {
        return stockCategoryService.findStockCategoryById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<StockCategory> addStockCategory(@RequestBody StockCategory stockCategory) {
        return stockCategoryService.addStockCategory(stockCategory);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StockCategory> updateStockCategoryById(@PathVariable String id, @RequestBody StockCategory updatedStockCategory) {
        return stockCategoryService.updateStockCategoryById(id, updatedStockCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStockCategoryById(@PathVariable String id) {
        return stockCategoryService.deleteStockCategoryById(id);
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<StockCategory>> findAllStockCategories() {
        return stockCategoryService.findAllStockCategories();
    }
}
