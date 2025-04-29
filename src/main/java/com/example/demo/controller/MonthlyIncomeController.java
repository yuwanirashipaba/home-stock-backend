package com.example.demo.controller;


import com.example.demo.models.MonthlyIncome;
import com.example.demo.service.MonthlyIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monthly-income")
public class MonthlyIncomeController {

    @Autowired
    private MonthlyIncomeService incomeService;

    // Add income (One-time or Monthly)
    @PostMapping("/add")
    public MonthlyIncome addIncome(@RequestBody MonthlyIncome income) {
        return incomeService.addIncome(income);
    }

    // Update income
    @PutMapping("/update/{id}")
    public MonthlyIncome updateIncome(@PathVariable String id, @RequestBody MonthlyIncome newIncome) {
        return incomeService.updateIncome(id, newIncome);
    }

    // Delete income
    @DeleteMapping("/delete/{id}")
    public void deleteIncome(@PathVariable String id) {
        incomeService.deleteIncome(id);
    }

    // Get all income for a month
    @GetMapping("/{year}/{month}")
    public List<MonthlyIncome> getIncomeForMonth(@PathVariable int year, @PathVariable int month) {
        return incomeService.getIncomeForMonth(YearMonth.of(year, month));
    }

    // Get total income for a month
    @GetMapping("/total/{year}/{month}")
    public double getMonthlyIncomeTotal(@PathVariable int year, @PathVariable int month) {
        return incomeService.getMonthlyIncomeTotal(year, month);
    }

    // Get balance (Income - Expenses)
    @GetMapping("/balance/{year}/{month}")
    public double getMonthlyBalance(@PathVariable int year, @PathVariable int month) {
        return incomeService.getMonthlyBalance(year, month);
    }

    @GetMapping("/yearly/{year}")
    public Map<Integer, Double> getYearlyIncome(@PathVariable int year) {
        return incomeService.getYearlyIncome(year);
    }
}

