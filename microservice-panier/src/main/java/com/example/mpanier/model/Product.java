package com.example.mpanier.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue
    private int id;

    private String titre;

    private String description;

    @Lob
    private byte[] image;

    private Double prix;

    private int quantite;

    @ManyToOne
    @JsonIgnore // Prevent circular reference during serialization
    private Panier panier;

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
