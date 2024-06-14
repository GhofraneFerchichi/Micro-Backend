package com.example.usermicroservice.controller;

import com.example.usermicroservice.entity.User;
import com.example.usermicroservice.service.AuthenticationService;
import com.example.usermicroservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/sign-in")
    public ResponseEntity<User> signIn(@RequestBody User user){
        User Suser = authenticationService.SignInAndReturnJWT(user);
        return new ResponseEntity<>(Suser, HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user){
        if (userService.findByEmail(user.getEmail()).isPresent()){

            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
            
        User Suser = userService.saveUser(user);
        return new ResponseEntity<>(Suser, HttpStatus.CREATED);
    }




}
