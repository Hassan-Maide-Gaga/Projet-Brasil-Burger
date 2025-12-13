package org.example;

import org.example.Vue.MenuVue;
import org.example.Vue.ComplementVue;
import org.example.Vue.CommandeVue;
import org.example.Vue.ClientVue;
import org.example.Vue.Vue;
import org.example.config.database.Database;
import org.example.config.factory.database.DatabaseFactory;
import org.example.Vue.BurgerVue;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   BRASIL BURGER - GESTION DES COMMANDES");
        System.out.println("=========================================");

        // Test de connexion à la base de données
        try {
            Database database = DatabaseFactory.getInstance();
            if (database.isConnected()) {
                System.out.println("✅ Connexion à la base de données établie.");
            } else {
                System.out.println("❌ Échec de connexion à la base de données.");
                System.out.println("Veuillez vérifier:");
                System.out.println("1. Que MySQL/PostgreSQL est lancé");
                System.out.println("2. Les identifiants dans EntityManager.java");
                System.out.println("3. La base 'brasil_burger' existe");
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion: " + e.getMessage());
            System.out.println("Assurez-vous d'avoir exécuté le script SQL:");
            System.out.println("mysql -u root -p < database/schema.sql");
            return;
        }

        // Menu principal
        boolean continuer = true;

        while (continuer) {
            Vue.afficherTitre("MENU PRINCIPAL - BRASIL BURGER");
            String[] options = {
                    "🍔  Gestion des Burgers",
                    "📋  Gestion des Menus",
                    "🥤  Gestion des Compléments",
                    "🛒  Gestion des Commandes",
                    "👤  Gestion des Clients",
                    "📊  Statistiques",
                    "❌  Quitter"
            };
            Vue.afficherMenu(options);

            int choix = Vue.lireChoix("\n🎯 Votre choix: ");

            switch (choix) {
                case 1:
                    BurgerVue burgerVue = new BurgerVue();
                    burgerVue.afficherMenuBurger();
                    break;
                case 2:
                    MenuVue menuVue = new MenuVue();
                    menuVue.afficherMenuMenu();
                    break;
                case 3:
                    ComplementVue complementVue = new ComplementVue();
                    complementVue.afficherMenuComplement();
                    break;
                case 4:
                    CommandeVue commandeVue = new CommandeVue();
                    commandeVue.afficherMenuCommande();
                    break;
                case 5:
                    ClientVue clientVue = new ClientVue();
                    clientVue.afficherMenuClient();
                    break;
                case 6:
                    afficherStatistiques();
                    break;
                case 7:
                    continuer = false;
                    System.out.println("\n👋 Au revoir! À bientôt chez Brasil Burger!");
                    break;
                default:
                    System.out.println("❌ Choix invalide! Veuillez choisir entre 1 et 7.");
            }
        }

        // Fermer la connexion à la base de données
        try {
            Database database = DatabaseFactory.getInstance();
            database.closeConnection();
            System.out.println("✅ Connexion à la base de données fermée.");
        } catch (Exception e) {
            System.out.println("⚠ Impossible de fermer la connexion: " + e.getMessage());
        }
    }

    private static void afficherStatistiques() {
        Vue.afficherTitre("📊 STATISTIQUES - EN DÉVELOPPEMENT");
        System.out.println("🚧 Cette fonctionnalité sera pleinement implémentée dans le livrable Symfony.");
        System.out.println("\n📈 Les statistiques qui seront disponibles:");
        System.out.println("─────────────────────────────────────");
        System.out.println("✅ Commandes en cours de la journée");
        System.out.println("✅ Commandes validées de la journée");
        System.out.println("✅ Recettes journalières");
        System.out.println("✅ Burgers les plus vendus");
        System.out.println("✅ Commandes annulées du jour");
        System.out.println("\n💡 Pour l'instant, vous pouvez:");
        System.out.println("- Voir les commandes par statut");
        System.out.println("- Filtrer les commandes par date");
        System.out.println("- Consulter l'historique des commandes");

        System.out.println("\n🎯 Exemple d'utilisation:");
        System.out.println("1. Allez dans 'Gestion des Commandes'");
        System.out.println("2. Choisissez 'Lister par statut'");
        System.out.println("3. Sélectionnez un statut pour voir les commandes");

        Vue.lireString("\nAppuyez sur Entrée pour continuer...");
    }

    // Méthode utilitaire pour afficher les infos système
    private static void afficherInfoSysteme() {
        System.out.println("\nℹ️  Informations système:");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Architecture: " + System.getProperty("os.arch"));
    }
}