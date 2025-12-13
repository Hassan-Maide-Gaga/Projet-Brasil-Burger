package org.example.Vue;

import org.example.Model.Burger;
import org.example.Model.Complement;
import org.example.Model.Menu;
import org.example.Service.BurgerService;
import org.example.Service.ComplementService;
import org.example.Service.MenuService;
import org.example.config.factory.service.ServiceFactory;
import java.util.ArrayList;
import java.util.List;

public class MenuVue extends Vue {

    private final MenuService menuService;
    private final BurgerService burgerService;
    private final ComplementService complementService;

    public MenuVue() {
        this.menuService = ServiceFactory.getMenuService();
        this.burgerService = ServiceFactory.getBurgerService();
        this.complementService = ServiceFactory.getComplementService();
    }

    public void afficherMenuMenu() {
        boolean continuer = true;

        while (continuer) {
            afficherTitre("GESTION DES MENUS");
            String[] options = {
                    "Lister tous les menus",
                    "Lister les menus actifs",
                    "Ajouter un menu",
                    "Modifier un menu",
                    "Archiver un menu",
                    "Supprimer un menu",
                    "Rechercher un menu",
                    "Afficher détails d'un menu"
            };
            afficherMenu(options);

            int choix = lireChoix("\nVotre choix: ");

            switch (choix) {
                case 1:
                    listerTousMenus();
                    break;
                case 2:
                    listerMenusActifs();
                    break;
                case 3:
                    ajouterMenu();
                    break;
                case 4:
                    modifierMenu();
                    break;
                case 5:
                    archiverMenu();
                    break;
                case 6:
                    supprimerMenu();
                    break;
                case 7:
                    rechercherMenu();
                    break;
                case 8:
                    afficherDetailsMenu();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    afficherLigne("Choix invalide!");
            }
        }
    }

    private void listerTousMenus() {
        List<Menu> menus = menuService.getAllMenus();
        afficherTitre("LISTE DES MENUS (" + menus.size() + ")");

        if (menus.isEmpty()) {
            afficherLigne("Aucun menu trouvé.");
            return;
        }

        for (Menu menu : menus) {
            afficherLigne(formatMenu(menu));
        }
    }

    private void listerMenusActifs() {
        List<Menu> menus = menuService.getMenusActifs();
        afficherTitre("MENUS ACTIFS (" + menus.size() + ")");

        if (menus.isEmpty()) {
            afficherLigne("Aucun menu actif trouvé.");
            return;
        }

        for (Menu menu : menus) {
            afficherLigne(formatMenu(menu));
        }
    }

    private void ajouterMenu() {
        afficherTitre("AJOUT D'UN NOUVEAU MENU");

        // Saisie des informations de base
        String nom = lireString("Nom du menu: ");
        String description = lireString("Description: ");

        // Lister les burgers disponibles
        afficherTitre("CHOIX DU BURGER");
        List<Burger> burgers = burgerService.getBurgersActifs();
        if (burgers.isEmpty()) {
            afficherLigne("Aucun burger disponible. Créez d'abord des burgers.");
            return;
        }

        for (int i = 0; i < burgers.size(); i++) {
            Burger burger = burgers.get(i);
            afficherLigne((i + 1) + ". " + burger.getNom() + " - " + burger.getPrix() + " FCFA");
        }

        int choixBurger = lireChoix("Choisir le burger (1-" + burgers.size() + "): ");
        if (choixBurger < 1 || choixBurger > burgers.size()) {
            afficherLigne("Choix invalide!");
            return;
        }
        Burger burgerChoisi = burgers.get(choixBurger - 1);

        // Lister les boissons disponibles
        afficherTitre("CHOIX DE LA BOISSON");
        List<Complement> boissons = complementService.getBoissons();
        if (boissons.isEmpty()) {
            afficherLigne("Aucune boisson disponible. Créez d'abord des boissons.");
            return;
        }

        for (int i = 0; i < boissons.size(); i++) {
            Complement boisson = boissons.get(i);
            afficherLigne((i + 1) + ". " + boisson.getNom() + " - " + boisson.getPrix() + " FCFA");
        }

        int choixBoisson = lireChoix("Choisir la boisson (1-" + boissons.size() + "): ");
        if (choixBoisson < 1 || choixBoisson > boissons.size()) {
            afficherLigne("Choix invalide!");
            return;
        }
        Complement boissonChoisie = boissons.get(choixBoisson - 1);

        // Lister les frites disponibles
        afficherTitre("CHOIX DES FRITES");
        List<Complement> frites = complementService.getFrites();
        if (frites.isEmpty()) {
            afficherLigne("Aucune frite disponible. Créez d'abord des frites.");
            return;
        }

        for (int i = 0; i < frites.size(); i++) {
            Complement frite = frites.get(i);
            afficherLigne((i + 1) + ". " + frite.getNom() + " - " + frite.getPrix() + " FCFA");
        }

        int choixFrite = lireChoix("Choisir les frites (1-" + frites.size() + "): ");
        if (choixFrite < 1 || choixFrite > frites.size()) {
            afficherLigne("Choix invalide!");
            return;
        }
        Complement friteChoisie = frites.get(choixFrite - 1);

        // Calculer le prix total
        double prixTotal = burgerChoisi.getPrix() + boissonChoisie.getPrix() + friteChoisie.getPrix();

        afficherLigne("\nRÉCAPITULATIF DU MENU:");
        afficherLigne("Nom: " + nom);
        afficherLigne("Description: " + description);
        afficherLigne("Burger: " + burgerChoisi.getNom() + " (" + burgerChoisi.getPrix() + " FCFA)");
        afficherLigne("Boisson: " + boissonChoisie.getNom() + " (" + boissonChoisie.getPrix() + " FCFA)");
        afficherLigne("Frites: " + friteChoisie.getNom() + " (" + friteChoisie.getPrix() + " FCFA)");
        afficherLigne("Prix total: " + prixTotal + " FCFA");

        String confirmation = lireString("\nConfirmer la création? (oui/non): ");
        if (confirmation.equalsIgnoreCase("oui")) {
            try {
                Menu menu = menuService.creerMenu(
                        nom,
                        description,
                        burgerChoisi.getId(),
                        boissonChoisie.getId(),
                        friteChoisie.getId()
                );
                afficherLigne("✅ Menu créé avec succès!");
                afficherLigne(formatMenuDetail(menu));
            } catch (Exception e) {
                afficherLigne("❌ Erreur lors de la création: " + e.getMessage());
            }
        } else {
            afficherLigne("Création annulée.");
        }
    }

    private void modifierMenu() {
        afficherTitre("MODIFICATION D'UN MENU");

        int id = lireChoix("ID du menu à modifier: ");

        try {
            Menu menu = menuService.getMenuById(id);
            afficherLigne("Menu actuel:");
            afficherLigne(formatMenuDetail(menu));

            String nom = lireString("\nNouveau nom (laisser vide pour garder '" + menu.getNom() + "'): ");
            String description = lireString("Nouvelle description (laisser vide pour garder): ");

            // Choix de nouveaux composants (optionnel)
            afficherLigne("\nVoulez-vous changer les composants du menu?");
            String changerComposants = lireString("(oui/non): ");

            int burgerId = menu.getBurgerId();
            int boissonId = menu.getBoissonId();
            int friteId = menu.getFriteId();

            if (changerComposants.equalsIgnoreCase("oui")) {
                // Changer burger
                List<Burger> burgers = burgerService.getBurgersActifs();
                afficherLigne("\nBurgers disponibles:");
                for (int i = 0; i < burgers.size(); i++) {
                    afficherLigne((i + 1) + ". " + burgers.get(i).getNom());
                }
                int choixBurger = lireChoix("Nouveau burger (0 pour garder actuel): ");
                if (choixBurger > 0 && choixBurger <= burgers.size()) {
                    burgerId = burgers.get(choixBurger - 1).getId();
                }

                // Changer boisson
                List<Complement> boissons = complementService.getBoissons();
                afficherLigne("\nBoissons disponibles:");
                for (int i = 0; i < boissons.size(); i++) {
                    afficherLigne((i + 1) + ". " + boissons.get(i).getNom());
                }
                int choixBoisson = lireChoix("Nouvelle boisson (0 pour garder actuelle): ");
                if (choixBoisson > 0 && choixBoisson <= boissons.size()) {
                    boissonId = boissons.get(choixBoisson - 1).getId();
                }

                // Changer frites
                List<Complement> frites = complementService.getFrites();
                afficherLigne("\nFrites disponibles:");
                for (int i = 0; i < frites.size(); i++) {
                    afficherLigne((i + 1) + ". " + frites.get(i).getNom());
                }
                int choixFrite = lireChoix("Nouvelles frites (0 pour garder actuelles): ");
                if (choixFrite > 0 && choixFrite <= frites.size()) {
                    friteId = frites.get(choixFrite - 1).getId();
                }
            }

            // Appliquer les modifications
            if (!nom.isEmpty()) menu.setNom(nom);
            if (!description.isEmpty()) menu.setDescription(description);

            Menu menuModifie = menuService.modifierMenu(
                    id,
                    menu.getNom(),
                    menu.getDescription(),
                    burgerId,
                    boissonId,
                    friteId
            );

            afficherLigne("✅ Menu modifié avec succès!");
            afficherLigne(formatMenuDetail(menuModifie));

        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de la modification: " + e.getMessage());
        }
    }

    private void archiverMenu() {
        afficherTitre("ARCHIVAGE D'UN MENU");

        int id = lireChoix("ID du menu à archiver: ");

        try {
            menuService.archiverMenu(id);
            afficherLigne("✅ Menu archivé avec succès!");
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de l'archivage: " + e.getMessage());
        }
    }

    private void supprimerMenu() {
        afficherTitre("SUPPRESSION D'UN MENU");

        int id = lireChoix("ID du menu à supprimer: ");

        try {
            Menu menu = menuService.getMenuById(id);
            afficherLigne("Menu à supprimer: " + menu.getNom());
            String confirmation = lireString("Êtes-vous sûr? (oui/non): ");

            if (confirmation.equalsIgnoreCase("oui")) {
                boolean success = menuService.supprimerMenu(id);
                if (success) {
                    afficherLigne("✅ Menu supprimé avec succès!");
                } else {
                    afficherLigne("❌ Erreur lors de la suppression.");
                }
            } else {
                afficherLigne("Suppression annulée.");
            }
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private void rechercherMenu() {
        afficherTitre("RECHERCHE DE MENU");

        String recherche = lireString("Nom à rechercher: ");
        List<Menu> menus = menuService.rechercherMenus(recherche);

        afficherTitre("RÉSULTATS (" + menus.size() + ")");

        if (menus.isEmpty()) {
            afficherLigne("Aucun menu trouvé.");
            return;
        }

        for (Menu menu : menus) {
            afficherLigne(formatMenu(menu));
        }
    }

    private void afficherDetailsMenu() {
        afficherTitre("DÉTAILS D'UN MENU");

        int id = lireChoix("ID du menu: ");

        try {
            Menu menu = menuService.getMenuById(id);
            afficherLigne(formatMenuDetail(menu));
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private String formatMenu(Menu menu) {
        return String.format("ID: %d | %s | %.0f FCFA | %s",
                menu.getId(),
                menu.getNom(),
                menu.getPrix(),
                menu.isEstArchive() ? "[ARCHIVÉ]" : "[ACTIF]"
        );
    }

    private String formatMenuDetail(Menu menu) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(50)).append("\n");
        sb.append("MENU #").append(menu.getId()).append("\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Nom: ").append(menu.getNom()).append("\n");
        sb.append("Prix: ").append(menu.getPrix()).append(" FCFA").append("\n");
        sb.append("Description: ").append(menu.getDescription()).append("\n");
        sb.append("Statut: ").append(menu.isEstArchive() ? "Archivé" : "Actif").append("\n");
        sb.append("Date création: ").append(menu.getDateCreation()).append("\n");

        if (menu.getBurger() != null) {
            sb.append("\nCOMPOSANTS:\n");
            sb.append("- Burger: ").append(menu.getBurger().getNom())
                    .append(" (").append(menu.getBurger().getPrix()).append(" FCFA)\n");
        }

        if (menu.getBoisson() != null) {
            sb.append("- Boisson: ").append(menu.getBoisson().getNom())
                    .append(" (").append(menu.getBoisson().getPrix()).append(" FCFA)\n");
        }

        if (menu.getFrite() != null) {
            sb.append("- Frites: ").append(menu.getFrite().getNom())
                    .append(" (").append(menu.getFrite().getPrix()).append(" FCFA)\n");
        }

        return sb.toString();
    }
}