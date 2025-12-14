package sn.brasilburger.repository.Bd;

import sn.brasilburger.config.database.Database;
import sn.brasilburger.entity.Burger;
import sn.brasilburger.repository.BurgerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurgerRepositoryBd implements BurgerRepository {
    private static BurgerRepositoryBd instance = null;
    private Database database;
    private BurgerRepositoryBd(Database database) {
        this.database = database;
    }
    public static BurgerRepositoryBd getInstance(Database database) {
        if (instance == null) {
            instance = new BurgerRepositoryBd(database);
        }
        return instance;
    }
    @Override
    public List<Burger> findAll() {
        List<Burger> burgers = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT * FROM burger WHERE etat = true"
            );
            burgers = database.fetchAll(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return burgers;
    }
    @Override
    public Optional<Burger> findById(int id) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT * FROM burger WHERE id = ? AND etat = true"
            );
            ps.setInt(1, id);
            return database.fetch(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public boolean insert(Burger burger) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "INSERT INTO burger (nom, prix, image, etat) VALUES (?, ?, ?, true)"
            );
            ps.setString(1, burger.getNom());
            ps.setDouble(2, burger.getPrix());
            ps.setString(3, burger.getImage());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean update(Burger burger) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE burger SET nom = ?, prix = ?, image = ? WHERE id = ?"
            );
            ps.setString(1, burger.getNom());
            ps.setDouble(2, burger.getPrix());
            ps.setString(3, burger.getImage());
            ps.setInt(4, burger.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean delete(Burger burger) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE burger SET etat = false WHERE id = ?"
            );
            ps.setInt(1, burger.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'archivage: " + e.getMessage());
        }
        return false;
    }
    @Override
    public Optional<Burger> getBurgerByMenu(sn.brasilburger.entity.Menu menu)
    {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT b.* FROM burger b JOIN menu m ON b.id = m.burger_id WHERE m.id = ? AND b.etat = true"
            );
            ps.setInt(1, menu.getId());
            return database.fetch(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du burger par menu: " + e.getMessage());
        }
        return Optional.empty();
    }
    private Burger toEntity(ResultSet rs) throws SQLException {
        Burger b = new Burger();
        b.setId(rs.getInt("id"));
        b.setNom(rs.getString("nom"));
        b.setPrix(rs.getDouble("prix"));
        b.setImage(rs.getString("image"));
        b.setEtat(rs.getBoolean("etat"));
        return b;
    }
}
