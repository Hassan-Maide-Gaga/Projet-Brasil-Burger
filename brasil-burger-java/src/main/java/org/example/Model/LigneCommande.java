package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class LigneCommande {
    private int id;
    private int quantite;
    private double prixUnitaire;
    private int commandeId;
    private int produitId;

    // Navigation
    private Produit produit;
    private List<Complement> complements;

    public LigneCommande() {
        this.complements = new ArrayList<>();
    }

    public LigneCommande(int quantite, double prixUnitaire, int commandeId, int produitId) {
        this();
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.commandeId = commandeId;
        this.produitId = produitId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }

    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public List<Complement> getComplements() { return complements; }
    public void setComplements(List<Complement> complements) { this.complements = complements; }

    public void ajouterComplement(Complement complement) {
        this.complements.add(complement);
    }

    public double getSousTotal() {
        double sousTotal = quantite * prixUnitaire;
        for (Complement c : complements) {
            sousTotal += c.getPrix();
        }
        return sousTotal;
    }
}