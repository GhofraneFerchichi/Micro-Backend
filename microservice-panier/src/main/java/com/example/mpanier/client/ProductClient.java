package com.example.mpanier.client;

import com.example.mpanier.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mproduits", url = "${spring.config.mproduits-url}")
public interface ProductClient {

    @PostMapping("/add/{panierId}/{productId}")
    public void addProductToPanier(@PathVariable int panierId, @PathVariable int productId);

    @GetMapping("/api/v1/mproduits/produits/{productId}")
    Product getProduct(@PathVariable int productId);

}
