package com.example.ExpenseTracker.dao;

import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense,Integer> {
    List<Expense> findByDate(LocalDate date);

    List<Expense> findByUserAndDateBetween(User user, LocalDate startOfMonth, LocalDate endOfMonth);
}
