package org.example.services;

import org.example.entities.Reservation;
import org.example.utils.MyDB;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IService<Reservation> {
    private final Connection connection;
    public ServiceReservation() {connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter_t(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (date_res, id_user, id_terrain) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setTimestamp(1, reservation.getDate_res());
        ps.setInt(2, reservation.getId_user());
        ps.setInt(3, reservation.getId_terrain());
        ps.executeUpdate();

    }
    @Override
    public void supprimer_t(int id_res) throws SQLException {
        try (Connection connection = MyDB.getInstance().getNewConnection()) {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                throw new SQLException("Connection is closed or invalid");
            }

            String sql = "DELETE FROM reservation WHERE id_res = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id_res);
                ps.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Cannot delete reservation: it is referenced by other records.", e);
        } catch (SQLException e) {
            throw new SQLException("Error deleting reservation: " + e.getMessage(), e);
        }
    }

    @Override
    public void modifier_t(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET date_res = ?, id_user = ?, id_terrain = ? WHERE id_res = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setTimestamp(1, reservation.getDate_res());
        ps.setInt(2, reservation.getId_user());
        ps.setInt(3, reservation.getId_terrain());
        ps.setInt(4, reservation.getId_res());
        ps.executeUpdate();
    }

    @Override
    public List<Reservation> afficher_t() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            reservations.add(new Reservation(
                    rs.getInt("id_res"),
                    rs.getTimestamp("date_res"),
                    rs.getInt("id_user"),
                    rs.getInt("id_terrain")
            ));
        }
        return reservations;
    }
    public String getUserNameById(int idUser) {
        try (Connection connection = MyDB.getInstance().getNewConnection()) {
            if (connection == null) {
                return "❌ Unable to connect to the database";
            }

            String sql = "SELECT nom, prenom FROM user WHERE id_user = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idUser);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("nom") + " " + resultSet.getString("prenom");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠ SQL Error: " + e.getMessage());
            return "❌ Error fetching user name";
        }
        return "Utilisateur inconnu";
    }



    @Override
    public int modifier(Reservation reservation, int id) throws SQLException {
        return 0;
    }

    @Override
    public boolean ajouteru(Reservation reservation) throws SQLException {
        return false;
    }

    @Override
    public boolean modifieru(Reservation reservation) throws SQLException {
        return false;
    }

    @Override
    public boolean supprimeru(int id) throws SQLException {
        return false;
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
}