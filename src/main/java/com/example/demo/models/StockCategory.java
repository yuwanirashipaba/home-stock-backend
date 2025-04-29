package com.example.demo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "category")
@Data
public class StockCategory {
    @MongoId(value = FieldType.STRING)
    private String id;
    private String name;
}
