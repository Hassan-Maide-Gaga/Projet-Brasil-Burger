package org.example.Model;

import org.example.Model.Enum.TypeProduit;
import java.time.LocalDateTime;

public abstract class Produit {
    protected int id;
    protected String nom;
    protected double prix;
    protected String image;
    protected String description;
    protected TypeProduit type;
    protected boolean estArchive;
    protected LocalDateTime dateCreation;

    public Produit() {
        this.dateCreation = LocalDateTime.now();
        this.estArchive = false;
    }

    public Produit(String nom, double prix, String description, TypeProduit type) {
        this();
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.type = type;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TypeProduit getType() { return type; }
    public void setType(TypeProduit type) { this.type = type; }

    public boolean isEstArchive() { return estArchive; }
    public void setEstArchive(boolean estArchive) { this.estArchive = estArchive; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    @Override
    public String toString() {
        return nom + " - " + prix + " FCFA";
    }
}