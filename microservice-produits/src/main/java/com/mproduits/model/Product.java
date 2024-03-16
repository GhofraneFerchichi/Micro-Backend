package com.mproduits.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private int id;

    private String titre;

    private String description;

    private String image;

    private Double prix;

    private int quantite;



   @ManyToMany
    @JoinTable(
            name = "panier_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "panier_id"))
    private Set<Panier> paniers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
