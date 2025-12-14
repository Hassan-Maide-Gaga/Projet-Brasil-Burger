package sn.brasilburger.repository.Bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sn.brasilburger.config.database.Database;
import sn.brasilburger.entity.RoleUser;
import sn.brasilburger.entity.User;
import sn.brasilburger.repository.LoginRepository;

public class LoginRepositoryBd implements LoginRepository{
    private Database database;
    private static LoginRepositoryBd instance = null;
    public LoginRepositoryBd(Database database) {
        this.database = database;
    }
    public static Object getInstance(Database database){
        if (instance == null) {
            instance = new LoginRepositoryBd(database);
        }
        return instance;
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
            Connection conn = database.getConnection();
            PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT * FROM \"user\" WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(toEntity(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return Optional.empty(); 
    }
    @Override
    public boolean insert(User user) {
        String sql = """
            INSERT INTO "user" (nom_complet, telephone, email, password, role)
            VALUES (?, ?, ?, ?, ?::role_user)
        """;
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getNomComplet());
            ps.setString(2, user.getTelephone());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setObject(5, user.getRole().name(), java.sql.Types.OTHER);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean update(User user){
        String sql = """
                UPDATE "user" SET nom_complet=?, telephone=?, email=?, password=? WHERE id=?
                """;
        PreparedStatement ps;
        Connection conn = database.getConnection();
        try {
            ps  = conn.prepareStatement(sql);
            ps.setString(1, user.getNomComplet());
            ps.setString(2, user.getTelephone());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
        return false;
    }
    public boolean delete(User user){
        String sql = """
                UPDATE "user" SET etat=false WHERE id=?
                """;
        PreparedStatement ps;
        Connection conn = database.getConnection();
        try {
            ps  = conn.prepareStatement(sql);
            ps.setInt(1, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'archivage: " + e.getMessage());
        }
        return false;
    }
    @Override 
    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        PreparedStatement ps;
        Connection conn = database.getConnection();
        try {
            ps = conn.prepareStatement("SELECT * FROM \"user\" WHERE role = CAST('GESTIONNAIRE' AS role_user) AND etat=true");
            users = database.<User>fetchAll(ps, this::toEntity);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche: " + e.getMessage());
        }
        return users;
    }
    private User toEntity(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setNomComplet(rs.getString("nom_complet"));
        u.setTelephone(rs.getString("telephone"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(RoleUser.valueOf(rs.getString("role")));
        return u;
    }
}
