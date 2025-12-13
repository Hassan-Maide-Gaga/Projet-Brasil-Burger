package org.example.config.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface Convert<T> {
    T toEntity(ResultSet rs) throws SQLException;
}