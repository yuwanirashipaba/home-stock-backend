package com.example.demo.service;

import com.example.demo.models.Budget;
import com.example.demo.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {
// Test comment
    @Autowired
    private BudgetRepository budgetRepo;

    public Budget addOrUpdateBudget(Budget budget) {
        Optional<Budget> existing = budgetRepo.findByYearAndMonth(budget.getYear(), budget.getMonth());
        existing.ifPresent(b -> budget.setId(b.getId())); // If exists, replace ID to update
        return budgetRepo.save(budget);
    }

    public Optional<Budget> getMonthlyBudget(int year, int month) {
        return budgetRepo.findByYearAndMonth(year, month);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepo.findAll();
    }

}
