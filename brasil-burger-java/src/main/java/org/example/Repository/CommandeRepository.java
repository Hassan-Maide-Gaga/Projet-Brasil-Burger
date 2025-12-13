package org.example.Repository;

import org.example.Model.Commande;
import org.example.Model.Enum.StatutCommande;
import org.example.Model.Enum.ModeConsommation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommandeRepository {
    Commande save(Commande commande);
    Commande update(Commande commande);
    boolean delete(int id);
    Optional<Commande> findById(int id);
    List<Commande> findAll();
    List<Commande> findByStatut(StatutCommande statut);
    List<Commande> findByClient(int clientId);
    List<Commande> findByLivreur(int livreurId);
    List<Commande> findByModeConsommation(ModeConsommation mode);
    boolean updateStatut(int id, StatutCommande statut);
    boolean assignerLivreur(int commandeId, int livreurId);
    List<Commande> findByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);
    List<Commande> findByDate(LocalDate date);
    double calculateChiffreAffairesByDate(LocalDate date);
    List<Commande> findCommandesEnAttente();
    List<Commande> findCommandesEnCours();
}