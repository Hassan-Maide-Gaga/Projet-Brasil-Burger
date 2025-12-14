// Fichier: src/main/java/org/example/repository/BurgerRepository.java
package org.example.repository;

import org.example.config.Database;
import org.example.factory.database.DatabaseFactory;
import org.example.model.Burger;
import org.example.model.enums.StatutArchivage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BurgerRepository {
    private final Database database;

    public BurgerRepository() {
        this.database = DatabaseFactory.getInstance();
    }

    public Burger create(Burger burger) {
        String sql = "INSERT INTO burger (nom, prix, image_url, image_public_id, description, statut_archivage) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, burger.getNom());
            pstmt.setDouble(2, burger.getPrix());
            pstmt.setString(3, burger.getImageUrl());
            pstmt.setString(4, burger.getImagePublicId());
            pstmt.setString(5, burger.getDescription());
            pstmt.setString(6, burger.getStatutArchivage().getValeur());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                burger.setId(rs.getInt("id"));
                return burger;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du burger: " + e.getMessage());
        }
        return null;
    }

    public Burger findById(Integer id) {
        String sql = "SELECT * FROM burger WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBurger(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du burger: " + e.getMessage());
        }
        return null;
    }

    public List<Burger> findAll() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burger ORDER BY date_creation DESC";

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des burgers: " + e.getMessage());
        }
        return burgers;
    }

    public List<Burger> findActifs() {
        List<Burger> burgers = new ArrayList<>();
        String sql = "SELECT * FROM burger WHERE statut_archivage = 'ACTIF' ORDER BY nom";

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des burgers actifs: " + e.getMessage());
        }
        return burgers;
    }

    public boolean update(Burger burger) {
        String sql = "UPDATE burger SET nom = ?, prix = ?, image_url = ?, image_public_id = ?, " +
                "description = ?, statut_archivage = ? WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, burger.getNom());
            pstmt.setDouble(2, burger.getPrix());
            pstmt.setString(3, burger.getImageUrl());
            pstmt.setString(4, burger.getImagePublicId());
            pstmt.setString(5, burger.getDescription());
            pstmt.setString(6, burger.getStatutArchivage().getValeur());
            pstmt.setInt(7, burger.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du burger: " + e.getMessage());
        }
        return false;
    }

    public boolean archiver(Integer id) {
        String sql = "UPDATE burger SET statut_archivage = 'ARCHIVE' WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du burger: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM burger WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du burger: " + e.getMessage());
        }
        return false;
    }

    private Burger mapResultSetToBurger(ResultSet rs) throws SQLException {
        return new Burger(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("prix"),
                rs.getString("image_url"),
                rs.getString("image_public_id"),
                rs.getString("description"),
                rs.getTimestamp("date_creation").toLocalDateTime(),
                StatutArchivage.fromString(rs.getString("statut_archivage"))
        );
    }
}