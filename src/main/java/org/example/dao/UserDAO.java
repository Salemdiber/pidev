package org.example.dao;

import org.example.model.User;
import org.example.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public boolean insertUser(User user) {
        String insertSQL = "INSERT INTO user (nom, prenom, email, role, mdp) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = MyDataBase.getInstance().getConnection();
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

    public boolean updateUser(User user) {
        String updateSQL = "UPDATE user SET nom = ?, prenom = ?, email = ?, role = ?, mdp = ? WHERE id_user = ?";

        try (Connection connection = MyDataBase.getInstance().getConnection();
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

    public boolean deleteUser(int userId) throws SQLException {
        String deleteSQL = "DELETE FROM user WHERE id_user = ?";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw e; // Rethrow the exception to handle it at a higher level
        }
    }
}
