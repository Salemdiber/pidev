package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {
    private static MyDB instance;
    private Connection con;

    // Database configuration
    private final String url = "jdbc:mysql://localhost:3306/sportnexus";
    private final String username = "root";
    private final String password = "";

    // Private constructor to enforce Singleton pattern
    private MyDB() {
        connect();
    }

    // Singleton instance retrieval
    public static synchronized MyDB getInstance() {
        if (instance == null) {
            instance = new MyDB();
        }
        return instance;
    }

    // Establish a new connection
    private void connect() {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connection established");
        } catch (SQLException ex) {
            System.err.println("❌ Connection failed: " + ex.getMessage());
        }
    }

    // Get a valid connection
    public synchronized Connection getCon() {
        try {
            if (con == null || con.isClosed() || !con.isValid(2)) {
                System.out.println("🔄 Reconnecting to database...");
                connect();
            }
        } catch (SQLException ex) {
            System.err.println("⚠ Error checking connection status: " + ex.getMessage());
        }
        return con;
    }

    // Close the connection (optional, for cleanup)
    public synchronized void closeCon() {
        if (con != null) {
            try {
                con.close();
                System.out.println("🔌 Connection closed");
            } catch (SQLException ex) {
                System.err.println("⚠ Error closing connection: " + ex.getMessage());
            }
        }
    }
    public Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}