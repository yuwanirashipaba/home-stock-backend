package com.example.demo.service;

import com.example.demo.models.MonthlyIncome;
import com.example.demo.repository.MonthlyIncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MonthlyIncomeService {

    @Autowired
    private MonthlyIncomeRepository incomeRepository;

    @Autowired
    private ExpenseService expenseService; // To calculate balance

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    // Add income (One-time or Monthly)
    public MonthlyIncome addIncome(MonthlyIncome income) {
        return incomeRepository.save(income);
    }

    // Update existing income
    public MonthlyIncome updateIncome(String id, MonthlyIncome newIncome) {
        Optional<MonthlyIncome> existingIncome = incomeRepository.findById(id);
        if (existingIncome.isPresent()) {
            MonthlyIncome updatedIncome = existingIncome.get();
            updatedIncome.setAmount(newIncome.getAmount());
            updatedIncome.setDescription(newIncome.getDescription());
            updatedIncome.setIncomeType(newIncome.getIncomeType());
            return incomeRepository.save(updatedIncome);
        }
        throw new RuntimeException("Income entry not found!");
    }

    // Delete income
    public void deleteIncome(String id) {
        incomeRepository.deleteById(id);
    }

    // Get income for a specific month
    public List<MonthlyIncome> getIncomeForMonth(YearMonth month) {
        String monthStr = month.format(formatter);
        return incomeRepository.findByMonth(monthStr);
    }

    // Get total income for a specific month
    public double getMonthlyIncomeTotal(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        String monthStr = yearMonth.format(formatter);
        List<MonthlyIncome> incomes = incomeRepository.findByMonth(monthStr);
        return incomes.stream().mapToDouble(MonthlyIncome::getAmount).sum();
    }

    // Auto-renew Monthly Income (Not One-time Income)
    public void autoRenewIncomeForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth previousMonth = currentMonth.minusMonths(1);

        String prevMonthStr = previousMonth.format(formatter);
        String currMonthStr = currentMonth.format(formatter);

        List<MonthlyIncome> prevIncomes = incomeRepository.findByMonth(prevMonthStr);

        for (MonthlyIncome prevIncome : prevIncomes) {
            if ("MONTHLY".equals(prevIncome.getIncomeType())) { // Renew only Monthly Income
                MonthlyIncome renewedIncome = new MonthlyIncome(
                        prevIncome.getAmount(),
                        currentMonth,
                        prevIncome.getDescription(),
                        "MONTHLY"
                );
                incomeRepository.save(renewedIncome);
            }
        }
    }

    // Get Balance (Income - Expenses)
    public double getMonthlyBalance(int year, int month) {
        double totalIncome = getMonthlyIncomeTotal(year, month);
        double totalExpenses = expenseService.getTotalExpensesForMonth(year, month);
        return totalIncome - totalExpenses;
    }

    public Map<Integer, Double> getYearlyIncome(int year) {
        Map<Integer, Double> yearlyIncome = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            String monthStr = yearMonth.format(formatter);
            List<MonthlyIncome> incomes = incomeRepository.findByMonth(monthStr);

            double total = incomes.stream().mapToDouble(MonthlyIncome::getAmount).sum();
            yearlyIncome.put(month, total);
        }

        return yearlyIncome;
    }

}
