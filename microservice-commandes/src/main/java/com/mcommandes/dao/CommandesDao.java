package com.mcommandes.dao;

import com.mcommandes.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommandesDao extends JpaRepository<Commande, Integer> {

    @Query("SELECT c FROM Commande c JOIN FETCH c.products")
    List<Commande> findAllWithProducts();
}
