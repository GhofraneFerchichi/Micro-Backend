package com.mproduits.web.controller;

import com.mproduits.dao.ProductDao;
import com.mproduits.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mproduits")
public class ProductController {



    @Autowired
    private  ProductDao productDao;


    // Create a new Product
    @PostMapping(value = "/produits")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> ajouterProduit(@RequestBody Product product) {
        Product nouveauProduit = productDao.save(product);
        return new ResponseEntity<>(nouveauProduit, HttpStatus.CREATED);
    }

    // Get all Products
    @GetMapping(value = "/produits")
    public List<Product> listeDesProduits() {
        return productDao.findAll();
    }

    // Get a Product by its ID
    @GetMapping(value = "/produits/{id}")
    public ResponseEntity<Product> recupererUnProduit(@PathVariable int id) {
        Optional<Product> produit = productDao.findById(id);
        return produit.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a Product by its ID
    @PutMapping(value = "/produits/{id}")
    public ResponseEntity<Product> modifierProduit(@PathVariable int id, @RequestBody Product produitModifie) {
        Optional<Product> produit = productDao.findById(id);
        if (produit.isPresent()) {
            Product produitExist = produit.get();
            produitExist.setTitre(produitModifie.getTitre());
            produitExist.setDescription(produitModifie.getDescription());
            produitExist.setImage(produitModifie.getImage());
            produitExist.setPrix(produitModifie.getPrix());
            Product produitMaj = productDao.save(produitExist);
            return new ResponseEntity<>(produitMaj, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a Product by its ID
    @DeleteMapping(value = "/produits/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable int id) {
        Optional<Product> produit = productDao.findById(id);
        if (produit.isPresent()) {
            productDao.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
  /*  @PostMapping("/add/{panierId}/{productId}")
    public void addProductToPanier(@PathVariable int panierId, @PathVariable int productId) {
        Panier panier = panierDao.findById(panierId)
                .orElseThrow(PanierNotFoundException::new);
        Product product = productDao.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        // Add the product to the panier
        panier.getProducts().add(product);

        // Update the quantity and total price
        Integer quantite = panier.getQuantite() != null ? panier.getQuantite() : 0;
        panier.setQuantite(quantite + 1);
        panier.setPrixTotale((panier.getPrixTotale() != null ? panier.getPrixTotale() : 0) + product.getPrix()); // Assuming product has a prix attribute

        // Save the updated panier
        panierDao.save(panier);
    }

    @PostMapping("/remove/{panierId}/{productId}")
    public void removeProductFromPanier(@PathVariable int panierId, @PathVariable int productId) {
        Panier panier = panierDao.findById(panierId)
                .orElseThrow(PanierNotFoundException::new);
        Product product = productDao.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        // Remove the product from the panier
        panier.getProducts().remove(product);
        // Update the quantity and total price
        panier.setQuantite(panier.getQuantite() - 1);
        panier.setPrixTotale(panier.getPrixTotale() - product.getPrix()); // Assuming product has a prix attribute

        // Save the updated panier
        panierDao.save(panier);
    }*/

}
