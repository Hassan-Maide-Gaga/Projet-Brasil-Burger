package org.example.Model;

import org.example.Model.Enum.StatutCommande;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Livraison {
    private int id;
    private LocalDateTime dateHeure;
    private StatutCommande statut;
    private int zoneId;
    private int livreurId;

    // Navigation
    private Zone zone;
    private Livreur livreur;
    private List<Commande> commandes;

    public Livraison() {
        this.dateHeure = LocalDateTime.now();
        this.statut = StatutCommande.EN_ATTENTE;
        this.commandes = new ArrayList<>();
    }

    public Livraison(int zoneId, int livreurId) {
        this();
        this.zoneId = zoneId;
        this.livreurId = livreurId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }

    public int getZoneId() { return zoneId; }
    public void setZoneId(int zoneId) { this.zoneId = zoneId; }

    public int getLivreurId() { return livreurId; }
    public void setLivreurId(int livreurId) { this.livreurId = livreurId; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public Livreur getLivreur() { return livreur; }
    public void setLivreur(Livreur livreur) { this.livreur = livreur; }

    public List<Commande> getCommandes() { return commandes; }
    public void setCommandes(List<Commande> commandes) { this.commandes = commandes; }

    public void ajouterCommande(Commande commande) {
        this.commandes.add(commande);
    }

    @Override
    public String toString() {
        return "Livraison #" + id + " - " + statut + " - " + commandes.size() + " commande(s)";
    }
}