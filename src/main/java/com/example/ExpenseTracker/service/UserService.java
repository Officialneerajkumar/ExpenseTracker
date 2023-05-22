package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.dao.AuthTokenRepo;
import com.example.ExpenseTracker.dao.UserRepo;
import com.example.ExpenseTracker.dto.SignInInput;
import com.example.ExpenseTracker.dto.SignInOutput;
import com.example.ExpenseTracker.dto.SignUpInput;
import com.example.ExpenseTracker.dto.SignUpOutput;
import com.example.ExpenseTracker.model.AuthToken;
import com.example.ExpenseTracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthTokenRepo authTokenRepo;
    public SignUpOutput SignUp(SignUpInput signUpDto) {
        //check if user exists or not based on email
        User user = userRepo.findFirstByEmail(signUpDto.getEmail());

        if(user != null)
        {
            throw new IllegalStateException("User already exists!!!!...Please sign in instead");
        }

        //encryption
        String encryptedPassword = null;
        try {
            encryptedPassword = encryptPassword(signUpDto.getPassword());//takes  a string and encrypts it...
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //save the user
        String randomUserId = UUID.randomUUID().toString();
        user = new User(signUpDto.getFirstName(),
                signUpDto.getLastName(),signUpDto.getEmail(),
                encryptedPassword, signUpDto.getPhoneNumber());
        user.setUserId(randomUserId);

        userRepo.save(user);

        //token creation and saving

//        AuthToken token = new AuthToken(user);
//
//        authService.saveToken(token);

        return new SignUpOutput("User registered");

    }
    private String encryptPassword(String userPassword) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(userPassword.getBytes());
        byte[] digested =  md5.digest();

        String hash = DatatypeConverter.printHexBinary(digested);
        return hash;
    }

    public SignInOutput signIn(SignInInput signInDto) {
        //check if user exists or not based on email
        User user = userRepo.findFirstByEmail(signInDto.getEmail());

        if(user == null)
        {
            throw new IllegalStateException("User invalid!!!!...sign up instead");
        }

        String encryptedPassword = null;

        try {
            encryptedPassword = encryptPassword(signInDto.getPassword());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        //match it with database encrypted password

        boolean isPasswordValid = encryptedPassword.equals(user.getPassword());

        if(!isPasswordValid)
        {
            throw new IllegalStateException("User invalid!!!!...sign up instead");
        }

        AuthToken token = new AuthToken(user);

        authService.saveToken(token);

        //set up output response

        return new SignInOutput(token.getToken());

    }

    public ResponseEntity<User> getUser(String token) {
        User user = authTokenRepo.findFirstByToken(token).getUser();
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }
}
