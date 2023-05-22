package com.example.ExpenseTracker.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_expense")
public class Expense {
    //title,description, price, date, time,userId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "expense_id")
    private Integer expenseId;
    @Column(name = "title")
    private String title;
    @Column(name = "price")
    private Integer price;
    @Column(name = "date")
    private String date;
    @Column(name = "time")
    private String time;
    @OneToOne()
    @JoinColumn(name = "fk_user_id")
    private User user;

}
