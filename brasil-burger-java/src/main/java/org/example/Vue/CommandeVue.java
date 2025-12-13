package org.example.Vue;

import org.example.Model.*;
import org.example.Model.Enum.*;
import org.example.Service.*;
import org.example.config.factory.service.ServiceFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeVue extends Vue {

    private final CommandeService commandeService;
    private final BurgerService burgerService;
    private final MenuService menuService;
    private final ComplementService complementService;
    private final ClientService clientService;

    public CommandeVue() {
        this.commandeService = ServiceFactory.getCommandeService();
        this.burgerService = ServiceFactory.getBurgerService();
        this.menuService = ServiceFactory.getMenuService();
        this.complementService = ServiceFactory.getComplementService();
        this.clientService = ServiceFactory.getClientService();
    }

    public void afficherMenuCommande() {
        boolean continuer = true;

        while (continuer) {
            afficherTitre("GESTION DES COMMANDES");
            String[] options = {
                    "Créer une commande",
                    "Lister toutes les commandes",
                    "Lister par statut",
                    "Rechercher une commande",
                    "Afficher détails commande",
                    "Modifier statut commande",
                    "Annuler une commande",
                    "Ajouter paiement",
                    "Filtrer par date",
                    "Filtrer par client"
            };
            afficherMenu(options);

            int choix = lireChoix("\nVotre choix: ");

            switch (choix) {
                case 1:
                    creerCommande();
                    break;
                case 2:
                    listerToutesCommandes();
                    break;
                case 3:
                    listerParStatut();
                    break;
                case 4:
                    rechercherCommande();
                    break;
                case 5:
                    afficherDetailsCommande();
                    break;
                case 6:
                    modifierStatutCommande();
                    break;
                case 7:
                    annulerCommande();
                    break;
                case 8:
                    ajouterPaiement();
                    break;
                case 9:
                    filtrerParDate();
                    break;
                case 10:
                    filtrerParClient();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    afficherLigne("Choix invalide!");
            }
        }
    }

    private void creerCommande() {
        afficherTitre("CRÉATION D'UNE COMMANDE");

        // Sélection du client
        afficherLigne("Sélection du client:");
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            afficherLigne("Aucun client disponible. Créez d'abord un client.");
            return;
        }

        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            afficherLigne((i + 1) + ". " + client.getNom() + " " + client.getPrenom() +
                    " - " + client.getTelephone());
        }

        int choixClient = lireChoix("Choisir le client (1-" + clients.size() + "): ");
        if (choixClient < 1 || choixClient > clients.size()) {
            afficherLigne("Choix invalide!");
            return;
        }
        Client client = clients.get(choixClient - 1);

        // Choix du mode de consommation
        afficherLigne("\nMode de consommation:");
        ModeConsommation[] modes = ModeConsommation.values();
        for (int i = 0; i < modes.length; i++) {
            afficherLigne((i + 1) + ". " + modes[i]);
        }

        int choixMode = lireChoix("Mode (1-" + modes.length + "): ");
        if (choixMode < 1 || choixMode > modes.length) {
            afficherLigne("Choix invalide!");
            return;
        }
        ModeConsommation mode = modes[choixMode - 1];

        // Adresse de livraison si nécessaire
        String adresseLivraison = null;
        if (mode == ModeConsommation.LIVRAISON) {
            adresseLivraison = lireString("Adresse de livraison: ");
        }

        // Création de la commande
        Commande commande = new Commande(mode, client.getId());
        commande.setAdresseLivraison(adresseLivraison);

        boolean ajouterArticles = true;
        List<LigneCommande> lignes = new ArrayList<>();

        while (ajouterArticles) {
            afficherLigne("\n=== AJOUT D'ARTICLE ===");
            afficherLigne("1. Ajouter un burger");
            afficherLigne("2. Ajouter un menu");
            afficherLigne("3. Terminer la commande");

            int choixArticle = lireChoix("Votre choix: ");

            switch (choixArticle) {
                case 1:
                    ajouterBurger(lignes);
                    break;
                case 2:
                    ajouterMenu(lignes);
                    break;
                case 3:
                    ajouterArticles = false;
                    break;
                default:
                    afficherLigne("Choix invalide!");
            }
        }

        if (lignes.isEmpty()) {
            afficherLigne("Commande annulée: aucun article ajouté.");
            return;
        }

        // Affichage du récapitulatif
        afficherTitre("RÉCAPITULATIF DE LA COMMANDE");
        afficherLigne("Client: " + client.getNom() + " " + client.getPrenom());
        afficherLigne("Mode: " + mode);
        if (adresseLivraison != null) {
            afficherLigne("Adresse: " + adresseLivraison);
        }

        double total = 0;
        afficherLigne("\nARTICLES:");
        for (LigneCommande ligne : lignes) {
            if (ligne.getProduit() instanceof Burger) {
                Burger burger = (Burger) ligne.getProduit();
                afficherLigne("- " + burger.getNom() + " x" + ligne.getQuantite() +
                        " = " + (burger.getPrix() * ligne.getQuantite()) + " FCFA");
                total += burger.getPrix() * ligne.getQuantite();

                // Afficher les compléments
                if (!ligne.getComplements().isEmpty()) {
                    afficherLigne("  Compléments:");
                    for (Complement complement : ligne.getComplements()) {
                        afficherLigne("    + " + complement.getNom() + " - " + complement.getPrix() + " FCFA");
                        total += complement.getPrix();
                    }
                }
            } else if (ligne.getProduit() instanceof Menu) {
                Menu menu = (Menu) ligne.getProduit();
                afficherLigne("- " + menu.getNom() + " x" + ligne.getQuantite() +
                        " = " + (menu.getPrix() * ligne.getQuantite()) + " FCFA");
                total += menu.getPrix() * ligne.getQuantite();
            }
        }

        afficherLigne("\nTOTAL: " + total + " FCFA");

        // Confirmation
        String confirmation = lireString("\nConfirmer la commande? (oui/non): ");
        if (confirmation.equalsIgnoreCase("oui")) {
            try {
                Commande commandeCreee = commandeService.creerCommande(commande, lignes);
                afficherLigne("✅ Commande créée avec succès! ID: " + commandeCreee.getId());

                // Proposition de paiement
                afficherLigne("\nVoulez-vous enregistrer un paiement maintenant?");
                String payer = lireString("(oui/non): ");
                if (payer.equalsIgnoreCase("oui")) {
                    enregistrerPaiement(commandeCreee.getId(), total);
                }

            } catch (Exception e) {
                afficherLigne("❌ Erreur lors de la création: " + e.getMessage());
            }
        } else {
            afficherLigne("Commande annulée.");
        }
    }

    private void ajouterBurger(List<LigneCommande> lignes) {
        afficherLigne("\nBurgers disponibles:");
        List<Burger> burgers = burgerService.getBurgersActifs();
        if (burgers.isEmpty()) {
            afficherLigne("Aucun burger disponible.");
            return;
        }

        for (int i = 0; i < burgers.size(); i++) {
            Burger burger = burgers.get(i);
            afficherLigne((i + 1) + ". " + burger.getNom() + " - " + burger.getPrix() + " FCFA");
        }

        int choixBurger = lireChoix("Choisir le burger (1-" + burgers.size() + "): ");
        if (choixBurger < 1 || choixBurger > burgers.size()) {
            afficherLigne("Choix invalide!");
            return;
        }

        int quantite = lireChoix("Quantité: ");
        if (quantite <= 0) {
            afficherLigne("Quantité invalide!");
            return;
        }

        Burger burgerChoisi = burgers.get(choixBurger - 1);
        LigneCommande ligne = new LigneCommande(quantite, burgerChoisi.getPrix(), 0, burgerChoisi.getId());
        ligne.setProduit(burgerChoisi);

        // Proposition de compléments
        afficherLigne("\nVoulez-vous ajouter des compléments?");
        String ajouterComplements = lireString("(oui/non): ");

        if (ajouterComplements.equalsIgnoreCase("oui")) {
            List<Complement> complementsDisponibles = complementService.getComplementsActifs();
            List<Complement> complementsChoisis = new ArrayList<>();

            boolean continuerComplements = true;
            while (continuerComplements && !complementsDisponibles.isEmpty()) {
                afficherLigne("\nCompléments disponibles:");
                for (int i = 0; i < complementsDisponibles.size(); i++) {
                    Complement complement = complementsDisponibles.get(i);
                    afficherLigne((i + 1) + ". " + complement.getNom() +
                            " (" + complement.getTypeComplement() + ") - " +
                            complement.getPrix() + " FCFA");
                }
                afficherLigne("0. Terminer");

                int choixComplement = lireChoix("Choisir un complément (0 pour terminer): ");
                if (choixComplement == 0) {
                    continuerComplements = false;
                } else if (choixComplement > 0 && choixComplement <= complementsDisponibles.size()) {
                    Complement complementChoisi = complementsDisponibles.get(choixComplement - 1);
                    complementsChoisis.add(complementChoisi);
                    ligne.ajouterComplement(complementChoisi);
                    afficherLigne("✅ " + complementChoisi.getNom() + " ajouté.");
                } else {
                    afficherLigne("Choix invalide!");
                }
            }
        }

        lignes.add(ligne);
        afficherLigne("✅ Burger ajouté à la commande.");
    }

    private void ajouterMenu(List<LigneCommande> lignes) {
        afficherLigne("\nMenus disponibles:");
        List<Menu> menus = menuService.getMenusActifs();
        if (menus.isEmpty()) {
            afficherLigne("Aucun menu disponible.");
            return;
        }

        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            afficherLigne((i + 1) + ". " + menu.getNom() + " - " + menu.getPrix() + " FCFA");
        }

        int choixMenu = lireChoix("Choisir le menu (1-" + menus.size() + "): ");
        if (choixMenu < 1 || choixMenu > menus.size()) {
            afficherLigne("Choix invalide!");
            return;
        }

        int quantite = lireChoix("Quantité: ");
        if (quantite <= 0) {
            afficherLigne("Quantité invalide!");
            return;
        }

        Menu menuChoisi = menus.get(choixMenu - 1);
        LigneCommande ligne = new LigneCommande(quantite, menuChoisi.getPrix(), 0, menuChoisi.getId());
        ligne.setProduit(menuChoisi);

        lignes.add(ligne);
        afficherLigne("✅ Menu ajouté à la commande.");
    }

    private void enregistrerPaiement(int commandeId, double montant) {
        afficherTitre("ENREGISTREMENT DU PAIEMENT");

        afficherLigne("Méthodes de paiement:");
        MethodePaiement[] methodes = MethodePaiement.values();
        for (int i = 0; i < methodes.length; i++) {
            afficherLigne((i + 1) + ". " + methodes[i]);
        }

        int choixMethode = lireChoix("Méthode (1-" + methodes.length + "): ");
        if (choixMethode < 1 || choixMethode > methodes.length) {
            afficherLigne("Choix invalide!");
            return;
        }

        MethodePaiement methode = methodes[choixMethode - 1];
        String reference = lireString("Référence du paiement: ");

        try {
            commandeService.enregistrerPaiement(commandeId, montant, methode, reference);
            afficherLigne("✅ Paiement enregistré avec succès!");
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de l'enregistrement: " + e.getMessage());
        }
    }

    private void listerToutesCommandes() {
        List<Commande> commandes = commandeService.getAllCommandes();
        afficherTitre("LISTE DES COMMANDES (" + commandes.size() + ")");

        if (commandes.isEmpty()) {
            afficherLigne("Aucune commande trouvée.");
            return;
        }

        for (Commande commande : commandes) {
            afficherLigne(formatCommande(commande));
        }
    }

    private void listerParStatut() {
        afficherTitre("LISTE PAR STATUT");
        System.out.println("Statuts disponibles:");
        for (StatutCommande statut : StatutCommande.values()) {
            System.out.println("- " + statut);
        }

        String statutStr = lireString("Statut à afficher: ").toUpperCase();
        try {
            StatutCommande statut = StatutCommande.valueOf(statutStr);
            List<Commande> commandes = commandeService.getByStatut(statut);

            afficherTitre("COMMANDES " + statut + " (" + commandes.size() + ")");

            if (commandes.isEmpty()) {
                afficherLigne("Aucune commande avec ce statut.");
                return;
            }

            for (Commande commande : commandes) {
                afficherLigne(formatCommande(commande));
            }
        } catch (IllegalArgumentException e) {
            afficherLigne("❌ Statut invalide!");
        }
    }

    private void rechercherCommande() {
        afficherTitre("RECHERCHE DE COMMANDE");

        int id = lireChoix("ID de la commande: ");

        try {
            Commande commande = commandeService.getCommandeById(id);
            afficherLigne(formatCommandeDetail(commande));
        } catch (Exception e) {
            afficherLigne("❌ Commande non trouvée: " + e.getMessage());
        }
    }

    private void afficherDetailsCommande() {
        afficherTitre("DÉTAILS D'UNE COMMANDE");

        int id = lireChoix("ID de la commande: ");

        try {
            Commande commande = commandeService.getCommandeById(id);
            afficherLigne(formatCommandeDetail(commande));
        } catch (Exception e) {
            afficherLigne("❌ Commande non trouvée: " + e.getMessage());
        }
    }

    private void modifierStatutCommande() {
        afficherTitre("MODIFICATION DU STATUT D'UNE COMMANDE");

        int id = lireChoix("ID de la commande: ");

        try {
            Commande commande = commandeService.getCommandeById(id);
            afficherLigne("Commande actuelle: " + formatCommande(commande));

            afficherLigne("\nNouveaux statuts disponibles:");
            StatutCommande[] statuts = StatutCommande.values();
            for (int i = 0; i < statuts.length; i++) {
                afficherLigne((i + 1) + ". " + statuts[i]);
            }

            int choixStatut = lireChoix("Nouveau statut (1-" + statuts.length + "): ");
            if (choixStatut < 1 || choixStatut > statuts.length) {
                afficherLigne("Choix invalide!");
                return;
            }

            StatutCommande nouveauStatut = statuts[choixStatut - 1];
            commandeService.changerStatutCommande(id, nouveauStatut);

            afficherLigne("✅ Statut de la commande modifié avec succès!");
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private void annulerCommande() {
        afficherTitre("ANNULATION D'UNE COMMANDE");

        int id = lireChoix("ID de la commande à annuler: ");

        try {
            Commande commande = commandeService.getCommandeById(id);
            afficherLigne("Commande à annuler: " + formatCommande(commande));

            String confirmation = lireString("Êtes-vous sûr? (oui/non): ");
            if (confirmation.equalsIgnoreCase("oui")) {
                commandeService.annulerCommande(id);
                afficherLigne("✅ Commande annulée avec succès!");
            } else {
                afficherLigne("Annulation annulée.");
            }
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private void ajouterPaiement() {
        afficherTitre("AJOUT DE PAIEMENT");

        int id = lireChoix("ID de la commande: ");

        try {
            Commande commande = commandeService.getCommandeById(id);

            if (commande.getPaiement() != null) {
                afficherLigne("⚠ Cette commande a déjà un paiement:");
                afficherLigne("Méthode: " + commande.getPaiement().getMethode());
                afficherLigne("Montant: " + commande.getPaiement().getMontant() + " FCFA");
                afficherLigne("Date: " + commande.getPaiement().getDate());

                String continuer = lireString("Voulez-vous ajouter un nouveau paiement? (oui/non): ");
                if (!continuer.equalsIgnoreCase("oui")) {
                    return;
                }
            }

            double montant = lireDouble("Montant du paiement (FCFA): ");

            afficherLigne("\nMéthodes de paiement:");
            MethodePaiement[] methodes = MethodePaiement.values();
            for (int i = 0; i < methodes.length; i++) {
                afficherLigne((i + 1) + ". " + methodes[i]);
            }

            int choixMethode = lireChoix("Méthode (1-" + methodes.length + "): ");
            if (choixMethode < 1 || choixMethode > methodes.length) {
                afficherLigne("Choix invalide!");
                return;
            }

            MethodePaiement methode = methodes[choixMethode - 1];
            String reference = lireString("Référence du paiement: ");

            commandeService.enregistrerPaiement(id, montant, methode, reference);
            afficherLigne("✅ Paiement enregistré avec succès!");

        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private void filtrerParDate() {
        afficherTitre("FILTRER PAR DATE");

        afficherLigne("Format de date: AAAA-MM-JJ");
        String dateDebutStr = lireString("Date de début (laisser vide pour ignorer): ");
        String dateFinStr = lireString("Date de fin (laisser vide pour ignorer): ");

        try {
            List<Commande> commandes = commandeService.filtrerParDate(dateDebutStr, dateFinStr);

            afficherTitre("COMMANDES TROUVÉES (" + commandes.size() + ")");

            if (commandes.isEmpty()) {
                afficherLigne("Aucune commande trouvée pour ces dates.");
                return;
            }

            for (Commande commande : commandes) {
                afficherLigne(formatCommande(commande));
            }
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private void filtrerParClient() {
        afficherTitre("FILTRER PAR CLIENT");

        String recherche = lireString("Nom ou téléphone du client: ");

        try {
            List<Commande> commandes = commandeService.filtrerParClient(recherche);

            afficherTitre("COMMANDES TROUVÉES (" + commandes.size() + ")");

            if (commandes.isEmpty()) {
                afficherLigne("Aucune commande trouvée pour ce client.");
                return;
            }

            for (Commande commande : commandes) {
                afficherLigne(formatCommande(commande));
            }
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private String formatCommande(Commande commande) {
        return String.format("ID: %d | %s | %s | %.0f FCFA | Client: %s",
                commande.getId(),
                commande.getDate().toLocalDate(),
                commande.getStatut(),
                commande.getTotal(),
                commande.getClient() != null ?
                        commande.getClient().getNom() + " " + commande.getClient().getPrenom() :
                        "N/A"
        );
    }

    private String formatCommandeDetail(Commande commande) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append("COMMANDE #").append(commande.getId()).append("\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("Date: ").append(commande.getDate()).append("\n");
        sb.append("Statut: ").append(commande.getStatut()).append("\n");
        sb.append("Mode: ").append(commande.getModeConsommation()).append("\n");

        if (commande.getAdresseLivraison() != null) {
            sb.append("Adresse livraison: ").append(commande.getAdresseLivraison()).append("\n");
        }

        sb.append("Total: ").append(commande.getTotal()).append(" FCFA").append("\n");

        if (commande.getClient() != null) {
            sb.append("\nCLIENT:\n");
            sb.append("- Nom: ").append(commande.getClient().getNom()).append(" ").append(commande.getClient().getPrenom()).append("\n");
            sb.append("- Téléphone: ").append(commande.getClient().getTelephone()).append("\n");
            sb.append("- Email: ").append(commande.getClient().getEmail()).append("\n");
        }

        if (commande.getLivreur() != null) {
            sb.append("\nLIVREUR:\n");
            sb.append("- Nom: ").append(commande.getLivreur().getNom()).append(" ").append(commande.getLivreur().getPrenom()).append("\n");
            sb.append("- Véhicule: ").append(commande.getLivreur().getVehicule()).append("\n");
        }

        if (commande.getPaiement() != null) {
            sb.append("\nPAIEMENT:\n");
            sb.append("- Méthode: ").append(commande.getPaiement().getMethode()).append("\n");
            sb.append("- Montant: ").append(commande.getPaiement().getMontant()).append(" FCFA").append("\n");
            sb.append("- Référence: ").append(commande.getPaiement().getReference()).append("\n");
            sb.append("- Date: ").append(commande.getPaiement().getDate()).append("\n");
        }

        sb.append("\nARTICLES:\n");
        for (LigneCommande ligne : commande.getLignes()) {
            if (ligne.getProduit() != null) {
                sb.append("- ").append(ligne.getProduit().getNom())
                        .append(" x").append(ligne.getQuantite())
                        .append(" = ").append(ligne.getPrixUnitaire() * ligne.getQuantite()).append(" FCFA").append("\n");

                if (!ligne.getComplements().isEmpty()) {
                    sb.append("  Compléments:\n");
                    for (Complement complement : ligne.getComplements()) {
                        sb.append("    + ").append(complement.getNom())
                                .append(" - ").append(complement.getPrix()).append(" FCFA").append("\n");
                    }
                }
            }
        }

        return sb.toString();
    }
}