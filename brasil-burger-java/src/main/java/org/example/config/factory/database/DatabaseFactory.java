package org.example.config.factory.database;

import org.example.config.database.Database;
import org.example.config.database.DatabaseImpl;

public final class DatabaseFactory {
    private static final SGDBName sgbdName = SGDBName.MYSQL; // Changez en POSTGRESQL si besoin

    private DatabaseFactory() {
    }

    public static Database getInstance() {
        return DatabaseImpl.getInstance(EntityManager.persistanceUnit(sgbdName));
    }
}