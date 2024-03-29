package com.mcommandes.web.controller;

import com.mcommandes.client.PanierClient;
import com.mcommandes.client.UserClient;
import com.mcommandes.dao.CommandesDao;
import com.mcommandes.model.Commande;
import com.mcommandes.model.Panier;
import com.mcommandes.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mcommandes")
public class CommandeController {

    private final PanierClient panierClient;
    private final CommandesDao commandesDao;
    private final UserClient userClient;

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from mcommandes service!");
    }

    @PostMapping("/validercommande/{id}")
    public ResponseEntity<Commande> validerCommande(@PathVariable int id, @RequestParam("userId") Long userId) {
        Panier panier = panierClient.getPanierById(id);
        com.mcommandes.model.User user = userClient.getUserById(userId);
        if (panier == null) {
            return ResponseEntity.notFound().build();
        }

        Commande commande = createCommandeFromPanier(panier, user );
        Commande savedCommande = commandesDao.save(commande);

        panierClient.supprimerPanier(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCommande);
    }





    @GetMapping("/commandes")
    public List<Commande> listeDesCommandes() {
        return commandesDao.findAll();
    }

    @GetMapping("/commandes/{id}")
    public ResponseEntity<Commande> recupererUneCommande(@PathVariable int id) {
        Optional<Commande> commande = commandesDao.findById(id);
        return commande.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/commandes/{id}")
    public ResponseEntity<Void> supprimerCommande(@PathVariable int id) {
        Optional<Commande> commande = commandesDao.findById(id);
        if (commande.isPresent()) {
            commandesDao.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Method to create a Commande from a Panier
    public Commande createCommandeFromPanier(Panier panier, User user) {
        Commande commande = new Commande();
        commande.setProducts(panier.getProducts());
        commande.setDateCommande(new Date());
        commande.setQuantite(panier.getQuantite());
        // Set the user for the Commande
        commande.setUser(user); // Assuming there's a setUser method in your Commande entity that accepts a User object
        // Set other properties as needed
        return commande;
    }

}
