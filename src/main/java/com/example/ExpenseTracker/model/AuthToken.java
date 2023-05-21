package com.example.ExpenseTracker.model;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_auth_token")
public class AuthToken {
    @Id
    @Column(name = "token_id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String tokenId;
    @Column(name = "token")
    private String token;
    @Column(name = "token_create_date")
    private LocalDate tokenCreationDate;

    @OneToOne
    @JoinColumn(nullable = false,name = "fk_user_ID")
    private User user;

    public AuthToken(User user) {
        this.user = user;
        this.tokenCreationDate = LocalDate.now();
        this.token = UUID.randomUUID().toString();
    }
}
