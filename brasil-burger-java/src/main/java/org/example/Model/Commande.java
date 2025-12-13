package org.example.Model;

import org.example.Model.Enum.StatutCommande;
import org.example.Model.Enum.ModeConsommation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private LocalDateTime date;
    private StatutCommande statut;
    private ModeConsommation modeConsommation;
    private String adresseLivraison;
    private double total;
    private int clientId;
    private int livreurId;

    // Navigation
    private Client client;
    private Livreur livreur;
    private List<LigneCommande> lignes;
    private Paiement paiement;

    public Commande() {
        this.date = LocalDateTime.now();
        this.statut = StatutCommande.EN_ATTENTE;
        this.lignes = new ArrayList<>();
    }

    public Commande(ModeConsommation modeConsommation, int clientId) {
        this();
        this.modeConsommation = modeConsommation;
        this.clientId = clientId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }

    public ModeConsommation getModeConsommation() { return modeConsommation; }
    public void setModeConsommation(ModeConsommation modeConsommation) { this.modeConsommation = modeConsommation; }

    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getLivreurId() { return livreurId; }
    public void setLivreurId(int livreurId) { this.livreurId = livreurId; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Livreur getLivreur() { return livreur; }
    public void setLivreur(Livreur livreur) { this.livreur = livreur; }

    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }

    public Paiement getPaiement() { return paiement; }
    public void setPaiement(Paiement paiement) { this.paiement = paiement; }

    public void ajouterLigne(LigneCommande ligne) {
        this.lignes.add(ligne);
        recalculerTotal();
    }

    private void recalculerTotal() {
        this.total = lignes.stream()
                .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
                .sum();
    }

    @Override
    public String toString() {
        return "Commande #" + id + " - " + statut + " - Total: " + total + " FCFA";
    }
}