package org.example.Service;

import org.example.Model.Menu;
import org.example.Model.Burger;
import org.example.Model.Complement;
import org.example.Repository.MenuRepository;
import org.example.Repository.MenuRepositoryImpl;
import org.example.Repository.BurgerRepository;
import org.example.Repository.BurgerRepositoryImpl;
import org.example.Repository.ComplementRepository;
import org.example.Repository.ComplementRepositoryImpl;
import java.util.List;
import java.util.Optional;

public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final BurgerRepository burgerRepository;
    private final ComplementRepository complementRepository;

    public MenuServiceImpl() {
        this.menuRepository = new MenuRepositoryImpl();
        this.burgerRepository = new BurgerRepositoryImpl();
        this.complementRepository = new ComplementRepositoryImpl();
    }

    @Override
    public Menu creerMenu(String nom, String description, int burgerId,
                          int boissonId, int friteId) {
        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du menu ne peut pas être vide");
        }

        // Vérifier que les produits existent
        Optional<Burger> burger = burgerRepository.findById(burgerId);
        if (!burger.isPresent()) {
            throw new IllegalArgumentException("Burger introuvable avec l'ID: " + burgerId);
        }

        Optional<Complement> boisson = complementRepository.findById(boissonId);
        if (!boisson.isPresent()) {
            throw new IllegalArgumentException("Boisson introuvable avec l'ID: " + boissonId);
        }

        Optional<Complement> frite = complementRepository.findById(friteId);
        if (!frite.isPresent()) {
            throw new IllegalArgumentException("Frite introuvable avec l'ID: " + friteId);
        }

        // Calculer le prix du menu (peut inclure une réduction)
        double prix = calculerPrixMenu(burgerId, boissonId, friteId);

        Menu menu = new Menu(nom, prix, description, burgerId, boissonId, friteId);
        return menuRepository.save(menu);
    }

    @Override
    public Menu modifierMenu(int id, String nom, String description,
                             int burgerId, int boissonId, int friteId) {
        // Vérifier que le menu existe
        Optional<Menu> existant = menuRepository.findById(id);
        if (!existant.isPresent()) {
            throw new IllegalArgumentException("Menu introuvable avec l'ID: " + id);
        }

        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du menu ne peut pas être vide");
        }

        // Vérifier que les produits existent
        Optional<Burger> burger = burgerRepository.findById(burgerId);
        if (!burger.isPresent()) {
            throw new IllegalArgumentException("Burger introuvable avec l'ID: " + burgerId);
        }

        Optional<Complement> boisson = complementRepository.findById(boissonId);
        if (!boisson.isPresent()) {
            throw new IllegalArgumentException("Boisson introuvable avec l'ID: " + boissonId);
        }

        Optional<Complement> frite = complementRepository.findById(friteId);
        if (!frite.isPresent()) {
            throw new IllegalArgumentException("Frite introuvable avec l'ID: " + friteId);
        }

        // Recalculer le prix
        double prix = calculerPrixMenu(burgerId, boissonId, friteId);

        Menu menu = existant.get();
        menu.setNom(nom);
        menu.setDescription(description);
        menu.setPrix(prix);
        menu.setBurgerId(burgerId);
        menu.setBoissonId(boissonId);
        menu.setFriteId(friteId);

        return menuRepository.update(menu);
    }

    @Override
    public boolean supprimerMenu(int id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (!menu.isPresent()) {
            throw new IllegalArgumentException("Menu introuvable avec l'ID: " + id);
        }

        return menuRepository.delete(id);
    }

    @Override
    public boolean archiverMenu(int id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (!menu.isPresent()) {
            throw new IllegalArgumentException("Menu introuvable avec l'ID: " + id);
        }

        return menuRepository.archive(id);
    }

    @Override
    public Menu getMenuById(int id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu introuvable avec l'ID: " + id));
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public List<Menu> getMenusActifs() {
        return menuRepository.findByArchive(false);
    }

    @Override
    public List<Menu> rechercherMenus(String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return getAllMenus();
        }
        return menuRepository.findByName(recherche.trim());
    }

    @Override
    public double calculerPrixMenu(int burgerId, int boissonId, int friteId) {
        Optional<Burger> burger = burgerRepository.findById(burgerId);
        Optional<Complement> boisson = complementRepository.findById(boissonId);
        Optional<Complement> frite = complementRepository.findById(friteId);

        if (!burger.isPresent() || !boisson.isPresent() || !frite.isPresent()) {
            throw new IllegalArgumentException("Un ou plusieurs produits sont introuvables");
        }

        double total = burger.get().getPrix() +
                boisson.get().getPrix() +
                frite.get().getPrix();

        // Appliquer une réduction de 10% pour les menus
        return total * 0.90;
    }
}