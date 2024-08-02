package com.mcommandes.web.controller;

import com.mcommandes.client.PanierClient;
import com.mcommandes.client.ProductClient;
import com.mcommandes.client.UserClient;
import com.mcommandes.dao.CommandesDao;
import com.mcommandes.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommandeControllerTest {

    @InjectMocks
    private CommandeController commandeController;

    @Mock
    private PanierClient panierClient;

    @Mock
    private CommandesDao commandesDao;

    @Mock
    private UserClient userClient;

    @Mock
    private ProductClient productClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSayHello() {
        ResponseEntity<String> response = commandeController.sayHello();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello from mcommandes service!", response.getBody());
    }

    @Test
    public void testValiderCommande() {
        Panier panier = new Panier();
        panier.setQuantite(2);
        panier.setPrixTotale(200.0);
        panier.setProducts(Arrays.asList(new Product(1, "Product1", "Description1", 100.0),
                new Product(2, "Product2", "Description2", 100.0)));

        com.mcommandes.model.User user = new com.mcommandes.model.User();
        user.setId(1L);

        when(panierClient.getPanierById(anyInt())).thenReturn(panier);
        when(userClient.getUserById(anyLong())).thenReturn(user);
        when(productClient.recupererUnProduit(anyInt())).thenReturn(new ResponseEntity<>(new ProductDTO(1, "Product1", "Description1", 100.0), HttpStatus.OK));
        when(commandesDao.save(any(Commande.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<Commande> response = commandeController.validerCommande(1, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Commande commande = response.getBody();
        assertEquals(2, commande.getQuantite());
        assertEquals(200.0, commande.getPrixTotale());
        verify(panierClient, times(1)).supprimerPanier(1);
        verify(commandesDao, times(1)).flush();
    }



    @Test
    public void testGetTotalPriceOfPanier() {
        Panier panier = new Panier();
        panier.setPrixTotale(150.0);

        when(panierClient.getPanierById(anyInt())).thenReturn(panier);

        double totalPrice = commandeController.getTotalPriceOfPanier(1);

        assertEquals(150.0, totalPrice);
    }

    @Test
    public void testListeDesCommandes() {
        Commande commande1 = new Commande();
        Commande commande2 = new Commande();
        when(commandesDao.findAll()).thenReturn(Arrays.asList(commande1, commande2));

        List<Commande> commandes = commandeController.listeDesCommandes();

        assertEquals(2, commandes.size());
        verify(commandesDao, times(1)).findAll();
    }

    @Test
    public void testRecupererUneCommande() {
        Commande commande = new Commande();
        when(commandesDao.findById(anyInt())).thenReturn(Optional.of(commande));

        ResponseEntity<Commande> response = commandeController.recupererUneCommande(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commande, response.getBody());
    }

    @Test
    public void testRecupererUneCommande_NotFound() {
        when(commandesDao.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Commande> response = commandeController.recupererUneCommande(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSupprimerCommande() {
        Commande commande = new Commande();
        when(commandesDao.findById(anyInt())).thenReturn(Optional.of(commande));

        ResponseEntity<Void> response = commandeController.supprimerCommande(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commandesDao, times(1)).deleteById(1);
    }

    @Test
    public void testSupprimerCommande_NotFound() {
        when(commandesDao.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Void> response = commandeController.supprimerCommande(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
