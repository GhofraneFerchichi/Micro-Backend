package com.mcommandes.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public Product(int id, String product1, String description1, double v) {
    }

    // Getter and setter for paniers
}