// Fichier: src/main/java/org/example/model/enums/StatutArchivage.java
package org.example.model.enums;

public enum StatutArchivage {
    ACTIF("ACTIF"),
    ARCHIVE("ARCHIVE");

    private final String valeur;

    StatutArchivage(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static StatutArchivage fromString(String text) {
        for (StatutArchivage statut : StatutArchivage.values()) {
            if (statut.valeur.equalsIgnoreCase(text)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Aucun statut ne correspond à : " + text);
    }
}