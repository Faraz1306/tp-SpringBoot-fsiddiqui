package com.example.demo.service;

import com.example.demo.entity.Annonce;
import com.example.demo.repository.AnnonceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;

    public AnnonceService(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }

    @Transactional
    public Annonce createAnnonce(Annonce annonce) {
        return annonceRepository.save(annonce);
    }

    @Transactional
    public Annonce updateAnnonce(Long id, Annonce annonce) {
        Annonce existing = annonceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'id " + id));
        existing.setTitle(annonce.getTitle());
        existing.setDescription(annonce.getDescription());
        existing.setAdress(annonce.getAdress());
        existing.setMail(annonce.getMail());
        existing.setDate(annonce.getDate());
        return annonceRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public Annonce getAnnonceById(Long id) {
        return annonceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'id " + id));
    }


    @Transactional
    public void deleteAnnonce(Long id) {
        Annonce existing = annonceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'id " + id));
        annonceRepository.delete(existing);
    }
}
