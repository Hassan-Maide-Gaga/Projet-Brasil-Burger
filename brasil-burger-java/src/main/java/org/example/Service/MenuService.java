// Fichier: src/main/java/org/example/service/MenuService.java
package org.example.service;

import org.example.model.Menu;
import org.example.model.MenuComposition;
import org.example.repository.MenuRepository;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MenuService {
    private final MenuRepository menuRepository;
    private final CloudinaryService cloudinaryService;

    public MenuService() {
        this.menuRepository = new MenuRepository();
        this.cloudinaryService = new CloudinaryService();
    }

    public Menu creerMenu(String nom, String description) {
        if (nom == null || nom.trim().isEmpty()) {
            System.err.println("Le nom du menu est obligatoire");
            return null;
        }

        Menu menu = new Menu(nom.trim(), description);
        Menu created = menuRepository.create(menu);

        if (created != null) {
            System.out.println("✓ Menu créé avec succès: " + created.getNom());
        }

        return created;
    }

    public Menu creerMenuAvecImage(String nom, String description, File imageFile) {
        Menu menu = creerMenu(nom, description);

        if (menu != null && imageFile != null && imageFile.exists()) {
            Map<String, Object> uploadResult = cloudinaryService.uploadMenuImage(imageFile);

            if (uploadResult != null) {
                menu.setImageUrl((String) uploadResult.get("secure_url"));
                menu.setImagePublicId((String) uploadResult.get("public_id"));
                menuRepository.update(menu);
                System.out.println("✓ Image du menu uploadée avec succès");
            }
        }

        return menu;
    }

    public boolean ajouterBurgerAuMenu(Integer menuId, Integer burgerId) {
        if (menuRepository.ajouterComposition(menuId, burgerId, null, MenuComposition.TypeElement.BURGER)) {
            System.out.println("✓ Burger ajouté au menu");
            return true;
        }
        return false;
    }

    public boolean ajouterBoissonAuMenu(Integer menuId, Integer complementId) {
        if (menuRepository.ajouterComposition(menuId, null, complementId, MenuComposition.TypeElement.BOISSON)) {
            System.out.println("✓ Boisson ajoutée au menu");
            return true;
        }
        return false;
    }

    public boolean ajouterFriteAuMenu(Integer menuId, Integer complementId) {
        if (menuRepository.ajouterComposition(menuId, null, complementId, MenuComposition.TypeElement.FRITE)) {
            System.out.println("✓ Frites ajoutées au menu");
            return true;
        }
        return false;
    }

    public Menu modifierMenu(Integer id, String nom, String description) {
        Menu menu = menuRepository.findById(id);

        if (menu == null) {
            System.err.println("Menu introuvable avec l'ID: " + id);
            return null;
        }

        if (nom != null && !nom.trim().isEmpty()) {
            menu.setNom(nom.trim());
        }

        if (description != null) {
            menu.setDescription(description);
        }

        if (menuRepository.update(menu)) {
            System.out.println("✓ Menu modifié avec succès");
            return menu;
        }

        return null;
    }

    public boolean modifierImageMenu(Integer id, File imageFile) {
        Menu menu = menuRepository.findById(id);

        if (menu == null) {
            System.err.println("Menu introuvable");
            return false;
        }

        // Supprimer l'ancienne image si elle existe
        if (menu.getImagePublicId() != null) {
            cloudinaryService.deleteImage(menu.getImagePublicId());
        }

        // Uploader la nouvelle image
        Map<String, Object> uploadResult = cloudinaryService.uploadMenuImage(imageFile);

        if (uploadResult != null) {
            menu.setImageUrl((String) uploadResult.get("secure_url"));
            menu.setImagePublicId((String) uploadResult.get("public_id"));
            return menuRepository.update(menu);
        }

        return false;
    }

    public boolean modifierCompositionMenu(Integer menuId, Integer burgerId, Integer boissonId, Integer friteId) {
        // Supprimer les anciennes compositions
        menuRepository.supprimerCompositions(menuId);

        // Ajouter les nouvelles compositions
        boolean success = true;

        if (burgerId != null) {
            success &= ajouterBurgerAuMenu(menuId, burgerId);
        }

        if (boissonId != null) {
            success &= ajouterBoissonAuMenu(menuId, boissonId);
        }

        if (friteId != null) {
            success &= ajouterFriteAuMenu(menuId, friteId);
        }

        if (success) {
            System.out.println("✓ Composition du menu modifiée avec succès");
        }

        return success;
    }

    public boolean archiverMenu(Integer id) {
        Menu menu = menuRepository.findById(id);

        if (menu == null) {
            System.err.println("Menu introuvable");
            return false;
        }

        if (menuRepository.archiver(id)) {
            System.out.println("✓ Menu archivé avec succès");
            return true;
        }

        return false;
    }

    public Menu obtenirMenu(Integer id) {
        return menuRepository.findById(id);
    }

    public List<Menu> listerMenus() {
        return menuRepository.findAll();
    }

    public List<Menu> listerMenusActifs() {
        return menuRepository.findActifs();
    }

    public void afficherDetailsMenu(Integer id) {
        Menu menu = menuRepository.findById(id);

        if (menu == null) {
            System.out.println("Menu introuvable");
            return;
        }

        System.out.println("\n=== Détails du Menu ===");
        System.out.println("ID: " + menu.getId());
        System.out.println("Nom: " + menu.getNom());
        System.out.println("Description: " + menu.getDescription());
        System.out.println("Prix Total: " + menu.calculerPrixTotal() + " FCFA");
        System.out.println("Statut: " + menu.getStatutArchivage());

        System.out.println("\nComposition:");
        for (MenuComposition comp : menu.getCompositions()) {
            System.out.print("  - " + comp.getTypeElement() + ": ");
            if (comp.getBurger() != null) {
                System.out.println(comp.getBurger().getNom() + " (" + comp.getBurger().getPrix() + " FCFA)");
            }
            if (comp.getComplement() != null) {
                System.out.println(comp.getComplement().getNom() + " (" + comp.getComplement().getPrix() + " FCFA)");
            }
        }
    }
}