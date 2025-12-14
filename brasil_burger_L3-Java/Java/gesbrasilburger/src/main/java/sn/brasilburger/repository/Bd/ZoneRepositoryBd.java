package sn.brasilburger.repository.Bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sn.brasilburger.config.database.Database;
import sn.brasilburger.entity.*;
import sn.brasilburger.repository.ZoneRepository;

public class ZoneRepositoryBd implements ZoneRepository {
    private static ZoneRepositoryBd instance = null;
    private Database database;
    private ZoneRepositoryBd(Database database){
        this.database = database;
    }
    public static ZoneRepositoryBd getInstance(Database database){
        if (instance == null) {
            instance = new ZoneRepositoryBd(database);
        }
        return instance;
    }
    @Override
    public List<Zone> findAll(){
        List<Zone> zones = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT * FROM zone WHERE etat = true");
            return database.<Zone>fetchAll(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return zones;
    }
    @Override
    public Optional<Zone> findById(int id) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT * FROM zone WHERE id=? AND etat = true");
            ps.setInt(1, id);
            return database.<Zone>fetch(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return Optional.empty();
    }
    public boolean insert(Zone zone){
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "INSERT INTO zone (nom, prix_livraison) VALUES (?,?)"
            );
            ps.setString(1, zone.getNom());
            ps.setDouble(2, zone.getPrixLivraison());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
        return false ;
    }
    @Override
    public boolean update(Zone zone) {
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "UPDATE zone SET nom=?, prix_livraison=? WHERE id=?"
            );
            ps.setString(1, zone.getNom());
            ps.setDouble(2, zone.getPrixLivraison());
            ps.setInt(3, zone.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean delete(Zone zone) {
        Connection conn = database.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE zone SET etat=false WHERE id=?"
            );
            ps.setInt(1, zone.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'archivage: " + e.getMessage());
        }
        return false;
    }
    private Zone toEntity(ResultSet rs) throws SQLException{
        Zone z = new Zone();
        z.setId(rs.getInt("id"));
        z.setNom(rs.getString("nom"));
        z.setPrixLivraison(rs.getDouble("prix_livraison"));
        return z;
    }
}
