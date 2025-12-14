package sn.brasilburger.repository.Bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import sn.brasilburger.config.database.Database;
import sn.brasilburger.entity.MenuComplement;
import sn.brasilburger.repository.MenuComplementRepository;

public class MenuComplementRepositoryBd implements MenuComplementRepository {
    private Database database;
    private static MenuComplementRepositoryBd instance = null;
    public MenuComplementRepositoryBd(Database database) {
        this.database = database;
    }
    public static Object getInstance(Database database){
        if (instance == null) {
            instance = new MenuComplementRepositoryBd(database);
        }
        return instance;
    }
    @Override
    public boolean insert(MenuComplement menuC){
        Connection conn = database.getConnection();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                "INSERT INTO menu_complement (complement_id,menu_id) VALUES (?, ?)"
            );
            ps.setInt(1, menuC.getComplement().getId());
            ps.setInt(2, menuC.getMenu().getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout: " + e.getMessage());
            return false;
        }
    }
}
