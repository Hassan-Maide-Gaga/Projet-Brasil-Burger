package org.example.config.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatabaseImpl implements Database {
    private Connection connection;
    private static DatabaseImpl instance;

    public static DatabaseImpl getInstance(Map<String, String> config) {
        if (instance == null) {
            instance = new DatabaseImpl(config);
        }
        return instance;
    }

    public static DatabaseImpl getInstance(String driver, String url, String user, String pwd) {
        if (instance == null) {
            Map<String, String> config = Map.of(
                    "driver", driver,
                    "url", url,
                    "user", user,
                    "password", pwd
            );
            instance = new DatabaseImpl(config);
        }
        return instance;
    }

    private DatabaseImpl(Map<String, String> config) {
        String driver = config.get("driver");
        String url = config.get("url");
        String user = config.get("user");
        String password = config.get("password");
        this.connection = openConnection(driver, url, user, password);
    }

    private Connection openConnection(String driver, String url, String user, String pwd) {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, pwd);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                instance = null;
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }

    @Override
    public <T> Optional<T> fetch(PreparedStatement ps, Convert<T> convert) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            T data = null;
            if (rs.next()) {
                data = convert.toEntity(rs);
            }
            return Optional.ofNullable(data);
        }
    }

    @Override
    public <T> List<T> fetchAll(PreparedStatement ps, Convert<T> convert) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            List<T> datas = new ArrayList<>();
            while (rs.next()) {
                datas.add(convert.toEntity(rs));
            }
            return datas;
        }
    }

    @Override
    public int executeUpdate(PreparedStatement ps) throws SQLException {
        return ps.executeUpdate();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (connection == null) {
            throw new SQLException("La connexion à la base de données n'est pas établie");
        }
        return connection.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int returnGeneratedKeys) throws SQLException {
        if (connection == null) {
            throw new SQLException("La connexion à la base de données n'est pas établie");
        }
        return connection.prepareStatement(sql, returnGeneratedKeys);
    }
}