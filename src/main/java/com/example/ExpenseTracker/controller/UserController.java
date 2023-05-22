package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.dto.SignInInput;
import com.example.ExpenseTracker.dto.SignInOutput;
import com.example.ExpenseTracker.dto.SignUpInput;
import com.example.ExpenseTracker.dto.SignUpOutput;
import com.example.ExpenseTracker.model.User;
import com.example.ExpenseTracker.service.AuthService;
import com.example.ExpenseTracker.service.UserService;
import com.sun.java.accessibility.util.GUIInitializedListener;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpOutput> SignUp(@Valid @RequestBody SignUpInput signUpDto){
        SignUpOutput response = userService.SignUp(signUpDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/sign-in")
    public ResponseEntity<SignInOutput> signIn(@RequestBody SignInInput signInDto){
        SignInOutput  response = userService.signIn(signInDto);
        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }
    @DeleteMapping("/signout")
    public ResponseEntity<String> signOut(@RequestParam String email , @RequestParam String token){
        HttpStatus status;
        String msg=null;

        if(authService.authenticate(email,token))
        {
            authService.deleteToken(token);
            msg = "Signout Successful";
            status = HttpStatus.OK;

        }
        else
        {
            msg = "Invalid User";
            status = HttpStatus.FORBIDDEN;
        }

        return new ResponseEntity<String>(msg , status);
    }
    @GetMapping("/get-user-by-token")
    public ResponseEntity<User> getByToken(@RequestParam String token){
        return userService.getUser(token);
    }
}
