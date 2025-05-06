package com.example.demo.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StockRequest {
    private String name;
    private double price;
    private int quantity;
    private String categoryId;
    private String categoryName;
    private String purchasedDate;
    private String expiredDate;
    private MultipartFile image;
}
