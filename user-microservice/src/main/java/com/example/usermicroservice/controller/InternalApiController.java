package com.example.usermicroservice.controller;

import com.example.usermicroservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/make")
public class InternalApiController {

    @Autowired
    private UserServiceImpl userService;

    @PutMapping("/upgrade-admin/{email}")
    public ResponseEntity<?> makeAdmin(@PathVariable String email){
         userService.makeAdmin(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
