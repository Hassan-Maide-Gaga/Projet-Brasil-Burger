package org.example.Repository;

import org.example.Model.Burger;
import org.example.Model.Enum.TypeProduit;
import org.example.config.database.Convert;
import org.example.config.database.Database;
import org.example.config.factory.database.DatabaseFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurgerRepositoryImpl implements BurgerRepository {

    private final Database database;

    public BurgerRepositoryImpl() {
        this.database = DatabaseFactory.getInstance();
    }

    private Convert<Burger> burgerConvert = new Convert<Burger>() {
        @Override
        public Burger toEntity(ResultSet rs) throws SQLException {
            Burger burger = new Burger();
            burger.setId(rs.getInt("id"));
            burger.setNom(rs.getString("nom"));
            burger.setPrix(rs.getDouble("prix"));
            burger.setImage(rs.getString("image"));
            burger.setDescription(rs.getString("description"));
            burger.setEstArchive(rs.getBoolean("est_archive"));
            burger.setDateCreation(rs.getObject("date_creation", LocalDateTime.class));
            return burger;
        }
    };

    @Override
    public Burger save(Burger burger) {
        String sql = "INSERT INTO produit (nom, prix, image, description, type, est_archive) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, burger.getNom());
            ps.setDouble(2, burger.getPrix());
            ps.setString(3, burger.getImage());
            ps.setString(4, burger.getDescription());
            ps.setString(5, TypeProduit.BURGER.name());
            ps.setBoolean(6, burger.isEstArchive());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        burger.setId(id);

                        // Insérer aussi dans la table burger
                        String sqlBurger = "INSERT INTO burger (id_burger) VALUES (?)";
                        try (PreparedStatement psBurger = database.prepareStatement(sqlBurger, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            psBurger.setInt(1, id);
                            psBurger.executeUpdate();
                        }
                    }
                }
            }
            return burger;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du burger: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Burger update(Burger burger) {
        String sql = "UPDATE produit SET nom = ?, prix = ?, image = ?, description = ?, " +
                "est_archive = ? WHERE id = ? AND type = 'BURGER'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, burger.getNom());
            ps.setDouble(2, burger.getPrix());
            ps.setString(3, burger.getImage());
            ps.setString(4, burger.getDescription());
            ps.setBoolean(5, burger.isEstArchive());
            ps.setInt(6, burger.getId());

            ps.executeUpdate();
            return burger;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du burger: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM produit WHERE id = ? AND type = 'BURGER'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du burger: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archive(int id) {
        String sql = "UPDATE produit SET est_archive = true WHERE id = ? AND type = 'BURGER'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du burger: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Burger> findById(int id) {
        String sql = "SELECT * FROM produit WHERE id = ? AND type = 'BURGER'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            return database.fetch(ps, burgerConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du burger: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Burger> findAll() {
        String sql = "SELECT * FROM produit WHERE type = 'BURGER' ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, burgerConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des burgers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Burger> findByArchive(boolean archive) {
        String sql = "SELECT * FROM produit WHERE type = 'BURGER' AND est_archive = ? ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, archive);
            return database.fetchAll(ps, burgerConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des burgers archivés: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Burger> findByName(String nom) {
        String sql = "SELECT * FROM produit WHERE type = 'BURGER' AND LOWER(nom) LIKE LOWER(?) ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "%" + nom + "%");
            return database.fetchAll(ps, burgerConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}