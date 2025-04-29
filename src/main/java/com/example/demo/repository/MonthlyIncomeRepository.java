package com.example.demo.repository;

import com.example.demo.models.MonthlyIncome;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MonthlyIncomeRepository extends MongoRepository<MonthlyIncome, String> {
    List<MonthlyIncome> findByMonth(String month);

}
