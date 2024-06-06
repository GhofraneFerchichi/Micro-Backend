package com.mcommandes.client;

import com.mcommandes.model.Panier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mpanier", url = "${spring.config.mpanier-url}")
@Component
public interface PanierClient {
    @GetMapping("/api/v1/mpanier/paniers/{id}")
    ResponseEntity<Panier> getPanierById(@PathVariable("id") int id);

    @GetMapping("/api/v1/mpanier/paniers/{panierId}/user/{userId}")
            ResponseEntity<Panier> getPanierByIdAndUserId(@PathVariable("panierId") int panierId, @PathVariable("userId") int userId);

    @DeleteMapping(value = "/api/v1/mpanier/paniers/{id}")
    ResponseEntity<Void> supprimerPanier(@PathVariable int id);
}