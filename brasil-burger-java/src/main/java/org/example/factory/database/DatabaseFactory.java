// Fichier: src/main/java/org/example/factory/database/DatabaseFactory.java
package org.example.factory.database;

import org.example.config.Database;

public class DatabaseFactory {
    private static EntityManager instance;

    private DatabaseFactory() {
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized (DatabaseFactory.class) {
                if (instance == null) {
                    instance = new EntityManager();
                }
            }
        }
        return instance;
    }

    public static Database getInstance(SGDBName dbName) {
        switch (dbName) {
            case POSTGRESQL:
                return getInstance();
            default:
                throw new IllegalArgumentException("Base de données non supportée: " + dbName);
        }
    }
}