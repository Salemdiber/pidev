package org.example.dao;

import org.example.model.User;
import org.example.utils.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public boolean insertUser(User user) {
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
    }public boolean updateUser(User user) {
        String updateSQL = "UPDATE user SET nom = ?, prenom = ?, email = ?, role = ? WHERE id_user = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            System.out.println("PreparedStatement created successfully.");  // Debugging

            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setInt(5, user.getIdUser());

            System.out.println("SQL executed with ID: " + user.getIdUser());  // Debugging

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);  // Debugging

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();  // This will print the SQL error if there is one
            return false;
        }
    }


}
