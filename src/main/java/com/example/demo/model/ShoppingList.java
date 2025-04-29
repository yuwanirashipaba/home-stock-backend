package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "shoppinglists")
public class ShoppingList {

    @Id
    private String id;
    private List<String> items;
    private LocalDateTime createdDate;

    // Constructors
    public ShoppingList() {}

    public ShoppingList(List<String> items, LocalDateTime createdDate) {
        this.items = items;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}