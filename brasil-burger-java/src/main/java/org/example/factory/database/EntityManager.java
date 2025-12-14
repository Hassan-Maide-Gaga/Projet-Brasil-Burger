// Fichier: src/main/java/org/example/factory/database/EntityManager.java
package org.example.factory.database;

import org.example.config.Database;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class EntityManager implements Database {
    private Connection connection;
    private Properties properties;

    public EntityManager() {
        loadProperties();
        initializeConnection();
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Fichier application.properties introuvable");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de application.properties: " + e.getMessage());
        }
    }

    private void initializeConnection() {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver");

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✓ Connexion à la base de données établie avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Driver JDBC introuvable: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Erreur de connexion à la base de données: " + e.getMessage());
            System.err.println("  URL: " + url);
            System.err.println("  Veuillez vérifier vos credentials Neon PostgreSQL dans application.properties");
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la connexion: " + e.getMessage());
        }
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
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}