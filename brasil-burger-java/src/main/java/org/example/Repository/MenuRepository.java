// Fichier: src/main/java/org/example/repository/MenuRepository.java
package org.example.repository;

import org.example.config.Database;
import org.example.factory.database.DatabaseFactory;
import org.example.model.*;
import org.example.model.enums.StatutArchivage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRepository {
    private final Database database;

    public MenuRepository() {
        this.database = DatabaseFactory.getInstance();
    }

    public Menu create(Menu menu) {
        String sql = "INSERT INTO menu (nom, image_url, image_public_id, description, statut_archivage) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, menu.getNom());
            pstmt.setString(2, menu.getImageUrl());
            pstmt.setString(3, menu.getImagePublicId());
            pstmt.setString(4, menu.getDescription());
            pstmt.setString(5, menu.getStatutArchivage().getValeur());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                menu.setId(rs.getInt("id"));
                return menu;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du menu: " + e.getMessage());
        }
        return null;
    }

    public boolean ajouterComposition(Integer menuId, Integer burgerId, Integer complementId,
                                      MenuComposition.TypeElement typeElement) {
        String sql = "INSERT INTO menu_composition (menu_id, burger_id, complement_id, type_element) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, menuId);
            if (burgerId != null) {
                pstmt.setInt(2, burgerId);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            if (complementId != null) {
                pstmt.setInt(3, complementId);
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, typeElement.name());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la composition: " + e.getMessage());
        }
        return false;
    }

    public Menu findById(Integer id) {
        String sql = "SELECT * FROM menu WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Menu menu = mapResultSetToMenu(rs);
                menu.setCompositions(findCompositionsByMenuId(id));
                return menu;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du menu: " + e.getMessage());
        }
        return null;
    }

    public List<Menu> findAll() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY date_creation DESC";

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Menu menu = mapResultSetToMenu(rs);
                menu.setCompositions(findCompositionsByMenuId(menu.getId()));
                menus.add(menu);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des menus: " + e.getMessage());
        }
        return menus;
    }

    public List<Menu> findActifs() {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE statut_archivage = 'ACTIF' ORDER BY nom";

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Menu menu = mapResultSetToMenu(rs);
                menu.setCompositions(findCompositionsByMenuId(menu.getId()));
                menus.add(menu);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des menus actifs: " + e.getMessage());
        }
        return menus;
    }

    private List<MenuComposition> findCompositionsByMenuId(Integer menuId) {
        List<MenuComposition> compositions = new ArrayList<>();
        String sql = "SELECT mc.*, b.nom as burger_nom, b.prix as burger_prix, " +
                "c.nom as complement_nom, c.prix as complement_prix " +
                "FROM menu_composition mc " +
                "LEFT JOIN burger b ON mc.burger_id = b.id " +
                "LEFT JOIN complement c ON mc.complement_id = c.id " +
                "WHERE mc.menu_id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, menuId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MenuComposition comp = new MenuComposition();
                comp.setId(rs.getInt("id"));
                comp.setMenuId(rs.getInt("menu_id"));
                comp.setTypeElement(MenuComposition.TypeElement.valueOf(rs.getString("type_element")));

                if (rs.getInt("burger_id") != 0) {
                    comp.setBurgerId(rs.getInt("burger_id"));
                    Burger burger = new Burger();
                    burger.setId(rs.getInt("burger_id"));
                    burger.setNom(rs.getString("burger_nom"));
                    burger.setPrix(rs.getDouble("burger_prix"));
                    comp.setBurger(burger);
                }

                if (rs.getInt("complement_id") != 0) {
                    comp.setComplementId(rs.getInt("complement_id"));
                    Complement complement = new Complement();
                    complement.setId(rs.getInt("complement_id"));
                    complement.setNom(rs.getString("complement_nom"));
                    complement.setPrix(rs.getDouble("complement_prix"));
                    comp.setComplement(complement);
                }

                compositions.add(comp);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compositions: " + e.getMessage());
        }
        return compositions;
    }

    public boolean update(Menu menu) {
        String sql = "UPDATE menu SET nom = ?, image_url = ?, image_public_id = ?, " +
                "description = ?, statut_archivage = ? WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, menu.getNom());
            pstmt.setString(2, menu.getImageUrl());
            pstmt.setString(3, menu.getImagePublicId());
            pstmt.setString(4, menu.getDescription());
            pstmt.setString(5, menu.getStatutArchivage().getValeur());
            pstmt.setInt(6, menu.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du menu: " + e.getMessage());
        }
        return false;
    }

    public boolean archiver(Integer id) {
        String sql = "UPDATE menu SET statut_archivage = 'ARCHIVE' WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du menu: " + e.getMessage());
        }
        return false;
    }

    public boolean supprimerCompositions(Integer menuId) {
        String sql = "DELETE FROM menu_composition WHERE menu_id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, menuId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des compositions: " + e.getMessage());
        }
        return false;
    }

    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        return new Menu(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("image_url"),
                rs.getString("image_public_id"),
                rs.getString("description"),
                rs.getTimestamp("date_creation").toLocalDateTime(),
                StatutArchivage.fromString(rs.getString("statut_archivage"))
        );
    }
}