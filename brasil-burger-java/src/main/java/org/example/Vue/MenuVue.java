// Fichier: src/main/java/org/example/vue/MenuVue.java
package org.example.vue;

import org.example.model.Burger;
import org.example.model.Complement;
import org.example.model.Menu;
import org.example.service.BurgerService;
import org.example.service.ComplementService;
import org.example.service.MenuService;

import java.io.File;
import java.util.List;

public class MenuVue extends Vue {
    private final MenuService menuService;
    private final BurgerService burgerService;
    private final ComplementService complementService;

    public MenuVue() {
        super();
        this.menuService = new MenuService();
        this.burgerService = new BurgerService();
        this.complementService = new ComplementService();
    }

    @Override
    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            afficherMenuMenu();
            Integer choix = lireEntier("\nVotre choix");

            if (choix == null) {
                continue;
            }

            switch (choix) {
                case 1:
                    creerMenu();
                    break;
                case 2:
                    listerMenus();
                    break;
                case 3:
                    listerMenusActifs();
                    break;
                case 4:
                    afficherDetailsMenu();
                    break;
                case 5:
                    modifierMenu();
                    break;
                case 6:
                    modifierComposition();
                    break;
                case 7:
                    archiverMenu();
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

    private void afficherMenuMenu() {
        afficherTitre("GESTION DES MENUS");
        System.out.println("1. Créer un menu");
        System.out.println("2. Lister tous les menus");
        System.out.println("3. Lister les menus actifs");
        System.out.println("4. Afficher détails d'un menu");
        System.out.println("5. Modifier un menu");
        System.out.println("6. Modifier la composition d'un menu");
        System.out.println("7. Archiver un menu");
        System.out.println("0. Retour");
        afficherSeparateur();
    }

    private void creerMenu() {
        afficherTitre("CRÉER UN MENU");

        String nom = lireChaine("Nom du menu");
        if (nom.isEmpty()) {
            afficherErreur("Le nom est obligatoire");
            return;
        }

        String description = lireChaine("Description (optionnel)");

        Menu menu = menuService.creerMenu(nom, description);

        if (menu == null) {
            afficherErreur("Échec de la création du menu");
            return;
        }

        afficherSucces("Menu créé avec succès (ID: " + menu.getId() + ")");

        // Ajouter la composition
        if (confirmer("Voulez-vous ajouter la composition du menu maintenant ?")) {
            ajouterComposition(menu.getId());
        }

        // Ajouter une image
        if (confirmer("Voulez-vous ajouter une image ?")) {
            String cheminImage = lireChaine("Chemin complet de l'image");
            File imageFile = new File(cheminImage);

            if (imageFile.exists()) {
                menuService.modifierImageMenu(menu.getId(), imageFile);
            }
        }
    }

    private void ajouterComposition(Integer menuId) {
        // Sélectionner un burger
        List<Burger> burgers = burgerService.listerBurgersActifs();
        if (burgers.isEmpty()) {
            afficherErreur("Aucun burger disponible. Créez d'abord un burger.");
            return;
        }

        System.out.println("\n--- Burgers disponibles ---");
        for (Burger b : burgers) {
            System.out.println(b.getId() + ". " + b.getNom() + " - " + b.getPrix() + " FCFA");
        }

        Integer burgerId = lireEntier("Sélectionnez un burger (ID)");
        if (burgerId == null) {
            return;
        }

        menuService.ajouterBurgerAuMenu(menuId, burgerId);

        // Sélectionner une boisson
        List<Complement> boissons = complementService.listerBoissons();
        if (!boissons.isEmpty()) {
            System.out.println("\n--- Boissons disponibles ---");
            for (Complement c : boissons) {
                System.out.println(c.getId() + ". " + c.getNom() + " - " + c.getPrix() + " FCFA");
            }

            Integer boissonId = lireEntier("Sélectionnez une boisson (ID)");
            if (boissonId != null) {
                menuService.ajouterBoissonAuMenu(menuId, boissonId);
            }
        }

        // Sélectionner des frites
        List<Complement> frites = complementService.listerFrites();
        if (!frites.isEmpty()) {
            System.out.println("\n--- Frites disponibles ---");
            for (Complement c : frites) {
                System.out.println(c.getId() + ". " + c.getNom() + " - " + c.getPrix() + " FCFA");
            }

            Integer friteId = lireEntier("Sélectionnez des frites (ID)");
            if (friteId != null) {
                menuService.ajouterFriteAuMenu(menuId, friteId);
            }
        }

        afficherSucces("Composition ajoutée avec succès");
    }

    private void listerMenus() {
        afficherTitre("LISTE DE TOUS LES MENUS");

        List<Menu> menus = menuService.listerMenus();

        if (menus.isEmpty()) {
            afficherMessage("Aucun menu trouvé");
            return;
        }

        afficherListeMenus(menus);
    }

    private void listerMenusActifs() {
        afficherTitre("LISTE DES MENUS ACTIFS");

        List<Menu> menus = menuService.listerMenusActifs();

        if (menus.isEmpty()) {
            afficherMessage("Aucun menu actif trouvé");
            return;
        }

        afficherListeMenus(menus);
    }

    private void afficherListeMenus(List<Menu> menus) {
        System.out.println(String.format("\n%-5s %-30s %-15s %-15s", "ID", "Nom", "Prix (FCFA)", "Statut"));
        afficherSeparateur();

        for (Menu menu : menus) {
            System.out.println(String.format("%-5d %-30s %-15.2f %-15s",
                    menu.getId(),
                    menu.getNom(),
                    menu.calculerPrixTotal(),
                    menu.getStatutArchivage()));
        }

        System.out.println("\nTotal: " + menus.size() + " menu(s)");
    }

    private void afficherDetailsMenu() {
        afficherTitre("DÉTAILS D'UN MENU");

        Integer id = lireEntier("ID du menu");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        menuService.afficherDetailsMenu(id);
    }

    private void modifierMenu() {
        afficherTitre("MODIFIER UN MENU");

        Integer id = lireEntier("ID du menu à modifier");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Menu menu = menuService.obtenirMenu(id);
        if (menu == null) {
            afficherErreur("Menu introuvable");
            return;
        }

        menuService.afficherDetailsMenu(id);
        System.out.println("\n(Laissez vide pour conserver la valeur actuelle)");

        String nom = lireChaine("Nouveau nom");
        String description = lireChaine("Nouvelle description");

        Menu modifie = menuService.modifierMenu(id,
                nom.isEmpty() ? null : nom,
                description.isEmpty() ? null : description);

        if (modifie != null) {
            afficherSucces("Menu modifié avec succès");

            if (confirmer("Voulez-vous modifier l'image ?")) {
                String cheminImage = lireChaine("Chemin complet de la nouvelle image");
                File imageFile = new File(cheminImage);

                if (imageFile.exists()) {
                    if (menuService.modifierImageMenu(id, imageFile)) {
                        afficherSucces("Image modifiée avec succès");
                    }
                }
            }
        }
    }

    private void modifierComposition() {
        afficherTitre("MODIFIER LA COMPOSITION D'UN MENU");

        Integer menuId = lireEntier("ID du menu");
        if (menuId == null) {
            afficherErreur("ID invalide");
            return;
        }

        menuService.afficherDetailsMenu(menuId);

        if (confirmer("\nVoulez-vous modifier la composition ?")) {
            ajouterComposition(menuId);
        }
    }

    private void archiverMenu() {
        afficherTitre("ARCHIVER UN MENU");

        Integer id = lireEntier("ID du menu à archiver");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Menu menu = menuService.obtenirMenu(id);
        if (menu == null) {
            afficherErreur("Menu introuvable");
            return;
        }

        menuService.afficherDetailsMenu(id);

        if (confirmer("\nConfirmez-vous l'archivage de ce menu ?")) {
            if (menuService.archiverMenu(id)) {
                afficherSucces("Menu archivé avec succès");
            } else {
                afficherErreur("Échec de l'archivage");
            }
        }
    }
}