package org.example.services;

import org.example.entities.Reservation;
import org.example.entities.Terrain;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IService<Reservation> {
    private Connection connection;
    public ServiceReservation() {connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter_t(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (date_res, id_user, id_terrain) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDate(1, reservation.getDate_res());
        ps.setInt(2, reservation.getId_user());
        ps.setInt(3, reservation.getId_terrain());
        ps.executeUpdate();

    }

    @Override
    public void supprimer_t(int id_res) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id_res = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id_res);
        ps.executeUpdate();
    }

    @Override
    public void modifier_t(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET date_res = ?, id_user = ?, id_terrain = ? WHERE id_res = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDate(1, reservation.getDate_res());
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
                    rs.getDate("date_res"),
                    rs.getInt("id_user"),
                    rs.getInt("id_terrain")
            ));
        }
        return reservations;
    }
    public List<Reservation> getReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations"; // Replace with your actual query

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_reservation");
                int terrainId = resultSet.getInt("id_terrain");
                int userId = resultSet.getInt("id_user");
                Date reservationDate = resultSet.getDate("reservation_date"); // Assuming the column is named "reservation_date"

                Reservation reservation = new Reservation(id, reservationDate, userId,terrainId );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

}
