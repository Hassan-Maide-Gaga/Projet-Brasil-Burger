package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Zone {
    private int id;
    private String nom;
    private double prix;
    private List<String> quartiers;

    public Zone() {
        this.quartiers = new ArrayList<>();
    }

    public Zone(String nom, double prix) {
        this();
        this.nom = nom;
        this.prix = prix;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public List<String> getQuartiers() { return quartiers; }
    public void setQuartiers(List<String> quartiers) { this.quartiers = quartiers; }

    public void ajouterQuartier(String quartier) {
        this.quartiers.add(quartier);
    }

    @Override
    public String toString() {
        return nom + " - " + prix + " FCFA";
    }
}