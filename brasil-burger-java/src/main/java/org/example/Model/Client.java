package org.example.Model;

import org.example.Model.Enum.RoleUtilisateur;

public class Client extends Utilisateur {
    private String adresse;

    public Client() {
        super();
        this.role = RoleUtilisateur.CLIENT;
    }

    public Client(String nom, String prenom, String telephone, String email,
                  String motDePasse, String adresse) {
        super(nom, prenom, telephone, email, motDePasse, RoleUtilisateur.CLIENT);
        this.adresse = adresse;
    }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}