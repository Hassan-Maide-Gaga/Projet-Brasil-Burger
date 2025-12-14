// Fichier: src/main/java/org/example/model/Menu.java
package org.example.model;

import org.example.model.enums.StatutArchivage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private Integer id;
    private String nom;
    private String imageUrl;
    private String imagePublicId;
    private String description;
    private LocalDateTime dateCreation;
    private StatutArchivage statutArchivage;
    private List<MenuComposition> compositions;

    // Constructeur vide
    public Menu() {
        this.dateCreation = LocalDateTime.now();
        this.statutArchivage = StatutArchivage.ACTIF;
        this.compositions = new ArrayList<>();
    }

    // Constructeur avec paramètres
    public Menu(String nom, String description) {
        this();
        this.nom = nom;
        this.description = description;
    }

    // Constructeur complet
    public Menu(Integer id, String nom, String imageUrl, String imagePublicId,
                String description, LocalDateTime dateCreation, StatutArchivage statutArchivage) {
        this.id = id;
        this.nom = nom;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statutArchivage = statutArchivage;
        this.compositions = new ArrayList<>();
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

    public List<MenuComposition> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<MenuComposition> compositions) {
        this.compositions = compositions;
    }

    // Méthodes utilitaires
    public void ajouterComposition(MenuComposition composition) {
        this.compositions.add(composition);
    }

    public boolean estArchive() {
        return this.statutArchivage == StatutArchivage.ARCHIVE;
    }

    public void archiver() {
        this.statutArchivage = StatutArchivage.ARCHIVE;
    }

    public void restaurer() {
        this.statutArchivage = StatutArchivage.ACTIF;
    }

    // Calcul du prix total du menu
    public Double calculerPrixTotal() {
        double total = 0.0;
        for (MenuComposition composition : compositions) {
            if (composition.getBurger() != null) {
                total += composition.getBurger().getPrix();
            }
            if (composition.getComplement() != null) {
                total += composition.getComplement().getPrix();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Menu[id=%d, nom='%s', prix=%.2f FCFA, statut=%s]",
                id, nom, calculerPrixTotal(), statutArchivage);
    }
}