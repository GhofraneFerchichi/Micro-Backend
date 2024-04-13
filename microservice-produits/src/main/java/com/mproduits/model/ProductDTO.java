package com.mproduits.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private int id;
    private String titre;
    private String description;
    private double prix;
    private String imageBase64; // Base64-encoded image string

    // Getters and setters...
}
