package org.example.Service;

import org.example.Model.Commande;
import org.example.Model.LigneCommande;
import org.example.Model.Enum.StatutCommande;
import org.example.Model.Enum.MethodePaiement;
import java.time.LocalDate;
import java.util.List;

public interface CommandeService {
    Commande creerCommande(Commande commande, List<LigneCommande> lignes);
    Commande getCommandeById(int id);
    List<Commande> getAllCommandes();
    List<Commande> getByStatut(StatutCommande statut);
    boolean annulerCommande(int id);
    boolean changerStatutCommande(int id, StatutCommande nouveauStatut);
    void enregistrerPaiement(int commandeId, double montant, MethodePaiement methode, String reference);
    List<Commande> filtrerParDate(String dateDebut, String dateFin);
    List<Commande> filtrerParClient(String recherche);
    List<Commande> getCommandesDuJour();
    double getChiffreAffairesDuJour();
}