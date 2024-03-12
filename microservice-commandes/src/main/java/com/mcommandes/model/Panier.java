package com.mcommandes.model;

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
    @GeneratedValue
    private int id;

    private Integer quantite;

    private Double prixTotale;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "panier_product",
            joinColumns = @JoinColumn(name = "panier_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>(); // Collection to hold products


    // Other fields and methods...
}
