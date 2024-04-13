package com.mcommandes.client;

import com.mcommandes.model.Product;
import com.mcommandes.model.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mproduits", url = "${spring.config.mproduits-url}")

public interface ProductClient {
    @GetMapping(value = "/api/v1/mproduits/produits/{id}")
    public ResponseEntity<ProductDTO> recupererUnProduit(@PathVariable int id);
}
