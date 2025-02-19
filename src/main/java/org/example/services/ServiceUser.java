package org.example.services;

import org.example.entities.User;
import org.example.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    public Connection connection;

    @Override
    public boolean ajouter(User user) throws SQLException {
        String insertSQL = "INSERT INTO user (nom, prenom, email, role, mdp) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getMdp());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifier(User user) throws SQLException {
        String updateSQL = "UPDATE user SET nom = ?, prenom = ?, email = ?, role = ?, mdp = ? WHERE id_user = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getMdp());
            preparedStatement.setInt(6, user.getIdUser());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean supprimer(int userId) throws SQLException {
        String deleteSQL = "DELETE FROM user WHERE id_user = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw e;
        }

    }

    @Override
    public List<User> afficher() throws SQLException {
        Connection connection = DataBase.getConnection();
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM `user`";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery(sql);

        while (rs.next()) {
            User user = new User();
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));
            user.setMdp(rs.getString("mdp"));
            users.add(user);
        }
        return users;

    }
}
