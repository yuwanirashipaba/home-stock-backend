package com.example.demo.controller;

import com.example.demo.models.Budget;
import com.example.demo.service.BudgetService;
import com.example.demo.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/set")
    public Budget addOrUpdateBudget(@RequestBody Budget budget) {
        return budgetService.addOrUpdateBudget(budget);
    }

    @GetMapping("/status/{year}/{month}")
    public Map<String, Object> getBudgetStatus(@PathVariable int year, @PathVariable int month) {
        Map<String, Object> response = new HashMap<>();

        Optional<Budget> optionalBudget = budgetService.getMonthlyBudget(year, month);
        double expenses = expenseService.getTotalExpensesForMonth(year, month);

        if (optionalBudget.isPresent()) {
            Budget budget = optionalBudget.get();
            double remaining = budget.getAmount() - expenses;

            response.put("budget", budget.getAmount());
            response.put("expenses", expenses);
            response.put("remaining", remaining);
            response.put("overLimit", remaining < 0);
        } else {
            response.put("error", "No budget set for this month");
        }

        return response;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentMonthBudget() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        Optional<Budget> budget = budgetService.getMonthlyBudget(year, month);

        if (budget.isPresent()) {
            return ResponseEntity.ok(budget.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No budget set for current month.");
        }
    }

    @GetMapping("/all")
    public List<Budget> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

}
