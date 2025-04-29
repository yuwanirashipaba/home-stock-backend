package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "expenses")
public class Expense {

    @Id
    private String id;
    private String name;
    private double price;
    private LocalDate date;
    private String category;

    // Constructors
    public Expense() {
    }

    public Expense(String name, double price, LocalDate date, String category) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.category = category;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return price;
    }
}
