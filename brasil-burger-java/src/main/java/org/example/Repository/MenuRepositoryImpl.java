package org.example.Repository;

import org.example.Model.Menu;
import org.example.Model.Enum.TypeProduit;
import org.example.config.database.Convert;
import org.example.config.database.Database;
import org.example.config.factory.database.DatabaseFactory;
import org.example.config.factory.repository.RepositoryFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepositoryImpl implements MenuRepository {

    private final Database database;
    private final BurgerRepository burgerRepository;
    private final ComplementRepository complementRepository;

    public MenuRepositoryImpl() {
        this.database = DatabaseFactory.getInstance();
        this.burgerRepository = RepositoryFactory.getBurgerRepository();
        this.complementRepository = RepositoryFactory.getComplementRepository();
    }

    private Convert<Menu> menuConvert = new Convert<Menu>() {
        @Override
        public Menu toEntity(ResultSet rs) throws SQLException {
            Menu menu = new Menu();
            menu.setId(rs.getInt("id"));
            menu.setNom(rs.getString("nom"));
            menu.setPrix(rs.getDouble("prix"));
            menu.setImage(rs.getString("image"));
            menu.setDescription(rs.getString("description"));
            menu.setEstArchive(rs.getBoolean("est_archive"));
            menu.setDateCreation(rs.getObject("date_creation", LocalDateTime.class));

            // Récupérer les IDs des composants
            String sqlComposants = "SELECT id_burger, id_boisson, id_frite FROM menu WHERE id_menu = ?";
            try (PreparedStatement ps = database.prepareStatement(sqlComposants, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, menu.getId());
                try (ResultSet rsComposants = ps.executeQuery()) {
                    if (rsComposants.next()) {
                        menu.setBurgerId(rsComposants.getInt("id_burger"));
                        menu.setBoissonId(rsComposants.getInt("id_boisson"));
                        menu.setFriteId(rsComposants.getInt("id_frite"));

                        // Charger les objets
                        menu.setBurger(burgerRepository.findById(menu.getBurgerId()).orElse(null));
                        menu.setBoisson(complementRepository.findById(menu.getBoissonId()).orElse(null));
                        menu.setFrite(complementRepository.findById(menu.getFriteId()).orElse(null));
                    }
                }
            }

            return menu;
        }
    };

    @Override
    public Menu save(Menu menu) {
        // Insérer d'abord le produit
        String sqlProduit = "INSERT INTO produit (nom, prix, image, description, type, est_archive) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = database.prepareStatement(sqlProduit, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, menu.getNom());
            ps.setDouble(2, menu.getPrix());
            ps.setString(3, menu.getImage());
            ps.setString(4, menu.getDescription());
            ps.setString(5, TypeProduit.MENU.name());
            ps.setBoolean(6, menu.isEstArchive());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        menu.setId(id);

                        // Insérer dans la table menu
                        String sqlMenu = "INSERT INTO menu (id_menu, id_burger, id_boisson, id_frite) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement psMenu = database.prepareStatement(sqlMenu, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            psMenu.setInt(1, id);
                            psMenu.setInt(2, menu.getBurgerId());
                            psMenu.setInt(3, menu.getBoissonId());
                            psMenu.setInt(4, menu.getFriteId());
                            psMenu.executeUpdate();
                        }
                    }
                }
            }
            return menu;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du menu: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Menu update(Menu menu) {
        String sqlProduit = "UPDATE produit SET nom = ?, prix = ?, image = ?, description = ?, " +
                "est_archive = ? WHERE id = ? AND type = 'MENU'";

        try (PreparedStatement ps = database.prepareStatement(sqlProduit, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, menu.getNom());
            ps.setDouble(2, menu.getPrix());
            ps.setString(3, menu.getImage());
            ps.setString(4, menu.getDescription());
            ps.setBoolean(5, menu.isEstArchive());
            ps.setInt(6, menu.getId());

            ps.executeUpdate();

            // Mettre à jour les composants
            String sqlMenu = "UPDATE menu SET id_burger = ?, id_boisson = ?, id_frite = ? WHERE id_menu = ?";
            try (PreparedStatement psMenu = database.prepareStatement(sqlMenu, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psMenu.setInt(1, menu.getBurgerId());
                psMenu.setInt(2, menu.getBoissonId());
                psMenu.setInt(3, menu.getFriteId());
                psMenu.setInt(4, menu.getId());
                psMenu.executeUpdate();
            }

            return menu;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du menu: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM produit WHERE id = ? AND type = 'MENU'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du menu: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archive(int id) {
        String sql = "UPDATE produit SET est_archive = true WHERE id = ? AND type = 'MENU'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du menu: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Menu> findById(int id) {
        String sql = "SELECT * FROM produit WHERE id = ? AND type = 'MENU'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            return database.fetch(ps, menuConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du menu: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Menu> findAll() {
        String sql = "SELECT * FROM produit WHERE type = 'MENU' ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, menuConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des menus: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Menu> findByArchive(boolean archive) {
        String sql = "SELECT * FROM produit WHERE type = 'MENU' AND est_archive = ? ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, archive);
            return database.fetchAll(ps, menuConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des menus archivés: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Menu> findByName(String nom) {
        String sql = "SELECT * FROM produit WHERE type = 'MENU' AND LOWER(nom) LIKE LOWER(?) ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "%" + nom + "%");
            return database.fetchAll(ps, menuConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Menu> findAllWithDetails() {
        List<Menu> menus = findAll();
        // Les détails sont déjà chargés dans le convert
        return menus;
    }
}