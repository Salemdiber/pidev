package org.example.services;

import org.example.entities.participant;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipant implements IService<participant> {
    private Connection connection;
    public ServiceParticipant() {connection = MyDataBase.getInstance().getConnection();}

    @Override
    public void ajouter(participant participant) throws SQLException {
        String sql = "INSERT INTO participant (id_user,id_event) VALUES ( ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, participant.getId_user());
        statement.setInt(2, participant.getId_event());
        statement.executeUpdate();
        System.out.println("bien ajouté");


    }

    @Override
    public void modifier(participant participant) throws SQLException {
        String sql = "UPDATE `participant` SET `id_user`=?,`id_event`=? WHERE id_part = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, participant.getId_user());
        ps.setInt(2, participant.getId_event());
        ps.setInt(3, participant.getId_part());
        ps.executeUpdate();
        System.out.println("bien modifie");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `participant` WHERE id_part =?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("bien supprimer");
    }

    @Override
    public List<participant> afficher() throws SQLException {
        List<participant> participants = new ArrayList<>();
        String sql = " SELECT * FROM `participant`";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            participants.add(new participant(rs.getInt("id_part"), rs.getInt("id_user"), rs.getInt("id_event")));
        }
        return participants;
    }
}
