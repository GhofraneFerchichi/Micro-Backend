package com.example.mpanier.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Commande {
    @Id
    @GeneratedValue

    private int id;

    private Date dateCommande;

    private Integer quantite;

    private Boolean commandePayee;

    private Double PrixTotale;

    @ManyToMany
    @JoinTable(
            name = "commande_product",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "commande")
    private Panier panier;


}