package services;

import entities.Commentaire;
import entities.Publication;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire implements IService<Commentaire> {
    private Connection connection;
    public ServiceCommentaire() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Commentaire commentaire) throws SQLException {
        String sql = "INSERT INTO `commentaire`(`desc_com`, `id_user`, `id_pub`) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,commentaire.getDesc() );
        ps.setInt(2, commentaire.getIdUser());
        ps.setInt(3, commentaire.getIdPub());
        ps.executeUpdate();

    }

    @Override
    public void modifier(Commentaire commentaire) throws SQLException {
        String sql = "UPDATE `commentaire` SET `id_pub`=?, `desc_com`=?, `id_user`=? WHERE `id_com`=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, commentaire.getIdPub());
            ps.setString(2, commentaire.getDesc());
            ps.setInt(3, commentaire.getIdUser());
            ps.setInt(4, commentaire.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `commentaire` WHERE id_com = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

    }

    @Override
    public List<Commentaire> afficher() throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT * FROM commentaire";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                commentaires.add(new Commentaire(
                        rs.getInt("id_com"),
                        rs.getString("desc_com"),
                        rs.getInt("id_user"),
                        rs.getInt("id_pub")
                ));
            }
        }
        return commentaires;
    }
}
