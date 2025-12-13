package org.example.Repository;

import org.example.Model.Burger;
import java.util.List;
import java.util.Optional;

public interface BurgerRepository {
    Burger save(Burger burger);
    Burger update(Burger burger);
    boolean delete(int id);
    boolean archive(int id);
    Optional<Burger> findById(int id);
    List<Burger> findAll();
    List<Burger> findByArchive(boolean archive);
    List<Burger> findByName(String nom);
}