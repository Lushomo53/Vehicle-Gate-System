package org.gatesystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(EnvLoader.get("DB_URL"), EnvLoader.get("DB_USER"), EnvLoader.get("DB_PASSWORD"));
    }
}

