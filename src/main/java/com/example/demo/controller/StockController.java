package com.example.demo.controller;

import com.example.demo.dtos.StockRequest;
import com.example.demo.models.Stock;
import com.example.demo.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/stocks")
@RestController
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Stock> findStockById(@PathVariable String id) {
        return stockService.findStockById(id);
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<Stock>> getStocks(@RequestParam("substr") String substr) {
        return stockService.findAllStocks(substr);
    }

    @PostMapping("/add")
    public ResponseEntity<Stock> addStock(@ModelAttribute StockRequest stockRequest) {
        return stockService.addStock(stockRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Stock> updateStockById(@PathVariable String id, @ModelAttribute StockRequest stockRequest) {
        return stockService.updateStockById(id, stockRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteStockById(@PathVariable String id) {
        return stockService.deleteStockById(id);
    }
}
