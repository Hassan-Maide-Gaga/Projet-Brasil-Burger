package org.example.Model;

import org.example.Model.Enum.TypeComplement;
import org.example.Model.Enum.TypeProduit;

public class Complement extends Produit {
    private TypeComplement typeComplement;
    private String taille; // Pour boissons/frites

    public Complement() {
        super();
        this.type = TypeProduit.COMPLEMENT;
    }

    public Complement(String nom, double prix, String description, TypeComplement typeComplement) {
        super(nom, prix, description, TypeProduit.COMPLEMENT);
        this.typeComplement = typeComplement;
    }

    public Complement(String nom, double prix, String description, TypeComplement typeComplement, String taille) {
        this(nom, prix, description, typeComplement);
        this.taille = taille;
    }

    // Getters et Setters
    public TypeComplement getTypeComplement() { return typeComplement; }
    public void setTypeComplement(TypeComplement typeComplement) { this.typeComplement = typeComplement; }

    public String getTaille() { return taille; }
    public void setTaille(String taille) { this.taille = taille; }

    @Override
    public String toString() {
        return nom + " (" + typeComplement + ") - " + prix + " FCFA" +
                (taille != null ? " - Taille: " + taille : "");
    }
}