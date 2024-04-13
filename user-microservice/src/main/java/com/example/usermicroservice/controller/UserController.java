package com.example.usermicroservice.controller;

import com.example.usermicroservice.entity.User;
import com.example.usermicroservice.repository.UserRepository;
import com.example.usermicroservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {


    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private  UserRepository userRepository;

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        String a = siteURL.replace(request.getServletPath(), "");
        System.out.println(a);
        return siteURL.replace(request.getServletPath(), "");
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> UpdateUser(@RequestBody User user) throws Exception {
        User Suser = userService.update(user);
        return new ResponseEntity<>(Suser, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<?> processRegister(@RequestBody User user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        if (userService.findByEmail(user.getEmail()).isPresent()){

            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.register(user, getSiteURL(request));
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
    @GetMapping
    public ResponseEntity<User> getUserById(@RequestParam Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/v1/current-user")
    public User getCurrentUser() {
        // Retrieve the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // Return null or handle unauthenticated user
            return null;
        }

        // Assuming the user details are stored in a UserDetails object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Assuming you have a service to retrieve user details by username or ID
        User currentUser = userService.findByUsername(userDetails.getUsername());

        return currentUser;
    }


}
