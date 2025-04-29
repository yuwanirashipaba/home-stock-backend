package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Document(collection = "monthly_income")
public class MonthlyIncome {

    @Id
    private String id;
    private double amount;
    private String month; // Store as a String (e.g., "2025-03")
    private String description;
    private String incomeType; // "ONE_TIME" or "MONTHLY"

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    // ✅ No-Arg Constructor (Needed for MongoDB Deserialization)
    public MonthlyIncome() {}

    // ✅ Constructor for Creating Income Entries
    public MonthlyIncome(double amount, YearMonth month, String description, String incomeType) {
        this.amount = amount;
        this.month = month.format(formatter); // Convert YearMonth to String before saving
        this.description = description;
        this.incomeType = incomeType;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public YearMonth getMonth() { return YearMonth.parse(month, formatter); } // Convert String to YearMonth
    public void setMonth(YearMonth month) { this.month = month.format(formatter); } // Convert YearMonth to String

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIncomeType() { return incomeType; }
    public void setIncomeType(String incomeType) { this.incomeType = incomeType; }
}
