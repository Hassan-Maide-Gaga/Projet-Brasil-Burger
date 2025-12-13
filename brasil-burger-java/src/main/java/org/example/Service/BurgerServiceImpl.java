package org.example.Service;

import org.example.Model.Burger;
import org.example.Repository.BurgerRepository;
import org.example.Repository.ComplementRepository;
import org.example.config.factory.repository.RepositoryFactory;
import java.util.List;

public class BurgerServiceImpl implements BurgerService {

    private final BurgerRepository burgerRepository;
    private final ComplementRepository complementRepository;

    public BurgerServiceImpl() {
        this.burgerRepository = RepositoryFactory.getBurgerRepository();
        this.complementRepository = RepositoryFactory.getComplementRepository();
    }

    @Override
    public Burger creerBurger(String nom, double prix, String description) {
        Burger burger = new Burger(nom, prix, description);
        return burgerRepository.save(burger);
    }

    @Override
    public Burger modifierBurger(int id, String nom, double prix, String description) {
        Burger burger = burgerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Burger non trouvé avec l'ID: " + id));

        burger.setNom(nom);
        burger.setPrix(prix);
        burger.setDescription(description);

        return burgerRepository.update(burger);
    }

    @Override
    public boolean supprimerBurger(int id) {
        return burgerRepository.delete(id);
    }

    @Override
    public boolean archiverBurger(int id) {
        return burgerRepository.archive(id);
    }

    @Override
    public Burger getBurgerById(int id) {
        return burgerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Burger non trouvé avec l'ID: " + id));
    }

    @Override
    public List<Burger> getAllBurgers() {
        return burgerRepository.findAll();
    }

    @Override
    public List<Burger> getBurgersActifs() {
        return burgerRepository.findByArchive(false);
    }

    @Override
    public List<Burger> rechercherBurgers(String recherche) {
        return burgerRepository.findByName(recherche);
    }

    @Override
    public double calculerPrixBurgerAvecComplements(int burgerId, List<Integer> complementIds) {
        Burger burger = getBurgerById(burgerId);
        double total = burger.getPrix();

        for (int complementId : complementIds) {
            var complement = complementRepository.findById(complementId)
                    .orElseThrow(() -> new RuntimeException("Complement non trouvé avec l'ID: " + complementId));
            total += complement.getPrix();
        }

        return total;
    }
}