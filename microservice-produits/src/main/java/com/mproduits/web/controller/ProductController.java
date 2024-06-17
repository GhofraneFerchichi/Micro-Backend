package com.mproduits.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mproduits.client.UserClient;
import com.mproduits.dao.ProductDao;
import com.mproduits.model.Panier;
import com.mproduits.model.Product;
import com.mproduits.model.ProductDTO;
import com.mproduits.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/mproduits")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    private final UserClient userFeignClient;

    public ProductController(UserClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @PostMapping(value = "/produits")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> ajouterProduit(@RequestParam("userId") Long userId,
                                                     @RequestParam("file") MultipartFile file,
                                                     @RequestParam("product") String productJson) {
        try {
            Product product = new ObjectMapper().readValue(productJson, Product.class);
            product.setUser(new User(userId));

            // Save the image file
            byte[] imageData = file.getBytes();
            product.setImage(imageData); // Store image data directly as byte array

            Product nouveauProduit = productDao.save(product);
            return new ResponseEntity<>(nouveauProduit, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Get a Product by its ID
// Get a Product by its ID
    @GetMapping(value = "/produits/{id}")
    public ProductDTO recupererUnProduit(@PathVariable int id) {
        // Retrieve the product by ID
        Optional<Product> productOptional = productDao.findById(id);

        // Check if the product exists
        if (productOptional.isPresent()) {
            // Convert the product to a DTO
            Product product = productOptional.get();
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setTitre(product.getTitre());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrix(product.getPrix());

            // Convert image byte array to Base64-encoded string
            productDTO.setImageBase64(Base64.getEncoder().encodeToString(product.getImage()));

            return productDTO;
        } else {
            // Throw a 404 Not Found exception if the product is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    // Get all Products


    // Get a Product by its ID



    @PutMapping(value = "/produits/{id}")
    public Product modifierProduit(@PathVariable int id, @RequestBody Product produitModifie) {
        Optional<Product> optionalProduct = productDao.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.setTitre(produitModifie.getTitre());
            existingProduct.setDescription(produitModifie.getDescription());
            existingProduct.setPrix(produitModifie.getPrix());

            // Update the associated Panier if it's included in the request
            if (produitModifie.getPanier() != null) {
                Panier panier = produitModifie.getPanier();
                panier.setId(existingProduct.getPanier().getId());
                existingProduct.setPanier(panier);
            }

            // Save the modified product
            return productDao.save(existingProduct);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }



    // Delete a Product by its ID
    @DeleteMapping(value = "/produits/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable int id) {
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

    @GetMapping(value = "/produits")
    public List<ProductDTO> listeDesProduits() {
        List<Product> products = productDao.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();

        // Convert products to DTOs
        for (Product product : products) {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setTitre(product.getTitre());
            dto.setDescription(product.getDescription());
            dto.setPrix(product.getPrix());

            // Convert image byte array to Base64-encoded string
            dto.setImageBase64(Base64.getEncoder().encodeToString(product.getImage()));

            productDTOs.add(dto);
        }

        return productDTOs;
    }
    @GetMapping("/products/{productId}/user")
    public ResponseEntity<User> getUserForProduct() {
        // Use the userId parameter as needed
        User user = userFeignClient.getUserById(userFeignClient.getCurrentUser().getId()); // Assuming userFeignClient is properly configured
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}