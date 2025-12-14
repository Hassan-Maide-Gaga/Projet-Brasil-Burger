package sn.brasilburger.repository.Bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sn.brasilburger.config.database.Database;
import sn.brasilburger.entity.CategorieComplement;
import sn.brasilburger.entity.Complement;
import sn.brasilburger.repository.ComplementRepository;

public class ComplementRepositoryBd implements ComplementRepository {
    private static ComplementRepositoryBd instance = null;
    private Database database;
    private ComplementRepositoryBd(Database database) {
        this.database = database;
    }
    public static ComplementRepositoryBd getInstance(Database database) {
        if (instance == null) {
            instance = new ComplementRepositoryBd(database);
        }
        return instance;
    }
    @Override
    public List<Complement> findAll() {
        List<Complement> complements = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT * FROM complement WHERE etat = true"
            );
            complements =  database.fetchAll(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return complements;
    }
    @Override
    public Optional<Complement> findById(int id) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT * FROM complement WHERE id = ? AND etat = true"
            );
            ps.setInt(1, id);
            return database.<Complement>fetch(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public boolean insert(Complement complement) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "INSERT INTO complement (nom, prix, image, categorie, etat) VALUES (?, ?, ?, ?::categorie_complement, true)"
            );
            ps.setString(1, complement.getNom());
            ps.setDouble(2, complement.getPrix());
            ps.setString(3, complement.getImage());
            ps.setObject(4, complement.getCategorie().name(), java.sql.Types.OTHER);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean update(Complement complement) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE complement SET nom=?, prix=?, image=?, categorie=? WHERE id=?"
            );
            ps.setString(1, complement.getNom());
            ps.setDouble(2, complement.getPrix());
            ps.setString(3, complement.getImage());
            ps.setObject(4, complement.getCategorie().name(), java.sql.Types.OTHER);
            ps.setInt(5, complement.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean delete(Complement complement) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE complement SET etat = false WHERE id = ?"
            );
            ps.setInt(1, complement.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'archivage: " + e.getMessage());
        }
        return false;
    }
    private Complement toEntity(ResultSet rs) throws SQLException {
        Complement c = new Complement();
        c.setId(rs.getInt("id"));
        c.setNom(rs.getString("nom"));
        c.setPrix(rs.getDouble("prix"));
        c.setImage(rs.getString("image"));
        c.setEtat(rs.getBoolean("etat"));
        String catStr = rs.getString("categorie");
        if (catStr != null) {
            c.setCategorie(CategorieComplement.valueOf(catStr.toUpperCase()));
        }
        return c;
    }
    public List<Complement> getComplementsByMenu(int idMenu){
        List<Complement> complements = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT c.* FROM complement c " +
                "JOIN menu_complement mc ON c.id = mc.complement_id " +
                "WHERE mc.menu_id = ? AND c.etat = true"
            );
            ps.setInt(1, idMenu);
            complements = database.fetchAll(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return complements;
    }

}
