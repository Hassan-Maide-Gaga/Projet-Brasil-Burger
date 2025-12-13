package org.example.config.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Database {
    Connection getConnection();
    boolean isConnected();
    void closeConnection();
    <T> Optional<T> fetch(PreparedStatement ps, Convert<T> convert) throws SQLException;
    <T> List<T> fetchAll(PreparedStatement ps, Convert<T> convert) throws SQLException;
    int executeUpdate(PreparedStatement ps) throws SQLException;

    PreparedStatement prepareStatement(String sql) throws SQLException;

    PreparedStatement prepareStatement(String sqlAdresse, int returnGeneratedKeys) throws SQLException;
}