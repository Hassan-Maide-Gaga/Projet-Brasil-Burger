package org.example.Model;

import org.example.Model.Enum.MethodePaiement;
import java.time.LocalDateTime;

public class Paiement {
    private int id;
    private LocalDateTime date;
    private double montant;
    private MethodePaiement methode;
    private String reference;
    private int commandeId;

    public Paiement() {
        this.date = LocalDateTime.now();
    }

    public Paiement(double montant, MethodePaiement methode, String reference, int commandeId) {
        this();
        this.montant = montant;
        this.methode = methode;
        this.reference = reference;
        this.commandeId = commandeId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public MethodePaiement getMethode() { return methode; }
    public void setMethode(MethodePaiement methode) { this.methode = methode; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }

    @Override
    public String toString() {
        return "Paiement #" + id + " - " + methode + " - " + montant + " FCFA";
    }
}