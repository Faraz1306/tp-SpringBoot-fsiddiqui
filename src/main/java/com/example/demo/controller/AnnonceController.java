package com.example.demo.controller;

import com.example.demo.entity.Annonce;
import com.example.demo.service.AnnonceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    private final AnnonceService annonceService;

    public AnnonceController(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    // Afficher la liste des annonces
    @GetMapping
    public List<Annonce> getAnnonces() {
        return annonceService.getAllAnnonces();
    }

    // Création d'une annonce
    @PostMapping
    public Annonce createAnnonce(@RequestBody Annonce annonce) {
        return annonceService.createAnnonce(annonce);
    }

    // Mise à jour d'une annonce
    @PutMapping("/{id}")
    public Annonce updateAnnonce(@PathVariable Long id, @RequestBody Annonce annonce) {
        return annonceService.updateAnnonce(id, annonce);
    }

    // Suppression d'une annonce
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAnnonce(@PathVariable Long id) {
        annonceService.deleteAnnonce(id);
    }

    @GetMapping("/{id}")
    public Annonce getAnnonceById(@PathVariable Long id) {
        return annonceService.getAnnonceById(id);
    }


}
