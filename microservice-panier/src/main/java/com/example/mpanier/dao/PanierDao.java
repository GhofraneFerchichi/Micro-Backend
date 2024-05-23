package com.example.mpanier.dao;


import com.example.mpanier.model.Panier;
import com.example.mpanier.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PanierDao extends JpaRepository<Panier, Integer> {

    List<Panier> findByUser(User user);
    Optional<Panier> findByIdAndUser(int id, User user);

    @Query("select u from User u where u.panier.id = ?1")
    Optional<Panier> findPanierByUserId(int id);


}
