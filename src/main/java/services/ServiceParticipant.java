package services;

import entities.Participant;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipant implements IService<Participant> {
    private Connection connection = MyDataBase.getInstance().getConnection();

    public ServiceParticipant() {
    }

    public void ajouter(Participant participant) throws SQLException {
        String sql = "INSERT INTO participant (id_user,id_event) VALUES ( ?, ?)";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setInt(1, participant.getId_user());
        statement.setInt(2, participant.getId_event());
        statement.executeUpdate();
        System.out.println("bien ajouté");
    }

    public void modifier(Participant participant) throws SQLException {
        String sql = "UPDATE `participant` SET `id_user`=?,`id_event`=? WHERE id_part = ?";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setInt(1, participant.getId_user());
        ps.setInt(2, participant.getId_event());
        ps.setInt(3, participant.getId_part());
        ps.executeUpdate();
        System.out.println("bien modifie");
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `participant` WHERE id_part =?";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("bien supprimer");
    }

    public List<Participant> afficher() throws SQLException {
        List<Participant> participants = new ArrayList();
        String sql = " SELECT * FROM `participant`";
        Statement statement = this.connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while(rs.next()) {
            participants.add(new Participant(rs.getInt("id_part"), rs.getInt("id_user"), rs.getInt("id_event")));
        }

        return participants;
    }
}

