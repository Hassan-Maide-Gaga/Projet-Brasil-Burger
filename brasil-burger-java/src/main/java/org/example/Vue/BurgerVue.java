// Fichier: src/main/java/org/example/vue/BurgerVue.java
package org.example.vue;

import org.example.model.Burger;
import org.example.service.BurgerService;

import java.io.File;
import java.util.List;

public class BurgerVue extends Vue {
    private final BurgerService burgerService;

    public BurgerVue() {
        super();
        this.burgerService = new BurgerService();
    }

    @Override
    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            afficherMenuBurger();
            Integer choix = lireEntier("\nVotre choix");

            if (choix == null) {
                continue;
            }

            switch (choix) {
                case 1:
                    creerBurger();
                    break;
                case 2:
                    listerBurgers();
                    break;
                case 3:
                    listerBurgersActifs();
                    break;
                case 4:
                    modifierBurger();
                    break;
                case 5:
                    archiverBurger();
                    break;
                case 6:
                    supprimerBurger();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    afficherErreur("Choix invalide");
            }

            if (continuer && choix != 0) {
                pause();
            }
        }
    }

    private void afficherMenuBurger() {
        afficherTitre("GESTION DES BURGERS");
        System.out.println("1. Créer un burger");
        System.out.println("2. Lister tous les burgers");
        System.out.println("3. Lister les burgers actifs");
        System.out.println("4. Modifier un burger");
        System.out.println("5. Archiver un burger");
        System.out.println("6. Supprimer un burger");
        System.out.println("0. Retour");
        afficherSeparateur();
    }

    private void creerBurger() {
        afficherTitre("CRÉER UN BURGER");

        String nom = lireChaine("Nom du burger");
        if (nom.isEmpty()) {
            afficherErreur("Le nom est obligatoire");
            return;
        }

        Double prix = lireDouble("Prix (FCFA)");
        if (prix == null || prix <= 0) {
            afficherErreur("Le prix doit être supérieur à 0");
            return;
        }

        String description = lireChaine("Description (optionnel)");

        boolean avecImage = confirmer("Voulez-vous ajouter une image ?");

        Burger burger;
        if (avecImage) {
            String cheminImage = lireChaine("Chemin complet de l'image");
            File imageFile = new File(cheminImage);

            if (!imageFile.exists()) {
                afficherErreur("Fichier image introuvable");
                return;
            }

            burger = burgerService.creerBurgerAvecImage(nom, prix, description, imageFile);
        } else {
            burger = burgerService.creerBurger(nom, prix, description);
        }

        if (burger != null) {
            afficherSucces("Burger créé avec succès (ID: " + burger.getId() + ")");
            afficherDetailsBurger(burger);
        } else {
            afficherErreur("Échec de la création du burger");
        }
    }

    private void listerBurgers() {
        afficherTitre("LISTE DE TOUS LES BURGERS");

        List<Burger> burgers = burgerService.listerBurgers();

        if (burgers.isEmpty()) {
            afficherMessage("Aucun burger trouvé");
            return;
        }

        afficherListeBurgers(burgers);
    }

    private void listerBurgersActifs() {
        afficherTitre("LISTE DES BURGERS ACTIFS");

        List<Burger> burgers = burgerService.listerBurgersActifs();

        if (burgers.isEmpty()) {
            afficherMessage("Aucun burger actif trouvé");
            return;
        }

        afficherListeBurgers(burgers);
    }

    private void afficherListeBurgers(List<Burger> burgers) {
        System.out.println(String.format("\n%-5s %-30s %-15s %-15s", "ID", "Nom", "Prix (FCFA)", "Statut"));
        afficherSeparateur();

        for (Burger burger : burgers) {
            System.out.println(String.format("%-5d %-30s %-15.2f %-15s",
                    burger.getId(),
                    burger.getNom(),
                    burger.getPrix(),
                    burger.getStatutArchivage()));
        }

        System.out.println("\nTotal: " + burgers.size() + " burger(s)");
    }

    private void afficherDetailsBurger(Burger burger) {
        System.out.println("\n--- Détails du Burger ---");
        System.out.println("ID: " + burger.getId());
        System.out.println("Nom: " + burger.getNom());
        System.out.println("Prix: " + burger.getPrix() + " FCFA");
        System.out.println("Description: " + (burger.getDescription() != null ? burger.getDescription() : "N/A"));
        System.out.println("Image URL: " + (burger.getImageUrl() != null ? burger.getImageUrl() : "Aucune image"));
        System.out.println("Statut: " + burger.getStatutArchivage());
        System.out.println("Date création: " + burger.getDateCreation());
    }

    private void modifierBurger() {
        afficherTitre("MODIFIER UN BURGER");

        Integer id = lireEntier("ID du burger à modifier");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Burger burger = burgerService.obtenirBurger(id);
        if (burger == null) {
            afficherErreur("Burger introuvable");
            return;
        }

        afficherDetailsBurger(burger);
        System.out.println("\n(Laissez vide pour conserver la valeur actuelle)");

        String nom = lireChaine("Nouveau nom");
        Double prix = lireDouble("Nouveau prix (FCFA)");
        String description = lireChaine("Nouvelle description");

        Burger modifie = burgerService.modifierBurger(id,
                nom.isEmpty() ? null : nom,
                prix,
                description.isEmpty() ? null : description);

        if (modifie != null) {
            afficherSucces("Burger modifié avec succès");

            if (confirmer("Voulez-vous modifier l'image ?")) {
                String cheminImage = lireChaine("Chemin complet de la nouvelle image");
                File imageFile = new File(cheminImage);

                if (imageFile.exists()) {
                    if (burgerService.modifierImageBurger(id, imageFile)) {
                        afficherSucces("Image modifiée avec succès");
                    }
                }
            }
        }
    }

    private void archiverBurger() {
        afficherTitre("ARCHIVER UN BURGER");

        Integer id = lireEntier("ID du burger à archiver");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Burger burger = burgerService.obtenirBurger(id);
        if (burger == null) {
            afficherErreur("Burger introuvable");
            return;
        }

        afficherDetailsBurger(burger);

        if (confirmer("\nConfirmez-vous l'archivage de ce burger ?")) {
            if (burgerService.archiverBurger(id)) {
                afficherSucces("Burger archivé avec succès");
            } else {
                afficherErreur("Échec de l'archivage");
            }
        }
    }

    private void supprimerBurger() {
        afficherTitre("SUPPRIMER UN BURGER");

        Integer id = lireEntier("ID du burger à supprimer");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Burger burger = burgerService.obtenirBurger(id);
        if (burger == null) {
            afficherErreur("Burger introuvable");
            return;
        }

        afficherDetailsBurger(burger);

        System.out.println("\n⚠️  ATTENTION: Cette action est irréversible!");
        if (confirmer("Confirmez-vous la suppression définitive de ce burger ?")) {
            if (burgerService.supprimerBurger(id)) {
                afficherSucces("Burger supprimé avec succès");
            } else {
                afficherErreur("Échec de la suppression");
            }
        }
    }
}