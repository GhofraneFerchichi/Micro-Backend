package com.mcommandes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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


    @Transient
    private String token;
    private boolean enabled;





}
