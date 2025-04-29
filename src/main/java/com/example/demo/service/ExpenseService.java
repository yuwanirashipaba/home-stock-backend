package com.example.demo.service;

import com.example.demo.repository.ExpenseRepository;
import com.example.demo.models.Expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Get total expenses for a specific month
    public double getTotalExpensesForMonth(int year, int month) {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                .filter(e -> e.getDate().getYear() == year && e.getDate().getMonthValue() == month)
                .mapToDouble(Expense::getPrice)
                .sum();
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }

    public Expense updateExpense(String id, Expense updatedExpense) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findById(id);

        if (existingExpenseOpt.isPresent()) {
            Expense existingExpense = existingExpenseOpt.get();
            existingExpense.setName(updatedExpense.getName());
            existingExpense.setPrice(updatedExpense.getPrice());
            existingExpense.setDate(updatedExpense.getDate());
            existingExpense.setCategory(updatedExpense.getCategory());

            return expenseRepository.save(existingExpense);
        } else {
            throw new RuntimeException("Expense not found with ID: " + id);
        }
    }

    public Map<Integer, Double> getYearlyExpenses(int year) {
        List<Expense> allExpenses = expenseRepository.findAll();
        Map<Integer, Double> monthlyTotals = new HashMap<>();

        for (Expense expense : allExpenses) {
            LocalDate date = expense.getDate();
            if (date.getYear() == year) {
                int month = date.getMonthValue();
                double amount = expense.getPrice();

                monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + amount);
            }
        }

        return monthlyTotals;
    }


}