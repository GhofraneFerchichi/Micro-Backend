package com.example.mpanier.client;

import com.example.mpanier.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "mproduits", url = "${spring.config.mproduits-url}")
public interface ProductClient {

    @PostMapping("/add/{panierId}/{productId}")
    public void addProductToPanier(@PathVariable int panierId, @PathVariable int productId);

    @GetMapping(value = "/api/v1/mproduits/produits/{id}")
    public Product recupererUnProduit(@PathVariable int id);



    @PutMapping(value = "/api/v1/mproduits/produits/{id}")
    public Product modifierProduit(@PathVariable int id, @RequestBody Product produitModifie);


    }
