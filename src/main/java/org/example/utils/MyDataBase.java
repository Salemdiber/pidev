package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private final String URL = "jdbc:mysql://localhost:3306/sportnexus?autoReconnect=true&useSSL=false";
    private final String USER = "root";
    private final String PSW = "";

    private Connection connection;
    private static MyDataBase instance;

    private MyDataBase() {
        connect();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("✅ Connection established");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
        }
    }

    public static synchronized MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("🔄 Reconnecting to database...");
                connect();
            }
        } catch (SQLException e) {
            System.err.println("⚠ Error checking connection status: " + e.getMessage());
        }
        return connection;
    }
}
