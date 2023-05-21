package com.example.ExpenseTracker.model;


import javax.validation.constraints.NotBlank;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_expense")
public class Expense {
    //title,description, price, date, time,userId
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Integer expenseId;
    @NotBlank
    @Column(name = "title")
    private String title;
    @NotBlank
    @Column(name = "price")
    private Integer price;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "time")
    private LocalTime time;
    @OneToOne()
    @JoinColumn(name = "fk_user_id")
    private User user;

}
