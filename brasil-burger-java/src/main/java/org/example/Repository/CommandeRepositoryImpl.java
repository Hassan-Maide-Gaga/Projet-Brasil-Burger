package org.example.Repository;

import org.example.Model.Commande;
import org.example.Model.Enum.StatutCommande;
import org.example.Model.Enum.ModeConsommation;
import org.example.config.database.Convert;
import org.example.config.database.Database;
import org.example.config.factory.database.DatabaseFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandeRepositoryImpl implements CommandeRepository {

    private final Database database;

    public CommandeRepositoryImpl() {
        this.database = DatabaseFactory.getInstance();
    }

    private Convert<Commande> commandeConvert = new Convert<Commande>() {
        @Override
        public Commande toEntity(ResultSet rs) throws SQLException {
            Commande commande = new Commande();
            commande.setId(rs.getInt("id"));
            commande.setDate(rs.getObject("date", LocalDateTime.class));
            commande.setTotal(rs.getDouble("total"));
            commande.setClientId(rs.getInt("client_id"));

            int livreurId = rs.getInt("livreur_id");
            if (!rs.wasNull()) {
                commande.setLivreurId(livreurId);
            }

            String adresse = rs.getString("adresse_livraison");
            if (adresse != null) {
                commande.setAdresseLivraison(adresse);
            }

            try {
                commande.setStatut(StatutCommande.valueOf(rs.getString("statut")));
            } catch (IllegalArgumentException e) {
                commande.setStatut(StatutCommande.EN_ATTENTE);
            }

            try {
                commande.setModeConsommation(ModeConsommation.valueOf(rs.getString("mode_consommation")));
            } catch (IllegalArgumentException e) {
                commande.setModeConsommation(ModeConsommation.SUR_PLACE);
            }

            return commande;
        }
    };

    @Override
    public Commande save(Commande commande) {
        String sql = "INSERT INTO commande (date, statut, mode_consommation, adresse_livraison, " +
                "total, client_id, livreur_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, commande.getDate());
            ps.setString(2, commande.getStatut().name());
            ps.setString(3, commande.getModeConsommation().name());
            ps.setString(4, commande.getAdresseLivraison());
            ps.setDouble(5, commande.getTotal());
            ps.setInt(6, commande.getClientId());

            if (commande.getLivreurId() > 0) {
                ps.setInt(7, commande.getLivreurId());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        commande.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return commande;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la commande: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Commande update(Commande commande) {
        String sql = "UPDATE commande SET date = ?, statut = ?, mode_consommation = ?, " +
                "adresse_livraison = ?, total = ?, client_id = ?, livreur_id = ? WHERE id = ?";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, commande.getDate());
            ps.setString(2, commande.getStatut().name());
            ps.setString(3, commande.getModeConsommation().name());
            ps.setString(4, commande.getAdresseLivraison());
            ps.setDouble(5, commande.getTotal());
            ps.setInt(6, commande.getClientId());

            if (commande.getLivreurId() > 0) {
                ps.setInt(7, commande.getLivreurId());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            ps.setInt(8, commande.getId());

            ps.executeUpdate();
            return commande;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la commande: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM commande WHERE id = ?";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la commande: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Commande> findById(int id) {
        String sql = "SELECT * FROM commande WHERE id = ?";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            return database.fetch(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la commande: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Commande> findAll() {
        String sql = "SELECT * FROM commande ORDER BY date DESC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Commande> findByStatut(StatutCommande statut) {
        String sql = "SELECT * FROM commande WHERE statut = ? ORDER BY date DESC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, statut.name());
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par statut: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Commande> findByClient(int clientId) {
        String sql = "SELECT * FROM commande WHERE client_id = ? ORDER BY date DESC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clientId);
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par client: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Commande> findByLivreur(int livreurId) {
        String sql = "SELECT * FROM commande WHERE livreur_id = ? ORDER BY date DESC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, livreurId);
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par livreur: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Commande> findByModeConsommation(ModeConsommation mode) {
        String sql = "SELECT * FROM commande WHERE mode_consommation = ? ORDER BY date DESC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, mode.name());
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par mode de consommation: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateStatut(int id, StatutCommande statut) {
        String sql = "UPDATE commande SET statut = ? WHERE id = ?";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, statut.name());
            ps.setInt(2, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean assignerLivreur(int commandeId, int livreurId) {
        String sql = "UPDATE commande SET livreur_id = ? WHERE id = ?";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, livreurId);
            ps.setInt(2, commandeId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'assignation du livreur: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Commande> findByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        String sql = "SELECT * FROM commande WHERE date BETWEEN ? AND ? ORDER BY date DESC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, dateDebut);
            ps.setObject(2, dateFin);
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par plage de dates: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Commande> findByDate(LocalDate date) {
        String sql = "SELECT * FROM commande WHERE DATE(date) = ? ORDER BY date DESC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, date);
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par date: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public double calculateChiffreAffairesByDate(LocalDate date) {
        String sql = "SELECT COALESCE(SUM(total), 0) as chiffre_affaires FROM commande " +
                "WHERE DATE(date) = ? AND statut IN ('LIVREE', 'TERMINEE')";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("chiffre_affaires");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du chiffre d'affaires: " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    public List<Commande> findCommandesEnAttente() {
        String sql = "SELECT * FROM commande WHERE statut = 'EN_ATTENTE' ORDER BY date ASC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes en attente: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Commande> findCommandesEnCours() {
        String sql = "SELECT * FROM commande WHERE statut IN ('EN_PREPARATION', 'EN_LIVRAISON') " +
                "ORDER BY date ASC";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, commandeConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes en cours: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}