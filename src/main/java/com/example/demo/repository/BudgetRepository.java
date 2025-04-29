package com.example.demo.repository;

import com.example.demo.models.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    Optional<Budget> findByYearAndMonth(int year, int month);
}
