package org.example.Model;

import org.example.Model.Enum.TypeProduit;

public class Burger extends Produit {

    public Burger() {
        super();
        this.type = TypeProduit.BURGER;
    }

    public Burger(String nom, double prix, String description) {
        super(nom, prix, description, TypeProduit.BURGER);
    }

    public Burger(int id, String nom, double prix, String description, boolean estArchive) {
        super(nom, prix, description, TypeProduit.BURGER);
        this.id = id;
        this.estArchive = estArchive;
    }
}