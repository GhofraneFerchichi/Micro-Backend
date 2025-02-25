package com.example.usermicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user") // Ensure this matches the actual table name in your database
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id ;

    @NotBlank(message = "Your Firstname is required !!")
    @Column(name = "first_name")
    private String firstname;

    @NotBlank(message = "Your Lastname is required !!")
    @Column(name = "last_name")
    private String lastname ;

    @NotBlank(message = "Your Email is required !!")
    @Column(name = "email")
    private String email ;

    @NotBlank(message = "Your Password is required !!")
    @Column(name = "password")
    private String password ;


    @NotBlank(message = "Your Phone number is required !!")
    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "Your Address is required !!")
    @Column(name = "adresse")
    private String adresse ;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    // Remove this line if 'reset_password' column doesn't exist in your database
    @Column(name = "reset_password")
    private String resetPasswordToken;

    @Transient
    private String token;
    private boolean enabled;
}
