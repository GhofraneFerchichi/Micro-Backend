package com.mcommandes.model;


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
    @ManyToMany
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
