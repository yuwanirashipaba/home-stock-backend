package com.example.demo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "stock")
@Data
public class Stock {
    @MongoId(value = FieldType.STRING)
    private String id;
    private String name;
    private double unitPrice;
    private int quantity;
    private String filepath;
    private String categoryId;
    private String categoryName;
    private String purchasedDate;
    private String expiredDate;
}
