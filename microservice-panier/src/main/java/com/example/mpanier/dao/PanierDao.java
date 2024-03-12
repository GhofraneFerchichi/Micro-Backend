package com.example.mpanier.dao;


import com.example.mpanier.model.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanierDao extends JpaRepository<Panier, Integer> {
}
