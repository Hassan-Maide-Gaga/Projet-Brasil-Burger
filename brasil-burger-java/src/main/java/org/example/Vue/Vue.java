package org.example.Vue;

import java.util.Scanner;

public abstract class Vue {
    protected static Scanner scanner = new Scanner(System.in);

    protected static void afficherLigne(String message) {
        System.out.println(message);
    }

    public static void afficherTitre(String titre) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(titre);
        System.out.println("=".repeat(50));
    }

    public static void afficherMenu(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Retour");
    }

    public static int lireChoix(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            scanner.next();
        }
        int choix = scanner.nextInt();
        scanner.nextLine(); // consommer le retour à la ligne
        return choix;
    }

    public static String lireString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    protected static double lireDouble(String message) {
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            scanner.next();
        }
        double valeur = scanner.nextDouble();
        scanner.nextLine(); // consommer le retour à la ligne
        return valeur;
    }
}