// Fichier: src/main/java/org/example/vue/Vue.java
package org.example.vue;

import java.util.Scanner;

public abstract class Vue {
    protected Scanner scanner;

    public Vue() {
        this.scanner = new Scanner(System.in);
    }

    protected void afficherTitre(String titre) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + titre);
        System.out.println("=".repeat(60));
    }

    protected void afficherSeparateur() {
        System.out.println("-".repeat(60));
    }

    protected void afficherMessage(String message) {
        System.out.println("\n" + message);
    }

    protected void afficherSucces(String message) {
        System.out.println("\n✓ " + message);
    }

    protected void afficherErreur(String message) {
        System.err.println("\n✗ " + message);
    }

    protected String lireChaine(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    protected Integer lireEntier(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer un nombre entier valide");
            }
        }
    }

    protected Double lireDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer un nombre décimal valide");
            }
        }
    }

    protected boolean confirmer(String message) {
        System.out.print(message + " (O/N): ");
        String reponse = scanner.nextLine().trim().toUpperCase();
        return reponse.equals("O") || reponse.equals("OUI");
    }

    protected void pause() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    protected void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si le clear échoue, on affiche simplement plusieurs lignes vides
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public abstract void afficher();
}