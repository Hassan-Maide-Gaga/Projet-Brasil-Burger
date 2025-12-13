package org.example.Model;

import org.example.Model.Enum.RoleUtilisateur;

public class Livreur extends Utilisateur {
    private String vehicule;
    private boolean estDisponible;

    public Livreur() {
        super();
        this.role = RoleUtilisateur.LIVREUR;
        this.estDisponible = true;
    }

    public Livreur(String nom, String prenom, String telephone, String email,
                   String motDePasse, String vehicule) {
        super(nom, prenom, telephone, email, motDePasse, RoleUtilisateur.LIVREUR);
        this.vehicule = vehicule;
        this.estDisponible = true;
    }

    public String getVehicule() { return vehicule; }
    public void setVehicule(String vehicule) { this.vehicule = vehicule; }

    public boolean isEstDisponible() { return estDisponible; }
    public void setEstDisponible(boolean estDisponible) { this.estDisponible = estDisponible; }
}