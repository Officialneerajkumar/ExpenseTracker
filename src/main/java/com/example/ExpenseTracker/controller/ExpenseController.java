package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.service.ExpenseService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add-expense")
    public ResponseEntity<String> addExpense(@RequestBody Expense expense,@RequestParam String token){
        return expenseService.addExpense(expense,token);
    }
    @GetMapping("/get-all-expense")
    public ResponseEntity<List<Expense>> getAllExpense(){
        return expenseService.getAllExpense();
    }
    @PutMapping("/update-expense")
    public ResponseEntity<String> updateExpense(@Valid Expense expenseData,@RequestParam Integer expenseIdToBeUpdate,@RequestParam String token){
        return expenseService.updateExpense(expenseData,expenseIdToBeUpdate,token);
    }
    @DeleteMapping("/delete-expense")
    public ResponseEntity<String> deleteExpense(@RequestParam Integer expenseId,@RequestParam String token){
        return expenseService.deleteExpense(expenseId,token);
    }
    @GetMapping("/get-all-expense-by-date")
    public ResponseEntity<List<Expense>> getExpenseByDate(@RequestParam LocalDate date){
        return expenseService.getExpenseByDate(date);
    }

}
