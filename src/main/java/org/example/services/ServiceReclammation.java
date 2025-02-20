package org.example.services;

import org.example.entities.Reclammation;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclammation implements IService<Reclammation> {

    @Override
    public boolean ajouteru(Reclammation reclammation) throws SQLException {
        String insertSQL = "INSERT INTO reclammation (id_user, sujet, description/*, statut*/) VALUES (?, ?, ?)";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, reclammation.getId_user());
            preparedStatement.setString(2, reclammation.getSujet());
            preparedStatement.setString(3, reclammation.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifieru(Reclammation reclammation) throws SQLException {
        String updateSQL = "UPDATE reclammation SET sujet = ?, description = ?, statut = ? WHERE id_reclammation = ?";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {


            preparedStatement.setString(1, reclammation.getSujet());
            preparedStatement.setString(2, reclammation.getDescription());
            preparedStatement.setString(3, reclammation.getStatut());
            preparedStatement.setInt(4, reclammation.getId_reclammation());


            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean supprimeru(int reclammationId) throws SQLException {
        String deleteSQL = "DELETE FROM reclammation WHERE id_reclammation = ?";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, reclammationId);


            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public void ajouterEquipeAMatch(int idMatch, int idEquipe) throws SQLException {

    }

    @Override
    public void supprimerEquipeDuMatch(int idMatch, int idEquipe) throws SQLException {

    }

    @Override
    public List<Integer> getEquipesParMatch(int idMatch) throws SQLException {
        return List.of();
    }

    @Override
    public List<Integer> getMatchsParEquipe(int idEquipe) throws SQLException {
        return List.of();
    }

    @Override
    public void ajouter_t(Reclammation reclammation) throws SQLException {

    }

    @Override
    public void supprimer_t(int id) throws SQLException {

    }

    @Override
    public void modifier_t(Reclammation reclammation) throws SQLException {

    }

    @Override
    public List<Reclammation> afficher_t() throws SQLException {

        List<Reclammation> reclammations = new ArrayList<>();
        String sql = "SELECT * FROM reclammation";


        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {


            while (rs.next()) {
                Reclammation reclammation = new Reclammation(
                        rs.getInt("id_user"),
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getTimestamp("date_creation"),
                        rs.getString("statut")
                );
                reclammation.setId_reclammation(rs.getInt("id_reclammation"));
                reclammations.add(reclammation);
            }
        }


        return reclammations;
    }

    @Override
    public int modifier(Reclammation reclammation, int id) throws SQLException {
        return 0;
    }

    public List<Reclammation> afficher(int idUser) throws SQLException {
        List<Reclammation> reclammations = new ArrayList<>();
        String sql = "SELECT * FROM reclammation WHERE id_user = ?";
        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);

            try (ResultSet rs = preparedStatement.executeQuery()) {

                while (rs.next()) {
                    Reclammation reclammation = new Reclammation(
                            rs.getInt("id_user"),
                            rs.getString("sujet"),
                            rs.getString("description"),
                            rs.getTimestamp("date_creation"),
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
