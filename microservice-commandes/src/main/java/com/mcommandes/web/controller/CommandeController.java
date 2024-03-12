package com.mcommandes.web.controller;

import com.mcommandes.dao.CommandesDao;
import com.mcommandes.model.Commande;
import com.mcommandes.web.exceptions.CommandeNotFoundException;
import com.mcommandes.web.exceptions.ImpossibleAjouterCommandeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mcommandes")
public class CommandeController {

    private final CommandesDao commandesDao;

    public CommandeController(CommandesDao commandesDao) {
        this.commandesDao = commandesDao;
    }

    @PostMapping("/commandes")
    public ResponseEntity<Commande> ajouterCommande(@RequestBody Commande commande) {
        // Add your logic to save the commande to the database using CommandesDao
        Commande savedCommande = commandesDao.save(commande);
        if (savedCommande != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCommande);
        } else {
            throw new ImpossibleAjouterCommandeException("Impossible d'ajouter cette commande");
        }
    }

    @GetMapping("/commandes/{id}")
    public ResponseEntity<Commande> recupererUneCommande(@PathVariable int id) {
        Optional<Commande> optionalCommande = commandesDao.findById(id);
        if (optionalCommande.isPresent()) {
            return ResponseEntity.ok(optionalCommande.get());
        } else {
            throw new CommandeNotFoundException("Cette commande n'existe pas");
        }
    }


    @GetMapping
    public ResponseEntity<List
            <Commande>> getAllCommands() {
        List<Commande> allCommands = commandesDao.findAll();
        if (!allCommands.isEmpty()) {
            return ResponseEntity.ok(allCommands);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

  /*  @GetMapping("/with-products/{command-id}")
    public ResponseEntity<List
            <Commande>> getAllCommands(@PathVariable("command-id") Integer commandId) {
        List<Commande> allCommands = commandesDao.findCommandWithProducts(commandId);
        if (!allCommands.isEmpty()) {
            return ResponseEntity.ok(allCommands);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
*/
}
