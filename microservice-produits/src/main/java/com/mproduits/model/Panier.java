package com.mproduits.model;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.ManyToMany;

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

}