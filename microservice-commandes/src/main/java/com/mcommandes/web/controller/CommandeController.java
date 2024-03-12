package com.mcommandes.web.controller;

import com.mcommandes.client.PanierClient;
import com.mcommandes.dao.CommandesDao;
import com.mcommandes.model.Commande;
import com.mcommandes.model.Panier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mcommandes")

public class CommandeController {

    private final PanierClient panierClient;
    private final CommandesDao commandesDao;

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from mcommandes service!");
    }
    @PostMapping("/validercommande/{panierId}")
    public ResponseEntity<Commande> validerCommande(@PathVariable int panierId) {
        // Retrieve panier information using PanierClient
        Panier panier = panierClient.getPanierById(panierId);

        // Validate the panier (add your validation logic here)

        // Create a new command based on the panier information
        Commande commande = createCommandeFromPanier(panier);

        // Save the command using CommandesDao
        Commande savedCommande = commandesDao.save(commande);

        // Now that the command is saved, remove the panier
        panierClient.supprimerPanier(panierId); // Assuming you have a method to delete a panier by its ID

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCommande);
    }

    // Method to create a Commande from a Panier
    private Commande createCommandeFromPanier(Panier panier) {
        Commande commande = new Commande();
        commande.setProducts(panier.getProducts()); // Assuming Commande has a list of products
        commande.setDateCommande(new Date());
        commande.setQuantite(panier.getQuantite());
        // Set other properties as needed
        return commande;
    }
}