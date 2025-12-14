// Fichier: src/main/java/org/example/service/BurgerService.java
package org.example.service;

import org.example.model.Burger;
import org.example.repository.BurgerRepository;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BurgerService {
    private final BurgerRepository burgerRepository;
    private final CloudinaryService cloudinaryService;

    public BurgerService() {
        this.burgerRepository = new BurgerRepository();
        this.cloudinaryService = new CloudinaryService();
    }

    public Burger creerBurger(String nom, Double prix, String description) {
        if (nom == null || nom.trim().isEmpty()) {
            System.err.println("Le nom du burger est obligatoire");
            return null;
        }

        if (prix == null || prix <= 0) {
            System.err.println("Le prix doit être supérieur à 0");
            return null;
        }

        Burger burger = new Burger(nom.trim(), prix, description);
        Burger created = burgerRepository.create(burger);

        if (created != null) {
            System.out.println("✓ Burger créé avec succès: " + created.getNom());
        }

        return created;
    }

    public Burger creerBurgerAvecImage(String nom, Double prix, String description, File imageFile) {
        Burger burger = creerBurger(nom, prix, description);

        if (burger != null && imageFile != null && imageFile.exists()) {
            Map<String, Object> uploadResult = cloudinaryService.uploadBurgerImage(imageFile);

            if (uploadResult != null) {
                burger.setImageUrl((String) uploadResult.get("secure_url"));
                burger.setImagePublicId((String) uploadResult.get("public_id"));
                burgerRepository.update(burger);
                System.out.println("✓ Image du burger uploadée avec succès");
            }
        }

        return burger;
    }

    public Burger modifierBurger(Integer id, String nom, Double prix, String description) {
        Burger burger = burgerRepository.findById(id);

        if (burger == null) {
            System.err.println("Burger introuvable avec l'ID: " + id);
            return null;
        }

        if (nom != null && !nom.trim().isEmpty()) {
            burger.setNom(nom.trim());
        }

        if (prix != null && prix > 0) {
            burger.setPrix(prix);
        }

        if (description != null) {
            burger.setDescription(description);
        }

        if (burgerRepository.update(burger)) {
            System.out.println("✓ Burger modifié avec succès");
            return burger;
        }

        return null;
    }

    public boolean modifierImageBurger(Integer id, File imageFile) {
        Burger burger = burgerRepository.findById(id);

        if (burger == null) {
            System.err.println("Burger introuvable");
            return false;
        }

        // Supprimer l'ancienne image si elle existe
        if (burger.getImagePublicId() != null) {
            cloudinaryService.deleteImage(burger.getImagePublicId());
        }

        // Uploader la nouvelle image
        Map<String, Object> uploadResult = cloudinaryService.uploadBurgerImage(imageFile);

        if (uploadResult != null) {
            burger.setImageUrl((String) uploadResult.get("secure_url"));
            burger.setImagePublicId((String) uploadResult.get("public_id"));
            return burgerRepository.update(burger);
        }

        return false;
    }

    public boolean archiverBurger(Integer id) {
        Burger burger = burgerRepository.findById(id);

        if (burger == null) {
            System.err.println("Burger introuvable");
            return false;
        }

        if (burgerRepository.archiver(id)) {
            System.out.println("✓ Burger archivé avec succès");
            return true;
        }

        return false;
    }

    public boolean supprimerBurger(Integer id) {
        Burger burger = burgerRepository.findById(id);

        if (burger == null) {
            System.err.println("Burger introuvable");
            return false;
        }

        // Supprimer l'image de Cloudinary si elle existe
        if (burger.getImagePublicId() != null) {
            cloudinaryService.deleteImage(burger.getImagePublicId());
        }

        if (burgerRepository.delete(id)) {
            System.out.println("✓ Burger supprimé avec succès");
            return true;
        }

        return false;
    }

    public Burger obtenirBurger(Integer id) {
        return burgerRepository.findById(id);
    }

    public List<Burger> listerBurgers() {
        return burgerRepository.findAll();
    }

    public List<Burger> listerBurgersActifs() {
        return burgerRepository.findActifs();
    }
}