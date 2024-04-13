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

    // Getter and setter for paniers
}