package com.example.mpanier.web.controller;

import com.example.mpanier.client.ProductClient;
import com.example.mpanier.dao.PanierDao;
import com.example.mpanier.model.FullPanierResponse;
import com.example.mpanier.model.Panier;
import com.example.mpanier.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mpanier")
@RequiredArgsConstructor
public class PanierController {

    private final PanierDao panierDao;
    private final ProductClient productClient;

    @PostMapping(value = "/paniers")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Panier> ajouterPanier(@RequestBody Panier panier) {
        Panier nouveauPanier = panierDao.save(panier);
        return new ResponseEntity<>(nouveauPanier, HttpStatus.CREATED);
    }

    @GetMapping(value = "/paniers")
    public List<Panier> listeDesPaniers() {
        return panierDao.findAll();
    }

    @GetMapping(value = "/paniers/{id}")
    public ResponseEntity<Panier> recupererUnPanier(@PathVariable int id) {
        Optional<Panier> panier = panierDao.findById(id);
        return panier.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add/{panierId}/{productId}")
    public ResponseEntity<FullPanierResponse> addProductToPanier(@PathVariable int panierId, @PathVariable int productId) {
        Optional<Panier> optionalPanier = panierDao.findById(panierId);
        if (!optionalPanier.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Panier panier = optionalPanier.get();
        Product product = productClient.getProduct(productId);

        panier.getProducts().add(product);
        panier.setQuantite(panier.getQuantite() + 1);
        panier.setPrixTotale(panier.getPrixTotale() + product.getPrix());

        Panier updatedPanier = panierDao.save(panier);

        FullPanierResponse fullPanierResponse = new FullPanierResponse();
        fullPanierResponse.setQuantite(updatedPanier.getQuantite());
        fullPanierResponse.setPrixTotale(updatedPanier.getPrixTotale());
        fullPanierResponse.setProducts(updatedPanier.getProducts());

        return ResponseEntity.ok(fullPanierResponse);
    }

    @DeleteMapping("/remove/{panierId}/{productId}")
    public ResponseEntity<FullPanierResponse> removeProductFromPanier(@PathVariable int panierId, @PathVariable int productId) {
        Optional<Panier> optionalPanier = panierDao.findById(panierId);
        if (!optionalPanier.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Panier panier = optionalPanier.get();
        Product product = productClient.getProduct(productId);

        boolean removed = panier.getProducts().removeIf(p -> p.getId() == productId);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }

        panier.setQuantite(panier.getQuantite() - 1);
        panier.setPrixTotale(panier.getPrixTotale() - product.getPrix());

        Panier updatedPanier = panierDao.save(panier);

        FullPanierResponse fullPanierResponse = new FullPanierResponse();
        fullPanierResponse.setQuantite(updatedPanier.getQuantite());
        fullPanierResponse.setPrixTotale(updatedPanier.getPrixTotale());
        fullPanierResponse.setProducts(updatedPanier.getProducts());

        return ResponseEntity.ok(fullPanierResponse);
    }

    @PutMapping("/updateQuantity/{panierId}/{productId}")
    public ResponseEntity<FullPanierResponse> updateProductQuantityInPanier(@PathVariable int panierId, @PathVariable int productId, @RequestParam int newQuantity) {
        Optional<Panier> optionalPanier = panierDao.findById(panierId);
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

        double totalPrice = panier.getProducts().stream()
                .mapToDouble(p -> p.getPrix() * p.getQuantite())
                .sum();

        panier.setPrixTotale(totalPrice);
        int totalQuantity = panier.getProducts().stream()
                .mapToInt(Product::getQuantite)
                .sum();
        panier.setQuantite(totalQuantity);

        Panier updatedPanier = panierDao.save(panier);

        FullPanierResponse fullPanierResponse = new FullPanierResponse();
        fullPanierResponse.setQuantite(updatedPanier.getQuantite());
        fullPanierResponse.setPrixTotale(updatedPanier.getPrixTotale());
        fullPanierResponse.setProducts(updatedPanier.getProducts());

        return ResponseEntity.ok(fullPanierResponse);
    }

    @DeleteMapping(value = "/paniers/{id}")
    public ResponseEntity<Void> supprimerPanier(@PathVariable int id) {
        Optional<Panier> panier = panierDao.findById(id);
        if (panier.isPresent()) {
            panierDao.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
