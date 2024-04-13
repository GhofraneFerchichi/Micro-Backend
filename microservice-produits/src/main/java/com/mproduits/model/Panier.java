package com.mproduits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Panier {

    @Id
    private int id;


    private Integer quantite;

    private Double prixTotale;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "panier_product")
    private List<Product> products = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @OneToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;


    // Constructors, getters, and setters...
}
