package org.example.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    public static Connection getConnection() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/sportnexus";
            String username = "root";
            String password = "";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("Erreur de connexion à la base de données", e);
        }
    }
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connexion réussie à la base de données !");
            } else {
                System.out.println("Connexion échouée !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }
}