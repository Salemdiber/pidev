package org.example.services;

import org.example.entities.User;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    private Connection connection;

    public ServiceUser() {
        this.connection = MyDataBase.getInstance().getConnection();
        if (this.connection == null) {
            System.err.println("❌ Erreur : La connexion à la base de données est NULL !");
        }
    }

    private void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("🔄 Reconnexion à la base de données...");
            connection = MyDataBase.getInstance().getConnection();
        }
    }

    @Override
    public boolean ajouteru(User user) {
        try {
            checkConnection();
            String insertSQL = "INSERT INTO user (nom, prenom, email, role, mdp) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, user.getNom());
                preparedStatement.setString(2, user.getPrenom());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getRole());
                preparedStatement.setString(5, user.getMdp());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifieru(User user) {
        try {
            checkConnection();
            String updateSQL = "UPDATE user SET nom = ?, prenom = ?, email = ?, role = ?, mdp = ? WHERE id_user = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, user.getNom());
                preparedStatement.setString(2, user.getPrenom());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getRole());
                preparedStatement.setString(5, user.getMdp());
                preparedStatement.setInt(6, user.getIdUser());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean supprimeru(int userId)throws SQLException {
        try {
            checkConnection();
            String deleteSQL = "DELETE FROM user WHERE id_user = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setInt(1, userId);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> afficher_t() throws SQLException {
        List<User> users = new ArrayList<>();
        try {
            checkConnection();
            String sql = "SELECT * FROM user";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet rs = preparedStatement.executeQuery()) {

                while (rs.next()) {
                    User user = new User();
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setMdp(rs.getString("mdp"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return users;
    }

    @Override
    public void ajouterEquipeAMatch(int idMatch, int idEquipe) {
    }

    @Override
    public void supprimerEquipeDuMatch(int idMatch, int idEquipe) {
    }

    @Override
    public List<Integer> getEquipesParMatch(int idMatch) {
        return List.of();
    }

    @Override
    public List<Integer> getMatchsParEquipe(int idEquipe) {
        return List.of();
    }

    @Override
    public void ajouter_t(User user) {
    }

    @Override
    public void supprimer_t(int id) {
    }

    @Override
    public void modifier_t(User user) {
    }

    @Override
    public int modifier(User user, int id) {
        return 0;
    }
}
