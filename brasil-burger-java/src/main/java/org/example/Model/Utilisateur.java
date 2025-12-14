package org.example.model;

import org.example.model.enums.RoleUtilisateur;
import java.time.LocalDateTime;

public abstract class Utilisateur {
    protected int id;
    protected String nom;
    protected String prenom;
    protected String telephone;
    protected String email;
    protected String motDePasse;
    protected RoleUtilisateur role;
    protected LocalDateTime dateCreation;
    protected boolean estArchive;

    public Utilisateur() {
        this.dateCreation = LocalDateTime.now();
        this.estArchive = false;
    }

    public Utilisateur(String nom, String prenom, String telephone, String email,
                       String motDePasse, RoleUtilisateur role) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public RoleUtilisateur getRole() { return role; }
    public void setRole(RoleUtilisateur role) { this.role = role; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public boolean isEstArchive() { return estArchive; }
    public void setEstArchive(boolean estArchive) { this.estArchive = estArchive; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + role + ")";
    }
}