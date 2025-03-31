package com.example.demo.repository;

import com.example.demo.entity.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
}
