package com.mproduits.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id ;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname ;

    @Column(name = "email")
    private String email ;

    @Column(name = "password")
    private String password ;


    @Column(name = "phone")
    private String phone;

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

    public User(Long id) {
        this.id = id;
    }

}
