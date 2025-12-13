package org.example.Vue;

import org.example.Model.Client;
import org.example.Service.ClientService;
import org.example.config.factory.service.ServiceFactory;
import java.util.List;

public class ClientVue extends Vue {

    private final ClientService clientService;

    public ClientVue() {
        this.clientService = ServiceFactory.getClientService();
    }

    public void afficherMenuClient() {
        boolean continuer = true;

        while (continuer) {
            afficherTitre("GESTION DES CLIENTS");
            String[] options = {
                    "Lister tous les clients",
                    "Rechercher un client",
                    "Créer un client",
                    "Modifier un client",
                    "Archiver un client",
                    "Afficher détails client",
                    "Voir commandes d'un client"
            };
            afficherMenu(options);

            int choix = lireChoix("\nVotre choix: ");

            switch (choix) {
                case 1:
                    listerTousClients();
                    break;
                case 2:
                    rechercherClient();
                    break;
                case 3:
                    creerClient();
                    break;
                case 4:
                    modifierClient();
                    break;
                case 5:
                    archiverClient();
                    break;
                case 6:
                    afficherDetailsClient();
                    break;
                case 7:
                    voirCommandesClient();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    afficherLigne("Choix invalide!");
            }
        }
    }

    private void listerTousClients() {
        List<Client> clients = clientService.getAllClients();
        afficherTitre("LISTE DES CLIENTS (" + clients.size() + ")");

        if (clients.isEmpty()) {
            afficherLigne("Aucun client trouvé.");
            return;
        }

        for (Client client : clients) {
            afficherLigne(formatClient(client));
        }
    }

    private void rechercherClient() {
        afficherTitre("RECHERCHE DE CLIENT");

        String recherche = lireString("Nom, prénom ou téléphone: ");
        List<Client> clients = clientService.rechercherClients(recherche);

        afficherTitre("RÉSULTATS (" + clients.size() + ")");

        if (clients.isEmpty()) {
            afficherLigne("Aucun client trouvé.");
            return;
        }

        for (Client client : clients) {
            afficherLigne(formatClient(client));
        }
    }

    private void creerClient() {
        afficherTitre("CRÉATION D'UN NOUVEAU CLIENT");

        String nom = lireString("Nom: ");
        String prenom = lireString("Prénom: ");
        String telephone = lireString("Téléphone: ");
        String email = lireString("Email: ");
        String motDePasse = lireString("Mot de passe: ");
        String adresse = lireString("Adresse: ");

        try {
            Client client = clientService.creerClient(nom, prenom, telephone, email, motDePasse, adresse);
            afficherLigne("✅ Client créé avec succès!");
            afficherLigne(formatClientDetail(client));
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de la création: " + e.getMessage());
        }
    }

    private void modifierClient() {
        afficherTitre("MODIFICATION D'UN CLIENT");

        int id = lireChoix("ID du client à modifier: ");

        try {
            Client client = clientService.getClientById(id);
            afficherLigne("Client actuel:");
            afficherLigne(formatClientDetail(client));

            String nom = lireString("\nNouveau nom (laisser vide pour garder '" + client.getNom() + "'): ");
            String prenom = lireString("Nouveau prénom (laisser vide pour garder '" + client.getPrenom() + "'): ");
            String telephone = lireString("Nouveau téléphone (laisser vide pour garder '" + client.getTelephone() + "'): ");
            String email = lireString("Nouvel email (laisser vide pour garder '" + client.getEmail() + "'): ");
            String adresse = lireString("Nouvelle adresse (laisser vide pour garder '" + client.getAdresse() + "'): ");

            // Appliquer les modifications
            if (!nom.isEmpty()) client.setNom(nom);
            if (!prenom.isEmpty()) client.setPrenom(prenom);
            if (!telephone.isEmpty()) client.setTelephone(telephone);
            if (!email.isEmpty()) client.setEmail(email);
            if (!adresse.isEmpty()) client.setAdresse(adresse);

            Client clientModifie = clientService.modifierClient(
                    id,
                    client.getNom(),
                    client.getPrenom(),
                    client.getTelephone(),
                    client.getEmail(),
                    client.getAdresse()
            );

            afficherLigne("✅ Client modifié avec succès!");
            afficherLigne(formatClientDetail(clientModifie));

        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de la modification: " + e.getMessage());
        }
    }

    private void archiverClient() {
        afficherTitre("ARCHIVAGE D'UN CLIENT");

        int id = lireChoix("ID du client à archiver: ");

        try {
            clientService.archiverClient(id);
            afficherLigne("✅ Client archivé avec succès!");
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de l'archivage: " + e.getMessage());
        }
    }

    private void afficherDetailsClient() {
        afficherTitre("DÉTAILS D'UN CLIENT");

        int id = lireChoix("ID du client: ");

        try {
            Client client = clientService.getClientById(id);
            afficherLigne(formatClientDetail(client));
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private void voirCommandesClient() {
        afficherTitre("COMMANDES D'UN CLIENT");

        int id = lireChoix("ID du client: ");

        try {
            Client client = clientService.getClientById(id);
            afficherLigne("Client: " + client.getNom() + " " + client.getPrenom());

            List<org.example.Model.Commande> commandes = clientService.getCommandesClient(id);

            afficherTitre("COMMANDES (" + commandes.size() + ")");

            if (commandes.isEmpty()) {
                afficherLigne("Aucune commande pour ce client.");
                return;
            }

            for (org.example.Model.Commande commande : commandes) {
                afficherLigne(formatCommandeClient(commande));
            }
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private String formatClient(Client client) {
        return String.format("ID: %d | %s %s | %s | %s | %s",
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getTelephone(),
                client.getEmail(),
                client.isEstArchive() ? "[ARCHIVÉ]" : "[ACTIF]"
        );
    }

    private String formatClientDetail(Client client) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(50)).append("\n");
        sb.append("CLIENT #").append(client.getId()).append("\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Nom: ").append(client.getNom()).append(" ").append(client.getPrenom()).append("\n");
        sb.append("Téléphone: ").append(client.getTelephone()).append("\n");
        sb.append("Email: ").append(client.getEmail()).append("\n");
        sb.append("Adresse: ").append(client.getAdresse()).append("\n");
        sb.append("Date création: ").append(client.getDateCreation()).append("\n");
        sb.append("Statut: ").append(client.isEstArchive() ? "Archivé" : "Actif").append("\n");
        return sb.toString();
    }

    private String formatCommandeClient(org.example.Model.Commande commande) {
        return String.format("Commande #%d | %s | %s | %.0f FCFA",
                commande.getId(),
                commande.getDate().toLocalDate(),
                commande.getStatut(),
                commande.getTotal()
        );
    }
}