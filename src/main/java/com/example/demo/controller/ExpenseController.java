package com.example.demo.controller;

import com.example.demo.models.Expense;
import com.example.demo.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")  // Allow frontend (e.g., Postman, React) to access API
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // POST: Add Expense
    @PostMapping("/add")
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    // GET: All Expenses
    @GetMapping("/all")
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    // DELETE: Delete Expense by ID
    @DeleteMapping("/delete/{id}")
    public String deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return "Expense with ID " + id + " deleted successfully.";
    }

    // PUT: Update Expense
    @PutMapping("/update/{id}")
    public Expense updateExpense(@PathVariable String id, @RequestBody Expense updatedExpense) {
        return expenseService.updateExpense(id, updatedExpense);
    }

    @GetMapping("/yearly/{year}")
    public Map<Integer, Double> getYearlyExpenses(@PathVariable int year) {
        return expenseService.getYearlyExpenses(year);
    }

}