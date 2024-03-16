package com.mproduits.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;

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

    public User(Long id) {
        this.id = id;
    }
    @Transient
    private String token;
    private boolean enabled;





}
