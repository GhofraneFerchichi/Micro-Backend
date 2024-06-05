package com.example.mpanier.model;
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
public class Panier {

    @Id
    private int id=1;

    private Integer quantite;

    private Double prixTotale;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "panier_product"
    )
    private List<Product> products = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @OneToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    public User getUser() {
        return user;
    }

    public void setUser( com.example.mpanier.model.User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


// Constructors, getters, and setters...
}
