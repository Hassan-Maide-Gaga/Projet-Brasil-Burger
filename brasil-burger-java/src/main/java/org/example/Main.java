package org.example;

import org.example.Vue.MenuVue;
import org.example.Vue.ComplementVue;
import org.example.Vue.CommandeVue;
import org.example.Vue.ClientVue;
import org.example.Vue.Vue;
import org.example.config.database.Database;
import org.example.config.factory.database.DatabaseFactory;
import org.example.Vue.BurgerVue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   BRASIL BURGER - GESTION DES COMMANDES");
        System.out.println("=========================================");

        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // Valeurs par défaut pour le développement local
        if (dbHost == null) dbHost = "localhost";
        if (dbPort == null) dbPort = "3306";
        if (dbName == null) dbName = "brasilburger";
        if (dbUser == null) dbUser = "root";
        if (dbPassword == null) dbPassword = "rootpassword";

        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
                dbHost, dbPort, dbName
        );
        try {
            System.out.println("🔗 Connexion à la base de données...");
            System.out.println("URL: " + jdbcUrl);
            System.out.println("User: " + dbUser);

            Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            System.out.println("✅ Connexion à MySQL établie avec succès !");

            // Ici, exécutez votre logique d'application
            runApplication(connection);

            connection.close();
            System.out.println("👋 Application terminée avec succès !");

        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion à la base de données:");
            e.printStackTrace();
            System.exit(1);
        }

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
    private static void runApplication(Connection connection) {
        // Votre logique métier ici
        System.out.println("🎯 Exécution de l'application Brasil Burger...");

        try {
            // Exemple: créer une table si elle n'existe pas
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS produits (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nom VARCHAR(100) NOT NULL,
                    prix DECIMAL(10,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

            connection.createStatement().execute(createTableSQL);
            System.out.println("✅ Table 'produits' créée ou déjà existante");

            // Ajouter des données de test
            String insertSQL = """
                INSERT INTO produits (nom, prix) 
                VALUES ('Burger Classique', 9.99),
                       ('Frites', 3.99),
                       ('Boisson', 2.49)
                """;

            int rows = connection.createStatement().executeUpdate(insertSQL);
            System.out.println("📊 " + rows + " produits insérés");

            // Lire les données
            var resultSet = connection.createStatement().executeQuery("SELECT * FROM produits");
            System.out.println("\n📋 Liste des produits:");
            while (resultSet.next()) {
                System.out.printf("  - %s: %.2f€%n",
                        resultSet.getString("nom"),
                        resultSet.getDouble("prix"));
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'exécution:");
            e.printStackTrace();
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