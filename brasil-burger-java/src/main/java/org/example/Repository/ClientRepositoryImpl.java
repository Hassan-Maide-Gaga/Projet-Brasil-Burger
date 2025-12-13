package org.example.Repository;

import org.example.Model.Client;
import org.example.Model.Enum.RoleUtilisateur;
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

public class ClientRepositoryImpl implements ClientRepository {

    private final Database database;

    public ClientRepositoryImpl() {
        this.database = DatabaseFactory.getInstance();
    }

    private Convert<Client> clientConvert = new Convert<Client>() {
        @Override
        public Client toEntity(ResultSet rs) throws SQLException {
            Client client = new Client();
            client.setId(rs.getInt("id_utilisateur"));
            client.setNom(rs.getString("nom"));
            client.setPrenom(rs.getString("prenom"));
            client.setTelephone(rs.getString("telephone"));
            client.setEmail(rs.getString("email"));
            client.setMotDePasse(rs.getString("mot_de_passe"));

            try {
                client.setRole(RoleUtilisateur.valueOf(rs.getString("role")));
            } catch (IllegalArgumentException e) {
                client.setRole(RoleUtilisateur.CLIENT);
            }

            client.setDateCreation(rs.getObject("date_creation", LocalDateTime.class));
            client.setEstArchive(rs.getBoolean("est_archive"));

            // Récupérer l'adresse depuis la table client
            String sqlAdresse = "SELECT adresse FROM client WHERE id_client = ?";
            try (PreparedStatement ps = database.prepareStatement(sqlAdresse, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, client.getId());
                try (ResultSet rsAdresse = ps.executeQuery()) {
                    if (rsAdresse.next()) {
                        client.setAdresse(rsAdresse.getString("adresse"));
                    }
                }
            }

            return client;
        }
    };

    @Override
    public Client save(Client client) {
        // Insérer d'abord dans utilisateur
        String sqlUtilisateur = "INSERT INTO utilisateur (nom, prenom, telephone, email, mot_de_passe, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = database.prepareStatement(sqlUtilisateur, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getMotDePasse());
            ps.setString(6, RoleUtilisateur.CLIENT.name());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        client.setId(id);

                        // Insérer dans la table client
                        String sqlClient = "INSERT INTO client (id_client, adresse) VALUES (?, ?)";
                        try (PreparedStatement psClient = database.prepareStatement(sqlClient, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            psClient.setInt(1, id);
                            psClient.setString(2, client.getAdresse());
                            psClient.executeUpdate();
                        }
                    }
                }
            }
            return client;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du client: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Client update(Client client) {
        // Mettre à jour utilisateur
        String sqlUtilisateur = "UPDATE utilisateur SET nom = ?, prenom = ?, telephone = ?, " +
                "email = ?, mot_de_passe = ? WHERE id_utilisateur = ? AND role = 'CLIENT'";

        try (PreparedStatement ps = database.prepareStatement(sqlUtilisateur, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getMotDePasse());
            ps.setInt(6, client.getId());

            ps.executeUpdate();

            // Mettre à jour client
            String sqlClient = "UPDATE client SET adresse = ? WHERE id_client = ?";
            try (PreparedStatement psClient = database.prepareStatement(sqlClient, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psClient.setString(1, client.getAdresse());
                psClient.setInt(2, client.getId());
                psClient.executeUpdate();
            }

            return client;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du client: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean archive(int id) {
        String sql = "UPDATE utilisateur SET est_archive = true WHERE id_utilisateur = ? AND role = 'CLIENT'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du client: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Client> findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ? AND role = 'CLIENT'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            return database.fetch(ps, clientConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND role = 'CLIENT'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            return database.fetch(ps, clientConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Client> findByTelephone(String telephone) {
        String sql = "SELECT * FROM utilisateur WHERE telephone = ? AND role = 'CLIENT'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, telephone);
            return database.fetch(ps, clientConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par téléphone: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT * FROM utilisateur WHERE role = 'CLIENT' ORDER BY nom, prenom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, clientConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Client> findByArchive(boolean archive) {
        String sql = "SELECT * FROM utilisateur WHERE role = 'CLIENT' AND est_archive = ? ORDER BY nom, prenom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, archive);
            return database.fetchAll(ps, clientConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients archivés: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Client> findByName(String nom) {
        String sql = "SELECT * FROM utilisateur WHERE role = 'CLIENT' AND " +
                "(LOWER(nom) LIKE LOWER(?) OR LOWER(prenom) LIKE LOWER(?)) " +
                "ORDER BY nom, prenom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "%" + nom + "%");
            ps.setString(2, "%" + nom + "%");
            return database.fetchAll(ps, clientConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Client> search(String recherche) {
        String sql = "SELECT * FROM utilisateur WHERE role = 'CLIENT' AND " +
                "(LOWER(nom) LIKE LOWER(?) OR LOWER(prenom) LIKE LOWER(?) OR " +
                "telephone LIKE ? OR LOWER(email) LIKE LOWER(?)) " +
                "ORDER BY nom, prenom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String pattern = "%" + recherche + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            return database.fetchAll(ps, clientConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean authentifier(String email, String motDePasse) {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ? AND mot_de_passe = ? " +
                "AND role = 'CLIENT' AND est_archive = false";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Client> Search(String trim) {
        return List.of();
    }
}