package org.example.Service;

import org.example.Model.Burger;
import java.util.List;

public interface BurgerService {
    Burger creerBurger(String nom, double prix, String description);
    Burger modifierBurger(int id, String nom, double prix, String description);
    boolean supprimerBurger(int id);
    boolean archiverBurger(int id);
    Burger getBurgerById(int id);
    List<Burger> getAllBurgers();
    List<Burger> getBurgersActifs();
    List<Burger> rechercherBurgers(String recherche);
    double calculerPrixBurgerAvecComplements(int burgerId, List<Integer> complementIds);
}