package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.dao.UserRepo;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.service.AuthService;
import com.example.ExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepo userRepo;

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

    @GetMapping("/date")
    public ResponseEntity<List<Expense>> getExpensesByDate(@RequestParam String email , @RequestParam String token, @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        HttpStatus status;
        List<Expense> expenses = null;
        if(authService.authenticate(email,token)) {
            User user  = userRepo.findFirstByEmail(email);
            if(user != null){
                expenses = expenseService.getExpensesByDate(parsedDate);;
                status = HttpStatus.OK;
            }else{
                throw new IllegalStateException("Invalid User..!!");
            }
        }else {
            status = HttpStatus.FORBIDDEN;
        }
        return new ResponseEntity<>(expenses , status);
    }


    @GetMapping("/get-monthly-report")
    public ResponseEntity<Map<LocalDate, Double>> getMonthlyReport(
            @RequestParam String email ,
            @RequestParam String token,
            @RequestParam int year,
            @RequestParam Month month
    ) {
        Map<LocalDate, Double> report = null;
        HttpStatus status;

        if(authService.authenticate(email,token)) {
            User user  = userRepo.findFirstByEmail(email);
            LocalDate monthDate = LocalDate.of(year, month.ordinal(), 1);
            if(user != null){
                report = expenseService.getMonthlyReport(user, monthDate);
                status = HttpStatus.OK;
            }else{
                throw new IllegalStateException("Invalid User..!!");
            }
        }else {
            status = HttpStatus.FORBIDDEN;
        }
        return new ResponseEntity<>(report , status);
    }

    @GetMapping("/totalExpenditure")
    public ResponseEntity<Double> getTotalExpenditure(
            @RequestParam String email ,
            @RequestParam String token,
            @RequestParam("year") int year,
            @RequestParam("month") Month month
    ) {
        Double totalExpenditure = null;
        HttpStatus status;
        if (authService.authenticate(email, token)) {
            User user = userRepo.findFirstByEmail(email);
            LocalDate monthDate = LocalDate.of(year, month.ordinal(), 1);
            if (user != null) {
                totalExpenditure = expenseService.getTotalExpenditure(user, monthDate);
                status = HttpStatus.OK;
            } else {
                throw new IllegalStateException("Invalid User..!!");
            }
        } else {
            status = HttpStatus.FORBIDDEN;
        }

        return new ResponseEntity<>(totalExpenditure,status);
    }


}
