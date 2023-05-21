package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dao.AuthTokenRepo;
import com.example.ExpenseTracker.model.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private AuthTokenRepo authTokenRepo;
    public void saveToken(AuthToken token) {
        String randomTokenId = UUID.randomUUID().toString();
        token.setTokenId(randomTokenId);
        authTokenRepo.save(token);
    }

    public boolean authenticate(String email, String token) {
        if(token==null && email==null){
            return false;
        }

        AuthToken authToken = authTokenRepo.findFirstByToken(token);

        if(authToken==null){
            return false;
        }

        String expectedEmail = authToken.getUser().getEmail();


        return expectedEmail.equals(email);
    }

    public void deleteToken(String token) {
        AuthToken token1 = authTokenRepo.findFirstByToken(token);
        authTokenRepo.deleteByTokeId(token1.getTokenId());
    }

    public boolean authenticateUser(String token) {
        if(token==null){
            return false;
        }

        AuthToken authToken = authTokenRepo.findFirstByToken(token);

        if(authToken==null){
            return false;
        }
        return true;
    }
}
