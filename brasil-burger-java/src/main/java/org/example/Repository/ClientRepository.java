package org.example.Repository;

import org.example.Model.Client;
import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Client update(Client client);
    boolean archive(int id);
    Optional<Client> findById(int id);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByTelephone(String telephone);
    List<Client> findAll();
    List<Client> findByArchive(boolean archive);
    List<Client> findByName(String nom);
    List<Client> search(String recherche);
    boolean authentifier(String email, String motDePasse);

    List<Client> Search(String trim);
}