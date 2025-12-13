package org.example.Model;

import org.example.Model.Enum.RoleUtilisateur;

public class Gestionnaire extends Utilisateur {

    public Gestionnaire() {
        super();
        this.role = RoleUtilisateur.GESTIONNAIRE;
    }

    public Gestionnaire(String nom, String prenom, String telephone, String email, String motDePasse) {
        super(nom, prenom, telephone, email, motDePasse, RoleUtilisateur.GESTIONNAIRE);
    }
}