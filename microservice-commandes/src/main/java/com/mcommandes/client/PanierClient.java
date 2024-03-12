package com.mcommandes.client;

import com.mcommandes.model.Panier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mpanier")
@Component
public interface PanierClient {
    @GetMapping("/api/v1/mpanier/paniers/{id}")
    Panier getPanierById(@PathVariable("id") int id) ;
    @DeleteMapping(value = "/api/v1/mpanier/paniers/{id}")
    public ResponseEntity<Void> supprimerPanier(@PathVariable int id);
}
