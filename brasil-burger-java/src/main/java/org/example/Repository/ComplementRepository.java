// Fichier: src/main/java/org/example/repository/ComplementRepository.java
package org.example.repository;

import org.example.config.Database;
import org.example.factory.database.DatabaseFactory;
import org.example.model.Complement;
import org.example.model.enums.StatutArchivage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplementRepository {
    private final Database database;

    public ComplementRepository() {
        this.database = DatabaseFactory.getInstance();
    }

    public Complement create(Complement complement) {
        String sql = "INSERT INTO complement (nom, prix, image_url, image_public_id, type_complement, statut_archivage) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, complement.getNom());
            pstmt.setDouble(2, complement.getPrix());
            pstmt.setString(3, complement.getImageUrl());
            pstmt.setString(4, complement.getImagePublicId());
            pstmt.setString(5, complement.getTypeComplement().name());
            pstmt.setString(6, complement.getStatutArchivage().getValeur());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                complement.setId(rs.getInt("id"));
                return complement;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du complément: " + e.getMessage());
        }
        return null;
    }

    public Complement findById(Integer id) {
        String sql = "SELECT * FROM complement WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToComplement(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du complément: " + e.getMessage());
        }
        return null;
    }

    public List<Complement> findAll() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complement ORDER BY type_complement, nom";

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compléments: " + e.getMessage());
        }
        return complements;
    }

    public List<Complement> findActifs() {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complement WHERE statut_archivage = 'ACTIF' ORDER BY type_complement, nom";

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compléments actifs: " + e.getMessage());
        }
        return complements;
    }

    public List<Complement> findByType(Complement.TypeComplement type) {
        List<Complement> complements = new ArrayList<>();
        String sql = "SELECT * FROM complement WHERE type_complement = ? AND statut_archivage = 'ACTIF' ORDER BY nom";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compléments par type: " + e.getMessage());
        }
        return complements;
    }

    public boolean update(Complement complement) {
        String sql = "UPDATE complement SET nom = ?, prix = ?, image_url = ?, image_public_id = ?, " +
                "type_complement = ?, statut_archivage = ? WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, complement.getNom());
            pstmt.setDouble(2, complement.getPrix());
            pstmt.setString(3, complement.getImageUrl());
            pstmt.setString(4, complement.getImagePublicId());
            pstmt.setString(5, complement.getTypeComplement().name());
            pstmt.setString(6, complement.getStatutArchivage().getValeur());
            pstmt.setInt(7, complement.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du complément: " + e.getMessage());
        }
        return false;
    }

    public boolean archiver(Integer id) {
        String sql = "UPDATE complement SET statut_archivage = 'ARCHIVE' WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du complément: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM complement WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du complément: " + e.getMessage());
        }
        return false;
    }

    private Complement mapResultSetToComplement(ResultSet rs) throws SQLException {
        return new Complement(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("prix"),
                rs.getString("image_url"),
                rs.getString("image_public_id"),
                Complement.TypeComplement.valueOf(rs.getString("type_complement")),
                rs.getTimestamp("date_creation").toLocalDateTime(),
                StatutArchivage.fromString(rs.getString("statut_archivage"))
        );
    }
}