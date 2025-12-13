package org.example.Service;

import org.example.Model.Menu;
import java.util.List;

public interface MenuService {
    Menu creerMenu(String nom, String description, int burgerId, int boissonId, int friteId);
    Menu modifierMenu(int id, String nom, String description, int burgerId, int boissonId, int friteId);
    boolean supprimerMenu(int id);
    boolean archiverMenu(int id);
    Menu getMenuById(int id);
    List<Menu> getAllMenus();
    List<Menu> getMenusActifs();
    List<Menu> rechercherMenus(String recherche);
    double calculerPrixMenu(int burgerId, int boissonId, int friteId);
}