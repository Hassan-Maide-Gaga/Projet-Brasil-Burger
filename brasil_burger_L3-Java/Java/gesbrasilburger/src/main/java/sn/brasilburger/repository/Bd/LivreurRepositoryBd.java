package sn.brasilburger.repository.Bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sn.brasilburger.config.database.Database;
import sn.brasilburger.entity.Livreur;
import sn.brasilburger.entity.Zone;
import sn.brasilburger.repository.LivreurRepository;

public class LivreurRepositoryBd implements LivreurRepository {
    private static LivreurRepositoryBd instance = null;
    private Database database;
    private LivreurRepositoryBd(Database database) {
        this.database = database;
    }
    public static LivreurRepositoryBd getInstance(Database database) {
        if (instance == null) {
            instance = new LivreurRepositoryBd(database);
        }
        return instance;
    }
    @Override
    public List<Livreur> findAll() {
        List<Livreur> livreurs = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "SELECT * FROM livreur WHERE etat = true"
            );
            livreurs =  database.fetchAll(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return livreurs;
    }
    @Override
    public Optional<Livreur> findById(int id) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT * FROM livreur WHERE id = ? AND etat = true");
            ps.setInt(1, id);
            return database.<Livreur>fetch(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public boolean insert(Livreur livreur) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "INSERT INTO livreur (nom_complet, telephone, zone_id, etat) VALUES (?, ?, ?, true)"
            );
            ps.setString(1, livreur.getNomComplet());
            ps.setString(2, livreur.getTelephone());
            ps.setInt(3, livreur.getZone().getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean update(Livreur livreur) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE livreur SET nom_complet=?, telephone=?, zone_id=? WHERE id=?"
            );
            ps.setString(1, livreur.getNomComplet());
            ps.setString(2, livreur.getTelephone());
            ps.setInt(3, livreur.getZone().getId());
            ps.setInt(4, livreur.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean delete(Livreur livreur) {
        Connection conn = database.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE livreur SET etat = false WHERE id = ?"
            );
            ps.setInt(1, livreur.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'archivage: " + e.getMessage());
        }
        return false;
    }
    private Livreur toEntity(ResultSet rs) throws SQLException {
        Livreur l = new Livreur();
        l.setId(rs.getInt("id"));
        l.setNomComplet(rs.getString("nom_complet"));
        l.setTelephone(rs.getString("telephone"));
        Zone z = new Zone();
        z.setId(rs.getInt("zone_id"));
        l.setZone(z);
        l.setEtat(rs.getBoolean("etat"));
        return l;
    }
}

