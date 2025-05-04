package com.example.demo.controller;

import com.example.demo.dtos.StockRequest;
import com.example.demo.dtos.StocksFilter;
import com.example.demo.models.Stock;
import com.example.demo.service.StockService;
import com.example.demo.service.ExcelService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/stocks")
@RestController
public class StockController {
    private final StockService stockService;
    private final ExcelService excelService;

    public StockController(StockService stockService, ExcelService excelService) {
        this.stockService = stockService;
        this.excelService = excelService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Stock> findStockById(@PathVariable String id) {
        return stockService.findStockById(id);
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<Stock>> getStocks() {
        return stockService.findAllStocks();
    }

    @PostMapping("/find/filter")
    public ResponseEntity<List<Stock>> getStocks(@RequestBody StocksFilter stocksFilter) {
        return stockService.findAllStocksByFilter(stocksFilter);
    }

    @GetMapping("/find/available")
    public ResponseEntity<List<Stock>> getAvailableStocks() {
        return stockService.findAvailableStocks();
    }

    @GetMapping("/find/outofstock")
    public ResponseEntity<List<Stock>> getOutOfStockItems() {
        return stockService.findOutOfStockItems();
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

    @GetMapping("/generate/report/{availability}")
    public ResponseEntity<byte[]> generateStocksReport(@PathVariable String availability) {
        return stockService.generateStocksReport(availability);
    }

    @GetMapping("/category/counts")
    public ResponseEntity<Map<String, Integer>> getCategoryStockCounts() {
        return new ResponseEntity<>(stockService.getCategoryStockCounts(), HttpStatus.OK);
    }

    @GetMapping("/category/quantities")
    public ResponseEntity<Map<String, Integer>> getCategoryStockQuantities() {
        return new ResponseEntity<>(stockService.getCategoryStockQuantities(), HttpStatus.OK);
    }

}
