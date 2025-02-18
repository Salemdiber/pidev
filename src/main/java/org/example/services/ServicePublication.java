package org.example.services;

import org.example.entities.Publication;
import org.example.services.IService;

import org.example.utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePublication implements IService<Publication> {
    private Connection connection;

    public ServicePublication() {

        connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter_t(Publication publication) throws SQLException {
        String sql = "INSERT INTO publication (titre, describ) VALUES (?, ?)";  // Include 'id_user'
        System.out.println("SQL Query: " + sql);

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println("Setting parameters:");
            System.out.println("Title: " + publication.getTitre());
            ps.setString(1, publication.getTitre());

            System.out.println("Description: " + publication.getDescrib());
            ps.setString(2, publication.getDescrib());



            System.out.println("Executing update...");
            int rowsAffected = ps.executeUpdate();
            System.out.println("Update executed. Rows affected: " + rowsAffected);

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    publication.setIdPub(generatedId);
                    System.out.println("Generated ID: " + generatedId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in ajouter(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Exiting ajouter()");
    }






    public void ajouter1(Publication publication) throws SQLException {
            String sql = "INSERT INTO publication (titre, describ, id_user) VALUES (?, ?, ?)";
            System.out.println("SQL Query: " + sql);

            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                System.out.println("Setting parameters:");
                System.out.println("Title: " + publication.getTitre());
                ps.setString(1, publication.getTitre());

                System.out.println("Description: " + publication.getDescrib());
                ps.setString(2, publication.getDescrib());
                ps.setInt(3, publication.getIdUser());

                System.out.println("Executing update...");
                int rowsAffected = ps.executeUpdate();
                System.out.println("Update executed. Rows affected: " + rowsAffected);


                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        publication.setIdPub(generatedId);
                        System.out.println("Generated ID: " + generatedId);
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error in ajouter(): " + e.getMessage());
                e.printStackTrace();
                throw e; // Re-throw the exception
            }
            System.out.println("Exiting ajouter()");
        }

    @Override
    public void modifier_t(Publication publication) throws SQLException {
        String sql = "UPDATE publication SET titre=?, describ=?, id_user=? WHERE id_pub=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, publication.getTitre());
            ps.setString(2, publication.getDescrib());
            ps.setInt(3, publication.getIdUser());
            ps.setInt(4, publication.getIdPub());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer_t(int id) throws SQLException {
        String sql = "DELETE FROM publication WHERE id_pub=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Publication> afficher_t() throws SQLException {
        List<Publication> publications = new ArrayList<>();
        String sql = "SELECT * FROM publication";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                publications.add(new Publication(
                        rs.getInt("id_pub"),
                        rs.getString("titre"),
                        rs.getString("describ"),
                        rs.getInt("id_user")
                ));
            }
        }
        return publications;
    }
}

