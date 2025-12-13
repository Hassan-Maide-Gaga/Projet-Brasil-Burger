package org.example.Service;

import org.example.Model.Client;
import org.example.Model.Commande;
import org.example.Repository.ClientRepository;
import org.example.Repository.ClientRepositoryImpl;
import org.example.Repository.CommandeRepository;
import org.example.Repository.CommandeRepositoryImpl;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final CommandeRepository commandeRepository;

    // Pattern pour valider l'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Pattern pour valider le téléphone (format sénégalais: +221XXXXXXXXX ou 7XXXXXXXX)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(\\+221)?[7][0-9]{8}$"
    );

    public ClientServiceImpl() {
        this.clientRepository = new ClientRepositoryImpl();
        this.commandeRepository = new CommandeRepositoryImpl();
    }

    @Override
    public Client creerClient(String nom, String prenom, String telephone,
                              String email, String motDePasse, String adresse) {
        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (telephone == null || !PHONE_PATTERN.matcher(telephone).matches()) {
            throw new IllegalArgumentException("Le numéro de téléphone est invalide");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("L'adresse email est invalide");
        }
        if (motDePasse == null || motDePasse.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
        }

        // Vérifier si l'email existe déjà
        Optional<Client> existant = clientRepository.findByEmail(email);
        if (existant.isPresent()) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        // Vérifier si le téléphone existe déjà
        Optional<Client> existantTel = clientRepository.findByTelephone(telephone);
        if (existantTel.isPresent()) {
            throw new IllegalArgumentException("Un client avec ce numéro de téléphone existe déjà");
        }

        Client client = new Client(nom, prenom, telephone, email, motDePasse, adresse);
        return clientRepository.save(client);
    }

    @Override
    public Client modifierClient(int id, String nom, String prenom, String telephone,
                                 String email, String adresse) {
        // Vérifier que le client existe
        Optional<Client> existant = clientRepository.findById(id);
        if (!existant.isPresent()) {
            throw new IllegalArgumentException("Client introuvable avec l'ID: " + id);
        }

        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (telephone == null || !PHONE_PATTERN.matcher(telephone).matches()) {
            throw new IllegalArgumentException("Le numéro de téléphone est invalide");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("L'adresse email est invalide");
        }

        Client client = existant.get();

        // Vérifier si l'email est déjà utilisé par un autre client
        if (!client.getEmail().equals(email)) {
            Optional<Client> clientAvecEmail = clientRepository.findByEmail(email);
            if (clientAvecEmail.isPresent()) {
                throw new IllegalArgumentException("Un autre client utilise déjà cet email");
            }
        }

        // Vérifier si le téléphone est déjà utilisé par un autre client
        if (!client.getTelephone().equals(telephone)) {
            Optional<Client> clientAvecTel = clientRepository.findByTelephone(telephone);
            if (clientAvecTel.isPresent()) {
                throw new IllegalArgumentException("Un autre client utilise déjà ce numéro de téléphone");
            }
        }

        client.setNom(nom);
        client.setPrenom(prenom);
        client.setTelephone(telephone);
        client.setEmail(email);
        client.setAdresse(adresse);

        return clientRepository.update(client);
    }

    @Override
    public boolean archiverClient(int id) {
        Optional<Client> client = clientRepository.findById(id);
        if (!client.isPresent()) {
            throw new IllegalArgumentException("Client introuvable avec l'ID: " + id);
        }

        return clientRepository.archive(id);
    }

    @Override
    public Client getClientById(int id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable avec l'ID: " + id));
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getClientsActifs() {
        return clientRepository.findByArchive(false);
    }

    @Override
    public List<Client> rechercherClients(String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return getAllClients();
        }
        return clientRepository.findByName(recherche.trim());
    }

    @Override
    public List<Commande> getCommandesClient(int clientId) {
        // Vérifier que le client existe
        Optional<Client> client = clientRepository.findById(clientId);
        if (!client.isPresent()) {
            throw new IllegalArgumentException("Client introuvable avec l'ID: " + clientId);
        }

        return commandeRepository.findByClient(clientId);
    }
}