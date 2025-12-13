package org.example.Repository;

import org.example.Model.Complement;
import org.example.Model.Enum.TypeComplement;
import org.example.Model.Enum.TypeProduit;
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

public class ComplementRepositoryImpl implements ComplementRepository {

    private final Database database;

    public ComplementRepositoryImpl() {
        this.database = DatabaseFactory.getInstance();
    }

    private Convert<Complement> complementConvert = new Convert<Complement>() {
        @Override
        public Complement toEntity(ResultSet rs) throws SQLException {
            Complement complement = new Complement();
            complement.setId(rs.getInt("id"));
            complement.setNom(rs.getString("nom"));
            complement.setPrix(rs.getDouble("prix"));
            complement.setImage(rs.getString("image"));
            complement.setDescription(rs.getString("description"));
            complement.setEstArchive(rs.getBoolean("est_archive"));
            complement.setDateCreation(rs.getObject("date_creation", LocalDateTime.class));

            // Récupérer le type de complément depuis la table complement
            String sqlType = "SELECT type, taille FROM complement WHERE id_complement = ?";
            try (PreparedStatement ps = database.prepareStatement(sqlType, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, complement.getId());
                try (ResultSet rsType = ps.executeQuery()) {
                    if (rsType.next()) {
                        try {
                            complement.setTypeComplement(TypeComplement.valueOf(rsType.getString("type")));
                        } catch (IllegalArgumentException e) {
                            complement.setTypeComplement(TypeComplement.SAUCE);
                        }
                        complement.setTaille(rsType.getString("taille"));
                    }
                }
            }

            return complement;
        }
    };

    @Override
    public Complement save(Complement complement) {
        // Insérer d'abord dans produit
        String sqlProduit = "INSERT INTO produit (nom, prix, image, description, type, est_archive) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = database.prepareStatement(sqlProduit, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, complement.getNom());
            ps.setDouble(2, complement.getPrix());
            ps.setString(3, complement.getImage());
            ps.setString(4, complement.getDescription());
            ps.setString(5, TypeProduit.COMPLEMENT.name());
            ps.setBoolean(6, complement.isEstArchive());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        complement.setId(id);

                        // Insérer dans la table complement
                        String sqlComplement = "INSERT INTO complement (id_complement, type, taille) VALUES (?, ?, ?)";
                        try (PreparedStatement psComplement = database.prepareStatement(sqlComplement, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            psComplement.setInt(1, id);
                            psComplement.setString(2, complement.getTypeComplement().name());
                            psComplement.setString(3, complement.getTaille());
                            psComplement.executeUpdate();
                        }
                    }
                }
            }
            return complement;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du complément: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Complement update(Complement complement) {
        // Mettre à jour produit
        String sqlProduit = "UPDATE produit SET nom = ?, prix = ?, image = ?, description = ?, " +
                "est_archive = ? WHERE id = ? AND type = 'COMPLEMENT'";

        try (PreparedStatement ps = database.prepareStatement(sqlProduit, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, complement.getNom());
            ps.setDouble(2, complement.getPrix());
            ps.setString(3, complement.getImage());
            ps.setString(4, complement.getDescription());
            ps.setBoolean(5, complement.isEstArchive());
            ps.setInt(6, complement.getId());

            ps.executeUpdate();

            // Mettre à jour complement
            String sqlComplement = "UPDATE complement SET type = ?, taille = ? WHERE id_complement = ?";
            try (PreparedStatement psComplement = database.prepareStatement(sqlComplement, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psComplement.setString(1, complement.getTypeComplement().name());
                psComplement.setString(2, complement.getTaille());
                psComplement.setInt(3, complement.getId());
                psComplement.executeUpdate();
            }

            return complement;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du complément: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM produit WHERE id = ? AND type = 'COMPLEMENT'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du complément: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archive(int id) {
        String sql = "UPDATE produit SET est_archive = true WHERE id = ? AND type = 'COMPLEMENT'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'archivage du complément: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Complement> findById(int id) {
        String sql = "SELECT * FROM produit WHERE id = ? AND type = 'COMPLEMENT'";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, id);
            return database.fetch(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du complément: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Complement> findAll() {
        String sql = "SELECT * FROM produit WHERE type = 'COMPLEMENT' ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compléments: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Complement> findByArchive(boolean archive) {
        String sql = "SELECT * FROM produit WHERE type = 'COMPLEMENT' AND est_archive = ? ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, archive);
            return database.fetchAll(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compléments archivés: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Complement> findByName(String nom) {
        String sql = "SELECT * FROM produit WHERE type = 'COMPLEMENT' AND LOWER(nom) LIKE LOWER(?) ORDER BY nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "%" + nom + "%");
            return database.fetchAll(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Complement> findByType(TypeComplement type) {
        String sql = "SELECT p.* FROM produit p " +
                "JOIN complement c ON p.id = c.id_complement " +
                "WHERE p.type = 'COMPLEMENT' AND c.type = ? " +
                "ORDER BY p.nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, type.name());
            return database.fetchAll(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par type: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Complement> findBoissons() {
        String sql = "SELECT p.* FROM produit p " +
                "JOIN complement c ON p.id = c.id_complement " +
                "WHERE p.type = 'COMPLEMENT' AND c.type = 'BOISSON' " +
                "ORDER BY p.nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des boissons: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Complement> findFrites() {
        String sql = "SELECT p.* FROM produit p " +
                "JOIN complement c ON p.id = c.id_complement " +
                "WHERE p.type = 'COMPLEMENT' AND c.type = 'FRITE' " +
                "ORDER BY p.nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            return database.fetchAll(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des frites: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Complement> findByTypeAndArchive(TypeComplement type, boolean archive) {
        String sql = "SELECT p.* FROM produit p " +
                "JOIN complement c ON p.id = c.id_complement " +
                "WHERE p.type = 'COMPLEMENT' AND c.type = ? AND p.est_archive = ? " +
                "ORDER BY p.nom";

        try (PreparedStatement ps = database.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, type.name());
            ps.setBoolean(2, archive);
            return database.fetchAll(ps, complementConvert);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par type et archive: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}