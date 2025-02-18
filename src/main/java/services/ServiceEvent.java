package services;

import entities.Event;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvent implements IService<Event> {
        private Connection connection = MyDataBase.getInstance().getConnection();

        public ServiceEvent() {
        }

        public void ajouter(Event event) throws SQLException {
            if (event.getDate() == null) {
                throw new SQLException("Erreur : la date de l'événement ne peut pas être null !");
            } else {
                String sql = "INSERT INTO event (nom, image, desc_event, lieu, date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = this.connection.prepareStatement(sql);
                statement.setString(1, event.getNom());
                statement.setString(2, event.getImage());
                statement.setString(3, event.getDesc());
                statement.setString(4, event.getLieu());
                statement.setTimestamp(5, Timestamp.valueOf(event.getDate()));
                statement.executeUpdate();
                System.out.println("bien ajouté");
            }
        }

        public void modifier(Event event) throws SQLException {
            String sql = "UPDATE `event` SET `nom`=?,`image`=?,`desc_event`=?,`lieu`=?,`date`=? WHERE id_event = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, event.getNom());
            ps.setString(2, event.getImage());
            ps.setString(3, event.getDesc());
            ps.setString(4, event.getLieu());
            ps.setTimestamp(5, Timestamp.valueOf(event.getDate()));
            ps.setInt(6, event.getId_event());
            ps.executeUpdate();
            System.out.println("bien modifié");
        }

        public void supprimer(int id) throws SQLException {
            String sql = "DELETE FROM `event` WHERE id_event =?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("bien supprimer");
        }

        public List<Event> afficher() throws SQLException {
            List<Event> events = new ArrayList();
            String sql = "SELECT * FROM `event`";
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                int idEvent = rs.getInt("id_event");
                String nom = rs.getString("nom");
                String image = rs.getString("image");
                String descEvent = rs.getString("desc_event");
                String lieu = rs.getString("lieu");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
                events.add(new Event(idEvent, nom, image, descEvent, lieu, date));
            }

            return events;
        }
    public void participer(int idUser, int idEvent) throws SQLException {
        // Vérifier si l'utilisateur existe
        String checkUserSql = "SELECT COUNT(*) FROM user WHERE id_user = ?";
        PreparedStatement checkUserStmt = this.connection.prepareStatement(checkUserSql);
        checkUserStmt.setInt(1, idUser);
        ResultSet userResult = checkUserStmt.executeQuery();
        userResult.next();
        if (userResult.getInt(1) == 0) {
            throw new SQLException("Erreur : L'utilisateur avec id_user = " + idUser + " n'existe pas !");
        }

        // Vérifier si l'événement existe
        String checkEventSql = "SELECT COUNT(*) FROM event WHERE id_event = ?";
        PreparedStatement checkEventStmt = this.connection.prepareStatement(checkEventSql);
        checkEventStmt.setInt(1, idEvent);
        ResultSet eventResult = checkEventStmt.executeQuery();
        eventResult.next();
        if (eventResult.getInt(1) == 0) {
            throw new SQLException("Erreur : L'événement avec id_event = " + idEvent + " n'existe pas !");
        }

        // Insérer la participation
        String sql = "INSERT INTO participant (id_user, id_event) VALUES (?, ?)";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setInt(1, idUser);
        statement.setInt(2, idEvent);
        statement.executeUpdate();
        System.out.println("Participation ajoutée avec succès !");
    }

    public Event getEventById(int idEvent) {
        Event event = null;
        String query = "SELECT * FROM event WHERE id_event = ?";  // Requête pour récupérer le terrain par ID

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, idEvent);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    event = new Event();
                    event.setId_event(rs.getInt("id_event"));
                    event.setNom(rs.getString("nom"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return event;
    }

}


