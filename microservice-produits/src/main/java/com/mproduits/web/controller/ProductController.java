package com.mproduits.web.controller;

import com.mproduits.client.UserClient;
import com.mproduits.dao.ProductDao;
import com.mproduits.model.Product;
import com.mproduits.model.User;
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
    private ProductDao productDao;

    private final UserClient userFeignClient;

    public ProductController(UserClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    // Create a new Product
    @PostMapping(value = "/produits")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> ajouterProduit(@RequestBody Product product, @RequestParam("userId") Long userId) {
        // Use the userId parameter as needed
        // Assuming user is associated with the product
        product.setUser(new User(userId)); // Assuming User constructor accepts userId
        Product nouveauProduit = productDao.save(product);
        return new ResponseEntity<>(nouveauProduit, HttpStatus.CREATED);
    }

    // Get all Products
    @GetMapping(value = "/produits")
    public List<Product> listeDesProduits(@RequestParam("userId") Long userId) {
        // Use the userId parameter as needed
        // Example: return productDao.findAllByUserId(userId);
        return productDao.findAll();
    }

    // Get a Product by its ID
    @GetMapping(value = "/produits/{id}")
    public Product recupererUnProduit(@PathVariable int id) {
        // Retrieve the product by ID
        Optional<Product> productOptional = productDao.findById(id);

        // Check if the product exists
        if (productOptional.isPresent()) {
            // Return the product if found
            return productOptional.get();
        } else {
            // Return null or throw an exception to handle the case where the product is not found
            return null;
        }
    }


    // Update a Product by its ID
    @PutMapping(value = "/produits/{id}")
    public ResponseEntity<Product> modifierProduit(@PathVariable int id, @RequestBody Product produitModifie, @RequestParam("userId") Long userId) {
        // Use the userId parameter as needed
        // Example: Optional<Product> produit = productDao.findByIdAndUserId(id, userId);
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
    public ResponseEntity<Void> supprimerProduit(@PathVariable int id, @RequestParam("userId") Long userId) {
        // Use the userId parameter as needed
        // Example: productDao.deleteByIdAndUserId(id, userId);
        Optional<Product> produit = productDao.findById(id);
        if (produit.isPresent()) {
            productDao.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get User for a Product by its ID
    @GetMapping("/products/{productId}/user")
    public ResponseEntity<User> getUserForProduct(@PathVariable Long productId, @RequestParam("userId") Long userId) {
        // Use the userId parameter as needed
        // Example: User user = productDao.findUserByProductId(productId);
        User user = userFeignClient.getUserById(userId); // Assuming userFeignClient is properly configured
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
