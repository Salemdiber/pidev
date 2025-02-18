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
        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT id_part, id_user, id_event FROM participant";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id_part = rs.getInt("id_part");
                int id_user = rs.getInt("id_user");
                int id_event = rs.getInt("id_event");
                System.out.println("🔍 Chargé depuis DB : ID = " + id_part);
                participants.add(new Participant(id_part, id_user, id_event));
            }
        }
        return participants;
    }

    public String getUserNameById(int idUser) throws SQLException {
        String sql = "SELECT nom, prenom FROM user WHERE id_user = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idUser);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String nom = resultSet.getString("nom");
            String prenom = resultSet.getString("prenom");
            return nom + " " + prenom;
        }
        return "Utilisateur inconnu"; // Si l'ID n'existe pas
    }
}

