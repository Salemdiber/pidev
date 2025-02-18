package org.example.services;

import org.example.entities.Commentaire;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire implements IService<Commentaire> {
    private final Connection connection;

    public ServiceCommentaire() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter_t(Commentaire commentaire) throws SQLException {
        String sql = "INSERT INTO commentaire`(desc_com`, id_user, id_pub) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, commentaire.getDesc());
            ps.setInt(2, commentaire.getIdUser());
            ps.setInt(3, commentaire.getIdPub());
            ps.executeUpdate();
        }
    }

    @Override
    public void modifier_t(Commentaire commentaire) throws SQLException {
        String sql = "UPDATE commentaire SET id_pub`=?, desc_com`=?, id_user`=? WHERE id_com`=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, commentaire.getIdPub());
            ps.setString(2, commentaire.getDesc());
            ps.setInt(3, commentaire.getIdUser());
            ps.setInt(4, commentaire.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer_t(int id) throws SQLException {
        String sql = "DELETE FROM commentaire WHERE id_com = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Commentaire> afficher_t() throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT c.id_com, c.desc_com, c.id_user, c.id_pub, u.nom " +
                "FROM commentaire c " +
                "JOIN user u ON c.id_user = u.id_user";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                commentaires.add(new Commentaire(
                        rs.getInt("id_com"),
                        rs.getString("desc_com"),
                        rs.getInt("id_user"),
                        rs.getInt("id_pub"),
                        rs.getString("nom")
                ));
            }
        }
        return commentaires;
    }
}