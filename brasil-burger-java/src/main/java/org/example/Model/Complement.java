// Fichier: src/main/java/org/example/model/Complement.java
package org.example.model;

import org.example.model.enums.StatutArchivage;
import java.time.LocalDateTime;

public class Complement {
    private Integer id;
    private String nom;
    private Double prix;
    private String imageUrl;
    private String imagePublicId;
    private TypeComplement typeComplement;
    private LocalDateTime dateCreation;
    private StatutArchivage statutArchivage;

    public enum TypeComplement {
        BOISSON, FRITE, AUTRE
    }

    // Constructeur vide
    public Complement() {
        this.dateCreation = LocalDateTime.now();
        this.statutArchivage = StatutArchivage.ACTIF;
    }

    // Constructeur avec paramètres
    public Complement(String nom, Double prix, TypeComplement typeComplement) {
        this();
        this.nom = nom;
        this.prix = prix;
        this.typeComplement = typeComplement;
    }

    // Constructeur complet
    public Complement(Integer id, String nom, Double prix, String imageUrl, String imagePublicId,
                      TypeComplement typeComplement, LocalDateTime dateCreation, StatutArchivage statutArchivage) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
        this.typeComplement = typeComplement;
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

    public TypeComplement getTypeComplement() {
        return typeComplement;
    }

    public void setTypeComplement(TypeComplement typeComplement) {
        this.typeComplement = typeComplement;
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
        return String.format("Complement[id=%d, nom='%s', type=%s, prix=%.2f FCFA, statut=%s]",
                id, nom, typeComplement, prix, statutArchivage);
    }
}