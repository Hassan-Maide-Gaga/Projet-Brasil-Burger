// Fichier: src/main/java/org/example/config/Database.java
package org.example.config;

import java.sql.Connection;

public interface Database {
    Connection getConnection();
    boolean isConnected();
    void closeConnection();
}