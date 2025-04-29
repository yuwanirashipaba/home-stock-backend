package com.example.demo.service;

import com.example.demo.dtos.StockRequest;
import com.example.demo.models.Stock;
import com.example.demo.models.StockCategory;
import com.example.demo.repository.StockCategoryRepository;
import com.example.demo.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockCategoryRepository stockCategoryRepository;

    // TODO: change image directory
    private static final String IMAGE_DIRECTORY = "C:\\Users\\ASUS\\Downloads\\Archive\\ITPM-Group-Project\\public\\products\\";

    public StockService(StockRepository stockRepository, StockCategoryRepository stockCategoryRepository) {
        this.stockRepository = stockRepository;
        this.stockCategoryRepository = stockCategoryRepository;
    }

    public ResponseEntity<Stock> findStockById(String id) {
        Stock stock = stockRepository.findById(id);
        if (stock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    public ResponseEntity<Stock> addStock(StockRequest stockRequest) {
        try {
            StockCategory stockCategory = stockCategoryRepository.findById(stockRequest.getCategoryId());
            Stock stock = new Stock();
            stock.setName(stockRequest.getName());
            stock.setCategoryId(stockRequest.getCategoryId());
            stock.setCategoryName(stockCategory.getName());
            stock.setUnitPrice(stockRequest.getUnitPrice());
            stock.setQuantity(stockRequest.getQuantity());
            stock.setPurchasedDate(stockRequest.getPurchasedDate());
            stock.setExpiredDate(stockRequest.getExpiredDate());
            MultipartFile imageFile = stockRequest.getImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                stock.setFilepath(imagePath);
            }
            Stock savedStock = stockRepository.save(stock);
            return new ResponseEntity<>(savedStock, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String saveImage(MultipartFile imageFile) {
        //validate image and check if it is greater than 1GIB
        if (!imageFile.getContentType().startsWith("image/") || imageFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("Only image files under 1GIG is allowed");
        }

        //create the directory if it doesn't exist
        File directory = new File(IMAGE_DIRECTORY);

        if (!directory.exists()) {
            directory.mkdir();
            System.out.println("Directory was created");
        }
        //generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        //Get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;
        System.out.println("ImagePath = "+imagePath);

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile); //writing the image to this folder
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving Image: " + e.getMessage());
        }
        return "/products/"+uniqueFileName;


    }

    public ResponseEntity<Stock> updateStockById(String id, StockRequest updatedStock) {
        Stock stock = stockRepository.findById(id);
        if (stock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        StockCategory updatedStockCategory = stockCategoryRepository.findById(updatedStock.getCategoryId());
        if (updatedStockCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            stock.setName(updatedStock.getName());
            stock.setCategoryId(updatedStock.getCategoryId());
            stock.setCategoryName(updatedStock.getName());
            stock.setUnitPrice(updatedStock.getUnitPrice());
            stock.setQuantity(updatedStock.getQuantity());
            stock.setPurchasedDate(updatedStock.getPurchasedDate());
            stock.setExpiredDate(updatedStock.getExpiredDate());
            MultipartFile imageFile = updatedStock.getImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                stock.setFilepath(imagePath);
            }
            Stock savedStock = stockRepository.save(stock);
            return new ResponseEntity<>(savedStock, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteStockById(String id) {
        Stock stock = stockRepository.findById(id);
        if (stock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        try {
            stockRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Stock>> findAllStocks(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new ResponseEntity<>(stockRepository.findAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>(stockRepository.findByAnyFieldContaining(str), HttpStatus.OK);

    }
}
