package com.example.mpanier.web.controller;

import com.example.mpanier.client.ProductClient;
import com.example.mpanier.client.UserClient;
import com.example.mpanier.dao.PanierDao;
import com.example.mpanier.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/mpanier")
@RequiredArgsConstructor
public class PanierController {

    private final PanierDao panierDao;
    private final ProductClient productClient;
    private final UserClient userClient;

    @PostMapping(value = "/paniers")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Panier> ajouterPanier(@RequestParam("userId") Long userId) {
        // Retrieve the user by ID
        User user = userClient.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Panier panier = new Panier();
        panier.setQuantite(0);
        panier.setPrixTotale(0.0);
        panier.setUser(user);

        // Save the panier
        Panier nouveauPanier = panierDao.save(panier);
        return new ResponseEntity<>(nouveauPanier, HttpStatus.CREATED);
    }

    @GetMapping("/paniers/{id}")
    public Optional<Panier> getPanierById(@PathVariable("id") int id) {
        return panierDao.findPanierByUserId(id);
    }

    @GetMapping("/paniers/{panierId}/products")
    public ResponseEntity<List<ProductDTO>> getCartProducts(@PathVariable int panierId) {
        // Retrieve the cart by ID
        Optional<Panier> optionalPanier = panierDao.findById(panierId);
        if (!optionalPanier.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve the products associated with the cart
        Panier panier = optionalPanier.get();
        List<Product> products = panier.getProducts();

        // Convert products to DTOs
        List<ProductDTO> productDTOs = new ArrayList<>();
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

        // Return the list of product DTOs
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping(value = "/paniers")
    public List<Panier> listeDesPaniers(@RequestParam("userId") Long userId) {
        // Retrieve the user by ID
        User user = userClient.getUserById(userId);
        if (user == null) {
            return (List<Panier>) ResponseEntity.notFound().build();
        }

        // Retrieve paniers associated with the user
        return panierDao.findByUser(user);
    }

    @GetMapping(value = "/panier")
    public ResponseEntity<Panier> getPanierByUserId(@RequestParam int userId) {
        // Retrieve the panier by user ID
        Optional<Panier> panierOptional = panierDao.findPanierByUserId(userId);

        // Check if the panier exists
        if (panierOptional.isPresent()) {
            // Return the panier if found
            return ResponseEntity.ok(panierOptional.get());
        } else {
            // Throw an exception or handle the case where the panier is not found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Panier not found");
        }
    }

    @PostMapping("/add/{panierId}/{productId}")
    public ResponseEntity<FullPanierResponse> addProductToPanier(@PathVariable int panierId,
                                                                 @PathVariable int productId,
                                                                 @RequestParam("userId") Long userId) {
        // Retrieve the user by ID
        User user = userClient.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve the panier by ID and associated with the user
        Optional<Panier> optionalPanier = panierDao.findByIdAndUser(panierId, user);
        if (!optionalPanier.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Panier panier = optionalPanier.get();
        Product product = productClient.recupererUnProduit(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        // Modify the panier as needed
        panier.getProducts().add(product);
        panier.setQuantite(panier.getQuantite() + 1);
        panier.setPrixTotale(panier.getPrixTotale() + product.getPrix());
        // Save the modified panier
        Panier updatedPanier = panierDao.save(panier);

        // Create and return the response
        FullPanierResponse fullPanierResponse = new FullPanierResponse();
        fullPanierResponse.setQuantite(updatedPanier.getQuantite());
        fullPanierResponse.setPrixTotale(updatedPanier.getPrixTotale());
        fullPanierResponse.setProducts(updatedPanier.getProducts());

        return ResponseEntity.ok(fullPanierResponse);
    }

    @DeleteMapping("/remove/{panierId}/{productId}")
    public ResponseEntity<FullPanierResponse> removeProductFromPanier(@PathVariable int panierId,
                                                                      @PathVariable int productId,
                                                                      @RequestParam("userId") Long userId) {
        // Retrieve the user by ID
        User user = userClient.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve the panier by ID and associated with the user
        Optional<Panier> optionalPanier = panierDao.findByIdAndUser(panierId, user);
        if (!optionalPanier.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Panier panier = optionalPanier.get();
        Product product = productClient.recupererUnProduit(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        // Modify the panier as needed
        boolean removed = panier.getProducts().removeIf(p -> p.getId() == productId);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        panier.setQuantite(panier.getQuantite() - 1);
        panier.setPrixTotale(panier.getPrixTotale() - product.getPrix());

        // Save the modified panier
        Panier updatedPanier = panierDao.save(panier);

        // Create and return the response
        FullPanierResponse fullPanierResponse = new FullPanierResponse();
        fullPanierResponse.setQuantite(updatedPanier.getQuantite());
        fullPanierResponse.setPrixTotale(updatedPanier.getPrixTotale());
        fullPanierResponse.setProducts(updatedPanier.getProducts());
        return ResponseEntity.ok(fullPanierResponse);
    }

    @PutMapping("/updateQuantity/{panierId}/{productId}")
    public ResponseEntity<FullPanierResponse> updateProductQuantityInPanier(@PathVariable int panierId,
                                                                            @PathVariable int productId,
                                                                            @RequestParam int newQuantity,
                                                                            @RequestParam("userId") Long userId) {
        // Retrieve the user by ID
        User user = userClient.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve the panier by ID and associated with the user
        Optional<Panier> optionalPanier = panierDao.findByIdAndUser(panierId, user);
        if (!optionalPanier.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Panier panier = optionalPanier.get();
        Optional<Product> optionalProduct = panier.getProducts().stream()
                .filter(p -> p.getId() == productId)
                .findFirst();

        if (!optionalProduct.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optionalProduct.get();
        product.setQuantite(newQuantity);

        // Modify the panier as needed
        double totalPrice = panier.getProducts().stream()
                .mapToDouble(p -> p.getPrix() * p.getQuantite())
                .sum();
        panier.setPrixTotale(totalPrice);

        int totalQuantity = panier.getProducts().stream()
                .mapToInt(Product::getQuantite)
                .sum();
        panier.setQuantite(totalQuantity);

        // Save the modified panier
        Panier updatedPanier = panierDao.save(panier);

        // Create and return the response
        FullPanierResponse fullPanierResponse = new FullPanierResponse();
        fullPanierResponse.setQuantite(updatedPanier.getQuantite());
        fullPanierResponse.setPrixTotale(updatedPanier.getPrixTotale());
        fullPanierResponse.setProducts(updatedPanier.getProducts());
        return ResponseEntity.ok(fullPanierResponse);
    }
}
