package org.example.config.factory.database;

import java.util.HashMap;
import java.util.Map;

public final class EntityManager {

    private EntityManager() {
    }

    public static Map<String, String> persistanceUnit(SGDBName sgbdName) {
        switch (sgbdName) {
            case POSTGRESQL:
                return persistanceUnitPostgre();
            case MYSQL:
                return persistanceUnitMysql();
            default:
                throw new IllegalArgumentException("SGBD non supporté: " + sgbdName);
        }
    }

    private static Map<String, String> persistanceUnitMysql() {
        Map<String, String> config = new HashMap<>();
        config.put("driver", "com.mysql.cj.jdbc.Driver");
        config.put("url", "jdbc:mysql://localhost:3306/brasil_burger1");
        config.put("user", "root");
        config.put("password", "");
        return config;
    }

    private static Map<String, String> persistanceUnitPostgre() {
        Map<String, String> config = new HashMap<>();
        config.put("driver", "org.postgresql.Driver");
        config.put("url", "jdbc:postgresql://localhost:5432/brasil_burger");
        config.put("user", "postgres");
        config.put("password", "passer");
        return config;
    }
}