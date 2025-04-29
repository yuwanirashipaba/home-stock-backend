package com.example.demo.repository;

import com.example.demo.models.Expense;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    // Optional: You can define custom queries here later
}
