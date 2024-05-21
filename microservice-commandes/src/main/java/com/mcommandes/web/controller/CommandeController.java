package com.mcommandes.web.controller;

import com.mcommandes.client.PanierClient;
import com.mcommandes.client.ProductClient;
import com.mcommandes.client.UserClient;
import com.mcommandes.dao.CommandesDao;
import com.mcommandes.model.Product;
import com.mcommandes.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mcommandes")
public class CommandeController {

    private final PanierClient panierClient;
    private final CommandesDao commandesDao;
    private final UserClient userClient;
    private final ProductClient productClient;

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from mcommandes service!");
    }

    @PostMapping("/validercommande/{id}")
    public ResponseEntity<Commande> validerCommande(@PathVariable int id, @RequestParam("userId") Long userId) {
        // Retrieve panier and user
        Panier panier = panierClient.getPanierById(id);
        com.mcommandes.model.User user = userClient.getUserById(userId);
        if (panier == null || user == null) {
            return ResponseEntity.notFound().build();
        }

        // Create new commande
        Commande commande = new Commande();
        commande.setDateCommande(new Date());
        commande.setQuantite(panier.getQuantite());
        commande.setCommandePayee(false);
        commande.setUser(user);

        // Fetch products from panier
        List<Product> panierProducts = panier.getProducts();
        List<Product> products = new ArrayList<>();
        for (Product panierProduct : panierProducts) {
            ResponseEntity<ProductDTO> responseEntity = productClient.recupererUnProduit(panierProduct.getId());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ProductDTO productDTO = responseEntity.getBody();
                Product product = new Product();
                product.setId(productDTO.getId());
                product.setTitre(productDTO.getTitre());
                product.setDescription(productDTO.getDescription());
                product.setPrix(productDTO.getPrix());
                products.add(product);
            }
        }
        commande.setProducts(products);

        // Calculate prixTotale from the products in the panier
        Double prixTotale = products.stream()
                .mapToDouble(Product::getPrix)
                .sum();
        commande.setPrixTotale(getTotalPriceOfPanier(1));

        // Save commande
        Commande savedCommande = commandesDao.save(commande);

        // Update associations and delete panier
        panier.setCommande(savedCommande);
        panierClient.supprimerPanier(id);

        // Flush changes to ensure persistence
        commandesDao.flush();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCommande);
    }
    @GetMapping("/paniers/{panierId}/totalPrice")
    public double getTotalPriceOfPanier(@PathVariable int panierId) {
        Panier panier = panierClient.getPanierById(panierId);
        if (panier != null) {
            return panier.getPrixTotale();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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
