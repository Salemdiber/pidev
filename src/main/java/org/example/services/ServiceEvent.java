package org.example.services;

import org.example.entities.event;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



public class ServiceEvent implements IService<event> {
    private Connection connection;
    public ServiceEvent() {connection = MyDataBase.getInstance().getConnection();}

    @Override
    public void ajouter(event event) throws SQLException {
        /*String sql = "INSERT INTO `event`(`nom`,`image` , `desc`,`lieu`,`date`) VALUES ('"+event.getNom()+"','"+event.getImage()+"','"+event.getDesc()+"','"+event.getLieu()+"','"+event.getDate()+"')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);*/


        if (event.getDate() == null) {
            throw new SQLException("Erreur : la date de l'événement ne peut pas être null !");
        }

        String sql = "INSERT INTO event (nom, image, desc_event, lieu, date) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, event.getNom());
        statement.setString(2, event.getImage());
        statement.setString(3, event.getDesc());
        statement.setString(4, event.getLieu());
        statement.setTimestamp(5, Timestamp.valueOf(event.getDate()));

        statement.executeUpdate();
        System.out.println("bien ajouté");

    }

    @Override
    public void modifier(event event) throws SQLException {
        String sql = "UPDATE `event` SET `nom`=?,`image`=?,`desc_event`=?,`lieu`=?,`date`=? WHERE id_event = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, event.getNom());
        ps.setString(2, event.getImage());
        ps.setString(3, event.getDesc());
        ps.setString(4, event.getLieu());
        ps.setTimestamp(5, Timestamp.valueOf(event.getDate()));
        ps.setInt(6, event.getId_event());
        ps.executeUpdate();
        System.out.println("bien modifié");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `event` WHERE id_event =?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("bien supprimer");

    }

    @Override
    public List<event> afficher() throws SQLException {
        List<event> events = new ArrayList<>();
        String sql = "SELECT * FROM `event`";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int idEvent = rs.getInt("id_event");
            String nom = rs.getString("nom");
            String image = rs.getString("image");
            String descEvent = rs.getString("desc_event");
            String lieu = rs.getString("lieu");
            LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

            events.add(new event(idEvent,nom, image, descEvent, lieu, date));
        }
        return events;
    }

}
