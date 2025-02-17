package tn.esprit.services;
import tn.esprit.entities.User;
import tn.esprit.utils.Mydatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {

    public List<User> getAllCoaches() throws SQLException {
        List<User> coaches = new ArrayList<>();
        String sql = "SELECT id_user, nom, role FROM user WHERE role = 'ENTRAINEUR'";

        // Déclarez et gérez la connexion à la base de données ici
        try (Connection conn = Mydatabase.getConnection(); // Utiliser la connexion ici
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User.Role role = User.Role.fromString(rs.getString("role"));
                coaches.add(new User(rs.getInt("id_user"), rs.getString("nom"), role));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
            throw e; // Propager l'exception pour gestion ultérieure
        }

        return coaches;
    }


}
