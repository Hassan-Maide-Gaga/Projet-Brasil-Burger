// Fichier: src/main/java/org/example/model/Burger.java
package org.example.model;

import org.example.model.enums.StatutArchivage;
import java.time.LocalDateTime;

public class Burger {
    private Integer id;
    private String nom;
    private Double prix;
    private String imageUrl;
    private String imagePublicId;
    private String description;
    private LocalDateTime dateCreation;
    private StatutArchivage statutArchivage;

    // Constructeur vide
    public Burger() {
        this.dateCreation = LocalDateTime.now();
        this.statutArchivage = StatutArchivage.ACTIF;
    }

    // Constructeur avec paramètres
    public Burger(String nom, Double prix, String description) {
        this();
        this.nom = nom;
        this.prix = prix;
        this.description = description;
    }

    // Constructeur complet
    public Burger(Integer id, String nom, Double prix, String imageUrl, String imagePublicId,
                  String description, LocalDateTime dateCreation, StatutArchivage statutArchivage) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statutArchivage = statutArchivage;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public StatutArchivage getStatutArchivage() {
        return statutArchivage;
    }

    public void setStatutArchivage(StatutArchivage statutArchivage) {
        this.statutArchivage = statutArchivage;
    }

    // Méthodes utilitaires
    public boolean estArchive() {
        return this.statutArchivage == StatutArchivage.ARCHIVE;
    }

    public void archiver() {
        this.statutArchivage = StatutArchivage.ARCHIVE;
    }

    public void restaurer() {
        this.statutArchivage = StatutArchivage.ACTIF;
    }

    @Override
    public String toString() {
        return String.format("Burger[id=%d, nom='%s', prix=%.2f FCFA, statut=%s]",
                id, nom, prix, statutArchivage);
    }
}