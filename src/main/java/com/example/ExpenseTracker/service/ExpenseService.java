package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dao.ExpenseRepo;
import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private AuthService authService;

    public ResponseEntity<String> addExpense(Expense expense,String token) {
        Integer savedExpenseId = null;
        HttpStatus status = null;
        if(authService.authenticateUser(token)){
            LocalDate currDate = LocalDate.now();
            LocalTime currTime = LocalTime.now();
            expense.setDate(currDate);
            expense.setTime(currTime);
            savedExpenseId = expenseRepo.save(expense).getExpenseId();
            status=HttpStatus.CREATED;
        }else{
            status=HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>("Expense added !!! with Id "+savedExpenseId,HttpStatus.CREATED);
    }

    public ResponseEntity<List<Expense>> getAllExpense() {
        List<Expense> expenseList = new ArrayList<>();
         expenseList = expenseRepo.findAll();
         return new ResponseEntity<>(expenseList,HttpStatus.OK);
    }

    public ResponseEntity<String> updateExpense(Expense expenseData,Integer expenseIdToBeUpdate,String token) {
        String message = null;
        HttpStatus status = null;
        Expense savedExpense = expenseRepo.findById(expenseIdToBeUpdate).get();
        if(authService.authenticateUser(token) && savedExpense!=null){
            Expense newDataExpense = null;
            newDataExpense.setExpenseId(expenseIdToBeUpdate);
            newDataExpense.getUser().setUserId(expenseData.getUser().getUserId());
            newDataExpense.setTitle(expenseData.getTitle());
            newDataExpense.setPrice(expenseData.getPrice());
            LocalDate currDate = LocalDate.now();
            LocalTime currTime = LocalTime.now();
            newDataExpense.setDate(currDate);
            newDataExpense.setTime(currTime);
            expenseRepo.save(newDataExpense);
            message = "Expense Updated !!!";
            status=HttpStatus.CREATED;
        }else{
            message = "Invalid User !! or Invalid expenseIdToBeUpdate !!";
            status=HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(message,status);
    }

    public ResponseEntity<String> deleteExpense(Integer expenseId, String token) {
        String message = null;
        HttpStatus status = null;
        Expense savedExpense = expenseRepo.findById(expenseId).get();
        if(authService.authenticateUser(token) && savedExpense!=null){
            expenseRepo.deleteById(expenseId);
            message = "Expense deleted !!!";
            status=HttpStatus.OK;
        }else{
            message = "Invalid User !! or Invalid expenseId !!";
            status=HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(message,status);
    }
    public ResponseEntity<List<Expense>> getExpenseByDate(LocalDate date) {
        List<Expense> expenseList = expenseRepo.findByDate(date);
        return new ResponseEntity<>(expenseList,HttpStatus.FOUND);
    }

    public List<Expense> getExpensesByDate(LocalDate parsedDate) {
        return expenseRepo.findByDate(parsedDate);
    }

    public Map<LocalDate, Double> getMonthlyReport(User user, LocalDate monthDate) {
        LocalDate startOfMonth = monthDate.withDayOfMonth(1);
        LocalDate endOfMonth = monthDate.withDayOfMonth(monthDate.lengthOfMonth());
        Map<LocalDate, Double> expenseMap = new HashMap<>();
        List<Expense> expenses = expenseRepo.findByUserAndDateBetween(user, startOfMonth, endOfMonth);

        for (Expense expense : expenses) {
            LocalDate date = expense.getDate();
            double price = expense.getPrice();
            if (expenseMap.containsKey(date)) {
                double currentTotal = expenseMap.get(date);
                expenseMap.put(date, currentTotal + price);
            } else {
                expenseMap.put(date, price);
            }
        }
        return expenseMap;

    }

    public Double getTotalExpenditure(User user, LocalDate monthDate) {
        LocalDate startOfMonth = monthDate.withDayOfMonth(1);
        LocalDate endOfMonth = monthDate.withDayOfMonth(monthDate.lengthOfMonth());
        List<Expense> expenses = expenseRepo.findByUserAndDateBetween(user, startOfMonth, endOfMonth);

        double totalExpenditure = 0.0;
        for (Expense expense : expenses) {
            double price = expense.getPrice();
            totalExpenditure += price;
        }
        return totalExpenditure;
    }
}
