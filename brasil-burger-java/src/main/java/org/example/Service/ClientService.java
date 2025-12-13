package org.example.Service;

import org.example.Model.Client;
import org.example.Model.Commande;
import java.util.List;

public interface ClientService {
    Client creerClient(String nom, String prenom, String telephone, String email, String motDePasse, String adresse);
    Client modifierClient(int id, String nom, String prenom, String telephone, String email, String adresse);
    boolean archiverClient(int id);
    Client getClientById(int id);
    List<Client> getAllClients();
    List<Client> getClientsActifs();
    List<Client> rechercherClients(String recherche);
    List<Commande> getCommandesClient(int clientId);
}