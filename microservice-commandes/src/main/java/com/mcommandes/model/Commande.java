package com.mcommandes.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

}
