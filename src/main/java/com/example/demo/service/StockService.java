package com.example.demo.service;

import com.example.demo.dtos.StockRequest;
import com.example.demo.dtos.StocksFilter;
import com.example.demo.models.Stock;
import com.example.demo.models.StockCategory;
import com.example.demo.repository.StockCategoryRepository;
import com.example.demo.repository.StockRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockCategoryRepository stockCategoryRepository;
    private final ExcelService excelService;

    // TODO: change image directory
    private static final String IMAGE_DIRECTORY = "C:\\Users\\ASUS\\Downloads\\Archive\\ITPM-Group-Project\\public\\products\\";

    public StockService(StockRepository stockRepository, StockCategoryRepository stockCategoryRepository, ExcelService excelService) {
        this.stockRepository = stockRepository;
        this.stockCategoryRepository = stockCategoryRepository;
        this.excelService = excelService;
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
            stock.setPrice(stockRequest.getPrice());
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
            stock.setPrice(updatedStock.getPrice());
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

    public ResponseEntity<List<Stock>> findAllStocks() {
        return new ResponseEntity<>(stockRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Stock>> findAllStocksByFilter(StocksFilter stocksFilter) {
        if (stocksFilter.getSubstr() == null || stocksFilter.getSubstr().trim().isEmpty()) {
            stocksFilter.setSubstr("");
        }
        if (stocksFilter.getAvailability() == null || stocksFilter.getAvailability().equals("all")) {
            return new ResponseEntity<>(stockRepository.findByAnyFieldContaining(stocksFilter.getSubstr()), HttpStatus.OK);
        } else if (stocksFilter.getAvailability().equals("available")) {
            return new ResponseEntity<>(stockRepository.findByAnyFieldContainingAndInStock(stocksFilter.getSubstr()), HttpStatus.OK);
        } else if (stocksFilter.getAvailability().equals("outOfStock")) {
            return new ResponseEntity<>(stockRepository.findByAnyFieldContainingAndOutOfStock(stocksFilter.getSubstr()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Stock>> findAvailableStocks() {
        return new ResponseEntity<>(stockRepository.findByQuantityIsGreaterThan(0), HttpStatus.OK);
    }

    public ResponseEntity<List<Stock>> findOutOfStockItems() {
        return new ResponseEntity<>(stockRepository.findByQuantityEquals(0), HttpStatus.OK);
    }

    public ResponseEntity<byte[]> generateStocksReport(String availability) {
        List<Stock> stocks;
        if (availability.equals("all")) {
            stocks = stockRepository.findAll();
        } else if (availability.equals("available")) {
            stocks = stockRepository.findByQuantityIsGreaterThan(0);
        } else {
            stocks = stockRepository.findByQuantityEquals(0);
        }
        try {
            byte[] excelBytes = excelService.generateStockReport(stocks);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String fileName = availability + "_stocks_report_" + LocalDateTime.now().format(formatter) + ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't generate Stock Report");
        }
    }

    public Map<String, Integer> getCategoryStockCounts() {
        List<StockRepository.CategoryCount> categoryCounts = stockRepository.getCategoryCounts();
        return categoryCounts.stream()
                .collect(Collectors.toMap(
                        StockRepository.CategoryCount::getCategoryName,
                        StockRepository.CategoryCount::getCount
                ));
    }

    public Map<String, Integer> getCategoryStockQuantities() {
        List<StockRepository.CategoryQuantity> categoryQuantities = stockRepository.getCategoryQuantities();
        return categoryQuantities.stream()
                .collect(Collectors.toMap(
                        StockRepository.CategoryQuantity::getCategoryName,
                        StockRepository.CategoryQuantity::getTotalQuantity
                ));
    }

}
