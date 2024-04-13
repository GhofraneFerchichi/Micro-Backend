package com.example.usermicroservice.controller;

import com.example.usermicroservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/api/user/verify")
    public String verifyUser(@RequestParam("code") String code) {
        if (userService.verify(code)) {
            return "redirect:/verify_success"; // Redirect to success page
        } else {
            return "redirect:/verify_fail"; // Redirect to failure page
        }
    }

    // Define endpoints for success and failure pages
    @GetMapping("/verify_success")
    public String verificationSuccess() {
        return "Verification Successful";
    }

    @GetMapping("/verify_fail")
    public String verificationFailure() {
        return "Verification Failed";
    }
}
