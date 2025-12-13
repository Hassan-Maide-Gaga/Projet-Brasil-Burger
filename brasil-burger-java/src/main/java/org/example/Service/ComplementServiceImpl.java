package org.example.Service;

import org.example.Model.Complement;
import org.example.Model.Enum.TypeComplement;
import org.example.Repository.ComplementRepository;
import org.example.Repository.ComplementRepositoryImpl;
import java.util.List;
import java.util.Optional;

public class ComplementServiceImpl implements ComplementService {

    private final ComplementRepository complementRepository;

    public ComplementServiceImpl() {
        this.complementRepository = new ComplementRepositoryImpl();
    }

    @Override
    public Complement creerComplement(String nom, double prix, String description,
                                      TypeComplement type, String taille) {
        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du complément ne peut pas être vide");
        }
        if (prix < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        if (type == null) {
            throw new IllegalArgumentException("Le type de complément est requis");
        }

        Complement complement = new Complement(nom, prix, description, type, taille);
        return complementRepository.save(complement);
    }

    @Override
    public Complement modifierComplement(int id, String nom, double prix, String description,
                                         TypeComplement type, String taille) {
        // Vérifier que le complément existe
        Optional<Complement> existant = complementRepository.findById(id);
        if (!existant.isPresent()) {
            throw new IllegalArgumentException("Complément introuvable avec l'ID: " + id);
        }

        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du complément ne peut pas être vide");
        }
        if (prix < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        if (type == null) {
            throw new IllegalArgumentException("Le type de complément est requis");
        }

        Complement complement = existant.get();
        complement.setNom(nom);
        complement.setPrix(prix);
        complement.setDescription(description);
        complement.setTypeComplement(type);
        complement.setTaille(taille);

        return complementRepository.update(complement);
    }

    @Override
    public boolean supprimerComplement(int id) {
        Optional<Complement> complement = complementRepository.findById(id);
        if (!complement.isPresent()) {
            throw new IllegalArgumentException("Complément introuvable avec l'ID: " + id);
        }

        return complementRepository.delete(id);
    }

    @Override
    public boolean archiverComplement(int id) {
        Optional<Complement> complement = complementRepository.findById(id);
        if (!complement.isPresent()) {
            throw new IllegalArgumentException("Complément introuvable avec l'ID: " + id);
        }

        return complementRepository.archive(id);
    }

    @Override
    public Complement getComplementById(int id) {
        return complementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Complément introuvable avec l'ID: " + id));
    }

    @Override
    public List<Complement> getAllComplements() {
        return complementRepository.findAll();
    }

    @Override
    public List<Complement> getComplementsActifs() {
        return complementRepository.findByArchive(false);
    }

    @Override
    public List<Complement> getByType(TypeComplement type) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de complément est requis");
        }
        return complementRepository.findByType(type);
    }

    @Override
    public List<Complement> getBoissons() {
        return complementRepository.findBoissons();
    }

    @Override
    public List<Complement> getFrites() {
        return complementRepository.findFrites();
    }

    @Override
    public List<Complement> rechercherComplements(String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return getAllComplements();
        }
        return complementRepository.findByName(recherche.trim());
    }
}