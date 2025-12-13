package org.example.Service;

import org.example.Model.Commande;
import org.example.Model.LigneCommande;
import org.example.Model.Client;
import org.example.Model.Paiement;
import org.example.Model.Enum.StatutCommande;
import org.example.Model.Enum.MethodePaiement;
import org.example.Repository.CommandeRepository;
import org.example.Repository.CommandeRepositoryImpl;
//import org.example.Repository.LigneCommandeRepository;
//import org.example.Repository.LigneCommandeRepositoryImpl;
import org.example.Repository.ClientRepository;
import org.example.Repository.ClientRepositoryImpl;
//import org.example.Repository.PaiementRepository;
//import org.example.Repository.PaiementRepositoryImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    //private final LigneCommandeRepository ligneCommandeRepository;
    private final ClientRepository clientRepository;
   // private final PaiementRepository paiementRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CommandeServiceImpl() {
        this.commandeRepository = new CommandeRepositoryImpl();
        //this.ligneCommandeRepository = new LigneCommandeRepositoryImpl();
        this.clientRepository = new ClientRepositoryImpl();
        //this.paiementRepository = new PaiementRepositoryImpl();
    }

    @Override
    public Commande creerCommande(Commande commande, List<LigneCommande> lignes) {
        // Validation
        if (commande == null) {
            throw new IllegalArgumentException("La commande ne peut pas être nulle");
        }
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins une ligne");
        }
        if (commande.getClientId() <= 0) {
            throw new IllegalArgumentException("Le client est requis");
        }

        // Vérifier que le client existe
        Optional<Client> client = clientRepository.findById(commande.getClientId());
        if (!client.isPresent()) {
            throw new IllegalArgumentException("Client introuvable avec l'ID: " + commande.getClientId());
        }

        // Calculer le total de la commande
        double total = lignes.stream()
                .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
                .sum();

        commande.setTotal(total);
        commande.setDate(LocalDateTime.now());
        commande.setStatut(StatutCommande.EN_ATTENTE);

        // Sauvegarder la commande
        Commande commandeSauvegardee = commandeRepository.save(commande);

        if (commandeSauvegardee != null && commandeSauvegardee.getId() > 0) {
            // Sauvegarder les lignes de commande
            for (LigneCommande ligne : lignes) {
                ligne.setCommandeId(commandeSauvegardee.getId());
                //ligneCommandeRepository.save(ligne);
            }

            commandeSauvegardee.setLignes(lignes);
        }

        return commandeSauvegardee;
    }

    @Override
    public Commande getCommandeById(int id) {
        Optional<Commande> commande = commandeRepository.findById(id);
        if (!commande.isPresent()) {
            throw new IllegalArgumentException("Commande introuvable avec l'ID: " + id);
        }

        // Charger les lignes de commande
        Commande cmd = commande.get();
        //List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(id);
        //cmd.setLignes(lignes);

        // Charger le paiement si existe
        //Optional<Paiement> paiement = paiementRepository.findByCommandeId(id);
        //paiement.ifPresent(cmd::setPaiement);

        return cmd;
    }

    @Override
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @Override
    public List<Commande> getByStatut(StatutCommande statut) {
        if (statut == null) {
            throw new IllegalArgumentException("Le statut est requis");
        }
        return commandeRepository.findByStatut(statut);
    }

    @Override
    public boolean annulerCommande(int id) {
        Optional<Commande> commande = commandeRepository.findById(id);
        if (!commande.isPresent()) {
            throw new IllegalArgumentException("Commande introuvable avec l'ID: " + id);
        }

        Commande cmd = commande.get();

        // Vérifier que la commande peut être annulée
        if (cmd.getStatut() == StatutCommande.LIVREE ||
                cmd.getStatut() == StatutCommande.ANNULEE) {
            throw new IllegalStateException("Cette commande ne peut pas être annulée");
        }

        return commandeRepository.updateStatut(id, StatutCommande.ANNULEE);
    }

    @Override
    public boolean changerStatutCommande(int id, StatutCommande nouveauStatut) {
        if (nouveauStatut == null) {
            throw new IllegalArgumentException("Le nouveau statut est requis");
        }

        Optional<Commande> commande = commandeRepository.findById(id);
        if (!commande.isPresent()) {
            throw new IllegalArgumentException("Commande introuvable avec l'ID: " + id);
        }

        Commande cmd = commande.get();

        // Vérifier la cohérence du changement de statut
        if (cmd.getStatut() == StatutCommande.ANNULEE) {
            throw new IllegalStateException("Une commande annulée ne peut pas changer de statut");
        }

        if (cmd.getStatut() == StatutCommande.LIVREE && nouveauStatut != StatutCommande.LIVREE) {
            throw new IllegalStateException("Une commande livrée ne peut pas changer de statut");
        }

        return commandeRepository.updateStatut(id, nouveauStatut);
    }

    @Override
    public void enregistrerPaiement(int commandeId, double montant,
                                    MethodePaiement methode, String reference) {
        // Validation
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (methode == null) {
            throw new IllegalArgumentException("La méthode de paiement est requise");
        }

        Optional<Commande> commande = commandeRepository.findById(commandeId);
        if (!commande.isPresent()) {
            throw new IllegalArgumentException("Commande introuvable avec l'ID: " + commandeId);
        }

        Commande cmd = commande.get();

        // Vérifier que le montant correspond au total
        if (montant < cmd.getTotal()) {
            throw new IllegalArgumentException("Le montant payé est insuffisant");
        }

        // Créer le paiement
       /* Paiement paiement = new Paiement();
        paiement.setCommandeId(commandeId);
        paiement.setMontant(montant);
        paiement.setMethodePaiement(methode);
        paiement.setReferencePaiement(reference);
        paiement.setDatePaiement(LocalDateTime.now());*/

        //paiementRepository.save(paiement);

        // Si le paiement est en espèces et qu'il y a de la monnaie
        if (montant > cmd.getTotal()) {
            double monnaie = montant - cmd.getTotal();
            System.out.println("Monnaie à rendre: " + monnaie + " FCFA");
        }
    }

    @Override
    public List<Commande> filtrerParDate(String dateDebut, String dateFin) {
        try {
            LocalDateTime debut = LocalDate.parse(dateDebut, DATE_FORMATTER).atStartOfDay();
            LocalDateTime fin = LocalDate.parse(dateFin, DATE_FORMATTER).atTime(23, 59, 59);

            return commandeRepository.findByDateRange(debut, fin);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez le format yyyy-MM-dd");
        }
    }

    @Override
    public List<Commande> filtrerParClient(String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return getAllCommandes();
        }

        // Rechercher d'abord les clients correspondants
        List<Client> clients = clientRepository.Search(recherche.trim());

        if (clients.isEmpty()) {
            return List.of();
        }

        // Récupérer les commandes du premier client trouvé
        return commandeRepository.findByClient(clients.get(0).getId());
    }

    @Override
    public List<Commande> getCommandesDuJour() {
        LocalDate aujourdhui = LocalDate.now();
        return commandeRepository.findByDate(aujourdhui);
    }

    @Override
    public double getChiffreAffairesDuJour() {
        LocalDate aujourdhui = LocalDate.now();
        return commandeRepository.calculateChiffreAffairesByDate(aujourdhui);
    }
}