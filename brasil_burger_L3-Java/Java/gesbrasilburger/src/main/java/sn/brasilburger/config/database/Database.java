package sn.brasilburger.config.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

public interface Database {
    Connection getConnection();
    boolean isConnected();
    void closeConnection();
    <T> List<T> fetchAll(PreparedStatement ps, Convert<T> convert) throws SQLException;
    <T> Optional<T> fetch(PreparedStatement ps, Convert<T> convert) throws SQLException;
}
