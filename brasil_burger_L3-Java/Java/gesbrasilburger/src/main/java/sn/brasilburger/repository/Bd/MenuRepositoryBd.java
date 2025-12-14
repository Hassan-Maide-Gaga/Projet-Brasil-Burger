package sn.brasilburger.repository.Bd;

import sn.brasilburger.config.database.Database;
import sn.brasilburger.entity.Menu;
import sn.brasilburger.repository.MenuRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepositoryBd implements MenuRepository {

    private static MenuRepositoryBd instance = null;
    private Database database;
    private MenuRepositoryBd(Database database) {
        this.database = database;
    }
    public static MenuRepositoryBd getInstance(Database database) {
        if (instance == null) {
            instance = new MenuRepositoryBd(database);
        }
        return instance;
    }
    @Override
    public List<Menu> findAll() {
        List<Menu> menus = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT * FROM menu WHERE etat = TRUE"
            );
            menus = database.<Menu>fetchAll(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return menus;
    }
    @Override
    public Optional<Menu> findById(int id) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT * FROM menu WHERE id=? AND etat = TRUE"
            );
            ps.setInt(1, id);
            return database.<Menu>fetch(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public boolean insert(Menu menu) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "INSERT INTO menu (nom, image, montant, burger_id, etat ) VALUES (?, ?, ?, ?, TRUE) RETURNING id"
            );
            ps.setString(1, menu.getNom());
            ps.setString(2, menu.getImage());
            ps.setDouble(3, menu.getMontant());
            ps.setInt(4, menu.getBurger().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                menu.setId(rs.getInt("id"));
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean update(Menu menu) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE menu SET nom=?, image=?, montant=? WHERE id=?"
            );
            ps.setString(1, menu.getNom());
            ps.setString(2, menu.getImage());
            ps.setDouble(3, menu.getMontant());
            ps.setInt(4, menu.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean delete(Menu menu) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE menu SET etat = FALSE WHERE id=?"
            );
            ps.setInt(1, menu.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'archivage: " + e.getMessage());
        }
        return false;
    }
    private Menu toEntity(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getInt("id"));
        menu.setNom(rs.getString("nom"));
        menu.setImage(rs.getString("image"));
        menu.setMontant(rs.getDouble("montant"));
        menu.setEtat(rs.getBoolean("etat"));
        menu.setComplements(null);
        return menu;
    }
}
