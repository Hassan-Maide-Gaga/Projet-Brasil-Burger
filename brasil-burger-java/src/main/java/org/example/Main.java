package org.example;

import org.example.config.Database;
import org.example.factory.database.DatabaseFactory;
import org.example.vue.BurgerVue;
import org.example.vue.ComplementVue;
import org.example.vue.MenuVue;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        afficherBanniere();

        // Test de connexion à la base de données
        Database database = DatabaseFactory.getInstance();
        if (!database.isConnected()) {
            System.err.println("\n✗ Échec de connexion à la base de données.");
            System.err.println("Veuillez vérifier vos paramètres dans application.properties");
            return;
        }

        boolean continuer = true;

        while (continuer) {
            afficherMenuPrincipal();

            int choix = lireChoix();

            switch (choix) {
                case 1:
                    new BurgerVue().afficher();
                    break;
                case 2:
                    new MenuVue().afficher();
                    break;
                case 3:
                    new ComplementVue().afficher();
                    break;
                case 0:
                    continuer = false;
                    afficherAuRevoir();
                    break;
                default:
                    System.err.println("\n✗ Choix invalide. Veuillez réessayer.");
            }
        }

        database.closeConnection();
        scanner.close();
    }

    private static void afficherBanniere() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     BRASIL BURGER - GESTION DES COMMANDES");
        System.out.println("=".repeat(60));
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  MENU PRINCIPAL");
        System.out.println("=".repeat(60));
        System.out.println("1. Gestion des Burgers");
        System.out.println("2. Gestion des Menus");
        System.out.println("3. Gestion des Compléments");
        System.out.println("0. Quitter");
        System.out.println("-".repeat(60));
    }

    private static int lireChoix() {
        try {
            System.out.print("Votre choix: ");
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void afficherAuRevoir() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     Merci d'avoir utilisé Brasil Burger");
        System.out.println("     À bientôt!");
        System.out.println("=".repeat(60) + "\n");
    }
}