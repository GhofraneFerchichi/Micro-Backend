package com.example.mpanier.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullPanierResponse {

    private Integer quantite;

    private Double prixTotale;

    List<Product> products;

}
