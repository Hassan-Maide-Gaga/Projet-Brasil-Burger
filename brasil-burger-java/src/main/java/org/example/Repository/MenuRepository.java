package org.example.Repository;

import org.example.Model.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    Menu save(Menu menu);
    Menu update(Menu menu);
    boolean delete(int id);
    boolean archive(int id);
    Optional<Menu> findById(int id);
    List<Menu> findAll();
    List<Menu> findByArchive(boolean archive);
    List<Menu> findByName(String nom);
    List<Menu> findAllWithDetails();
}