package com.example.mpanier.model;
import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Panier {

    @Id
    private int id;

    private Integer quantite;
    private Double prixTotale;

    @JsonIgnore
    @ManyToMany
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
