// Fichier: src/main/java/org/example/service/ComplementService.java
package org.example.service;

import org.example.model.Complement;
import org.example.repository.ComplementRepository;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ComplementService {
    private final ComplementRepository complementRepository;
    private final CloudinaryService cloudinaryService;

    public ComplementService() {
        this.complementRepository = new ComplementRepository();
        this.cloudinaryService = new CloudinaryService();
    }

    public Complement creerComplement(String nom, Double prix, Complement.TypeComplement type) {
        if (nom == null || nom.trim().isEmpty()) {
            System.err.println("Le nom du complément est obligatoire");
            return null;
        }

        if (prix == null || prix <= 0) {
            System.err.println("Le prix doit être supérieur à 0");
            return null;
        }

        if (type == null) {
            System.err.println("Le type de complément est obligatoire");
            return null;
        }

        Complement complement = new Complement(nom.trim(), prix, type);
        Complement created = complementRepository.create(complement);

        if (created != null) {
            System.out.println("✓ Complément créé avec succès: " + created.getNom());
        }

        return created;
    }

    public Complement creerComplementAvecImage(String nom, Double prix, Complement.TypeComplement type, File imageFile) {
        Complement complement = creerComplement(nom, prix, type);

        if (complement != null && imageFile != null && imageFile.exists()) {
            Map<String, Object> uploadResult = cloudinaryService.uploadComplementImage(imageFile);

            if (uploadResult != null) {
                complement.setImageUrl((String) uploadResult.get("secure_url"));
                complement.setImagePublicId((String) uploadResult.get("public_id"));
                complementRepository.update(complement);
                System.out.println("✓ Image du complément uploadée avec succès");
            }
        }

        return complement;
    }

    public Complement modifierComplement(Integer id, String nom, Double prix, Complement.TypeComplement type) {
        Complement complement = complementRepository.findById(id);

        if (complement == null) {
            System.err.println("Complément introuvable avec l'ID: " + id);
            return null;
        }

        if (nom != null && !nom.trim().isEmpty()) {
            complement.setNom(nom.trim());
        }

        if (prix != null && prix > 0) {
            complement.setPrix(prix);
        }

        if (type != null) {
            complement.setTypeComplement(type);
        }

        if (complementRepository.update(complement)) {
            System.out.println("✓ Complément modifié avec succès");
            return complement;
        }

        return null;
    }

    public boolean modifierImageComplement(Integer id, File imageFile) {
        Complement complement = complementRepository.findById(id);

        if (complement == null) {
            System.err.println("Complément introuvable");
            return false;
        }

        // Supprimer l'ancienne image si elle existe
        if (complement.getImagePublicId() != null) {
            cloudinaryService.deleteImage(complement.getImagePublicId());
        }

        // Uploader la nouvelle image
        Map<String, Object> uploadResult = cloudinaryService.uploadComplementImage(imageFile);

        if (uploadResult != null) {
            complement.setImageUrl((String) uploadResult.get("secure_url"));
            complement.setImagePublicId((String) uploadResult.get("public_id"));
            return complementRepository.update(complement);
        }

        return false;
    }

    public boolean archiverComplement(Integer id) {
        Complement complement = complementRepository.findById(id);

        if (complement == null) {
            System.err.println("Complément introuvable");
            return false;
        }

        if (complementRepository.archiver(id)) {
            System.out.println("✓ Complément archivé avec succès");
            return true;
        }

        return false;
    }

    public boolean supprimerComplement(Integer id) {
        Complement complement = complementRepository.findById(id);

        if (complement == null) {
            System.err.println("Complément introuvable");
            return false;
        }

        // Supprimer l'image de Cloudinary si elle existe
        if (complement.getImagePublicId() != null) {
            cloudinaryService.deleteImage(complement.getImagePublicId());
        }

        if (complementRepository.delete(id)) {
            System.out.println("✓ Complément supprimé avec succès");
            return true;
        }

        return false;
    }

    public Complement obtenirComplement(Integer id) {
        return complementRepository.findById(id);
    }

    public List<Complement> listerComplements() {
        return complementRepository.findAll();
    }

    public List<Complement> listerComplementsActifs() {
        return complementRepository.findActifs();
    }

    public List<Complement> listerBoissons() {
        return complementRepository.findByType(Complement.TypeComplement.BOISSON);
    }

    public List<Complement> listerFrites() {
        return complementRepository.findByType(Complement.TypeComplement.FRITE);
    }
}