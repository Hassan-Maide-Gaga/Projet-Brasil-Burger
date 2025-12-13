package org.example.Vue;

import org.example.Model.Burger;
import org.example.Service.BurgerService;
import org.example.config.factory.service.ServiceFactory;
import java.util.List;

public class BurgerVue extends Vue {

    private final BurgerService burgerService;

    public BurgerVue() {
        this.burgerService = ServiceFactory.getBurgerService();
    }

    public void afficherMenuBurger() {
        boolean continuer = true;

        while (continuer) {
            afficherTitre("GESTION DES BURGERS");
            String[] options = {
                    "Lister tous les burgers",
                    "Lister les burgers actifs",
                    "Ajouter un burger",
                    "Modifier un burger",
                    "Archiver un burger",
                    "Supprimer un burger",
                    "Rechercher un burger"
            };
            afficherMenu(options);

            int choix = lireChoix("\nVotre choix: ");

            switch (choix) {
                case 1:
                    listerTousBurgers();
                    break;
                case 2:
                    listerBurgersActifs();
                    break;
                case 3:
                    ajouterBurger();
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
                case 7:
                    rechercherBurger();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    afficherLigne("Choix invalide!");
            }
        }
    }

    private void listerTousBurgers() {
        List<Burger> burgers = burgerService.getAllBurgers();
        afficherTitre("LISTE DES BURGERS (" + burgers.size() + ")");

        if (burgers.isEmpty()) {
            afficherLigne("Aucun burger trouvé.");
            return;
        }

        for (Burger burger : burgers) {
            afficherLigne(formatBurger(burger));
        }
    }

    private void listerBurgersActifs() {
        List<Burger> burgers = burgerService.getBurgersActifs();
        afficherTitre("BURGERS ACTIFS (" + burgers.size() + ")");

        if (burgers.isEmpty()) {
            afficherLigne("Aucun burger actif trouvé.");
            return;
        }

        for (Burger burger : burgers) {
            afficherLigne(formatBurger(burger));
        }
    }

    private void ajouterBurger() {
        afficherTitre("AJOUT D'UN NOUVEAU BURGER");

        String nom = lireString("Nom du burger: ");
        double prix = lireDouble("Prix (FCFA): ");
        String description = lireString("Description: ");

        try {
            Burger burger = burgerService.creerBurger(nom, prix, description);
            afficherLigne("✅ Burger créé avec succès!");
            afficherLigne(formatBurger(burger));
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de la création: " + e.getMessage());
        }
    }

    private void modifierBurger() {
        afficherTitre("MODIFICATION D'UN BURGER");

        int id = lireChoix("ID du burger à modifier: ");

        try {
            Burger burger = burgerService.getBurgerById(id);
            afficherLigne("Burger actuel: " + formatBurger(burger));

            String nom = lireString("Nouveau nom (laisser vide pour garder '" + burger.getNom() + "'): ");
            String prixStr = lireString("Nouveau prix (laisser vide pour garder " + burger.getPrix() + "): ");
            String description = lireString("Nouvelle description (laisser vide pour garder): ");

            if (!nom.isEmpty()) burger.setNom(nom);
            if (!prixStr.isEmpty()) burger.setPrix(Double.parseDouble(prixStr));
            if (!description.isEmpty()) burger.setDescription(description);

            burgerService.modifierBurger(id, burger.getNom(), burger.getPrix(), burger.getDescription());
            afficherLigne("✅ Burger modifié avec succès!");
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de la modification: " + e.getMessage());
        }
    }

    private void archiverBurger() {
        afficherTitre("ARCHIVAGE D'UN BURGER");

        int id = lireChoix("ID du burger à archiver: ");

        try {
            burgerService.archiverBurger(id);
            afficherLigne("✅ Burger archivé avec succès!");
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de l'archivage: " + e.getMessage());
        }
    }

    private void supprimerBurger() {
        afficherTitre("SUPPRESSION D'UN BURGER");

        int id = lireChoix("ID du burger à supprimer: ");
        String confirmation = lireString("Êtes-vous sûr? (oui/non): ");

        if (confirmation.equalsIgnoreCase("oui")) {
            try {
                boolean success = burgerService.supprimerBurger(id);
                if (success) {
                    afficherLigne("✅ Burger supprimé avec succès!");
                } else {
                    afficherLigne("❌ Erreur lors de la suppression.");
                }
            } catch (Exception e) {
                afficherLigne("❌ Erreur: " + e.getMessage());
            }
        } else {
            afficherLigne("Suppression annulée.");
        }
    }

    private void rechercherBurger() {
        afficherTitre("RECHERCHE DE BURGER");

        String recherche = lireString("Nom à rechercher: ");
        List<Burger> burgers = burgerService.rechercherBurgers(recherche);

        afficherTitre("RÉSULTATS (" + burgers.size() + ")");

        if (burgers.isEmpty()) {
            afficherLigne("Aucun burger trouvé.");
            return;
        }

        for (Burger burger : burgers) {
            afficherLigne(formatBurger(burger));
        }
    }

    private String formatBurger(Burger burger) {
        return String.format("ID: %d | %s | %.0f FCFA | %s | %s",
                burger.getId(),
                burger.getNom(),
                burger.getPrix(),
                burger.getDescription(),
                burger.isEstArchive() ? "[ARCHIVÉ]" : "[ACTIF]"
        );
    }
}