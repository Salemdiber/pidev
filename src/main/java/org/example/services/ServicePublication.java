package org.example.services;

import org.example.entities.Publication;
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
        String sql = "INSERT INTO publication (titre, describ, id_user) VALUES (?, ?, ?)";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, publication.getTitre());
            ps.setString(2, publication.getDescrib());
            ps.setInt(3, publication.getIdUser());

            int rowsAffected = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    publication.setIdPub(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans ajouter_t: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public void modifier_t(Publication publication) throws SQLException {
        String sql = "UPDATE publication SET titre=?, describ=?, id_user=? WHERE id_pub=?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
        String sql = "SELECT p.id_pub, p.titre, p.describ, p.id_user, u.nom " +
                "FROM publication p " +
                "JOIN user u ON p.id_user = u.id_user";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                publications.add(new Publication(
                        rs.getInt("id_pub"),
                        rs.getString("titre"),
                        rs.getString("describ"),
                        rs.getInt("id_user"),
                        rs.getString("nom")
                ));
            }
        }
        return publications;
    }


    public String getUserNameById(int idUser) {
        String sql = "SELECT nom FROM user WHERE id_user = ?";
        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Utilisateur inconnu";
    }


    @Override
    public int modifier(Publication publication, int id) throws SQLException {
        return 0;
    }

    @Override
    public boolean ajouteru(Publication publication) throws SQLException {
        return false;
    }

    @Override
    public boolean modifieru(Publication publication) throws SQLException {
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