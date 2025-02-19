package org.example.services;

import org.example.entities.Reclammation;
import org.example.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclammation implements IService<Reclammation> {

    @Override
    public boolean ajouter(Reclammation reclammation) throws SQLException {
        String insertSQL = "INSERT INTO reclammation (id_user, sujet, description/*, statut*/) VALUES (?, ?, ?)";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Set parameters for the insert statement
            preparedStatement.setInt(1, reclammation.getId_user());
            preparedStatement.setString(2, reclammation.getSujet());
            preparedStatement.setString(3, reclammation.getDescription());
            //preparedStatement.setString(4, reclammation.getStatut());

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifier(Reclammation reclammation) throws SQLException {
        String updateSQL = "UPDATE reclammation SET sujet = ?, description = ?, statut = ? WHERE id_reclammation = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            // Set parameters for the update statement
            preparedStatement.setString(1, reclammation.getSujet());
            preparedStatement.setString(2, reclammation.getDescription());
            preparedStatement.setString(3, reclammation.getStatut());
            preparedStatement.setInt(4, reclammation.getId_reclammation());

            // Execute the update statement
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean supprimer(int reclammationId) throws SQLException {
        String deleteSQL = "DELETE FROM reclammation WHERE id_reclammation = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            // Set the ID of the reclamation to delete
            preparedStatement.setInt(1, reclammationId);

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reclammation> afficher() throws SQLException {
        // Declare and initialize the list properly
        List<Reclammation> reclammations = new ArrayList<>();
        String sql = "SELECT * FROM reclammation";

        // Open a connection and execute the query
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Iterate over each result in the ResultSet and create a Reclammation object
            while (rs.next()) {
                Reclammation reclammation = new Reclammation(
                        rs.getInt("id_user"),
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getTimestamp("date_creation"), // Using Timestamp for date
                        rs.getString("statut")
                );
                reclammation.setId_reclammation(rs.getInt("id_reclammation"));
                reclammations.add(reclammation);
            }
        }

        // Return the list of Reclammation objects
        return reclammations;
    }
    public List<Reclammation> afficher(int idUser) throws SQLException {
        List<Reclammation> reclammations = new ArrayList<>();
        String sql = "SELECT * FROM reclammation WHERE id_user = ?";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);

            try (ResultSet rs = preparedStatement.executeQuery()) {

                while (rs.next()) {
                    Reclammation reclammation = new Reclammation(
                            rs.getInt("id_user"),
                            rs.getString("sujet"),
                            rs.getString("description"),
                            rs.getTimestamp("date_creation"), // Using Timestamp for date
                            rs.getString("statut")
                    );
                    reclammation.setId_reclammation(rs.getInt("id_reclammation"));
                    reclammations.add(reclammation);
                }
            }
        }
        return reclammations;
    }

}
