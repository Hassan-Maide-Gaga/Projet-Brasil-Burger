package org.example.Model;

import org.example.Model.Enum.TypeProduit;

public class Menu extends Produit {
    private int burgerId;
    private int boissonId;
    private int friteId;

    // Navigation
    private Burger burger;
    private Complement boisson;
    private Complement frite;

    public Menu() {
        super();
        this.type = TypeProduit.MENU;
    }

    public Menu(String nom, double prix, String description, int burgerId, int boissonId, int friteId) {
        super(nom, prix, description, TypeProduit.MENU);
        this.burgerId = burgerId;
        this.boissonId = boissonId;
        this.friteId = friteId;
    }

    // Getters et Setters
    public int getBurgerId() { return burgerId; }
    public void setBurgerId(int burgerId) { this.burgerId = burgerId; }

    public int getBoissonId() { return boissonId; }
    public void setBoissonId(int boissonId) { this.boissonId = boissonId; }

    public int getFriteId() { return friteId; }
    public void setFriteId(int friteId) { this.friteId = friteId; }

    public Burger getBurger() { return burger; }
    public void setBurger(Burger burger) { this.burger = burger; }

    public Complement getBoisson() { return boisson; }
    public void setBoisson(Complement boisson) { this.boisson = boisson; }

    public Complement getFrite() { return frite; }
    public void setFrite(Complement frite) { this.frite = frite; }
}