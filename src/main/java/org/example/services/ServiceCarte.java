package org.example.services;

import org.example.entities.Carte;
import org.example.entities.MiniGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCarte implements IService<Carte> {
    public Connection connection;
    public ServiceCarte() {connection= org.example.utils.MyDataBase.getInstance().getConnection();}

    @Override
    public void  ajouter_t(Carte carte) throws SQLException
    {
        String sql="INSERT INTO `carte`(`rarite`, `score`, `image`, `id_user`) VALUES (?,?,?,?)";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setString(1,carte.getRarite());
        ps.setInt(2,carte.getScore());
        ps.setString(3,carte.getImage());
        ps.setInt(4,carte.getId_user());
         ps.executeUpdate();

    }
    @Override
    public int modifier(Carte carte,int id)throws SQLException
    {
        String sql="UPDATE `carte` SET `rarite`=?,`score`=?,`image`=?,`id_user`=? WHERE `id`=?";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setString(1,carte.getRarite());
        ps.setInt(2,carte.getScore());
        ps.setString(3,carte.getImage());
        ps.setInt(4,carte.getId_user());
        ps.setInt(5,id);

        return ps.executeUpdate();
    }

    @Override
    public boolean ajouteru(Carte carte) throws SQLException {
        return false;
    }

    @Override
    public boolean modifieru(Carte carte) throws SQLException {
        return false;
    }

    @Override
    public boolean supprimeru(int id) throws SQLException {
        return false;
    }

    @Override
    public void supprimer_t(int id)throws SQLException
    {
        String sql="DELETE FROM `carte` WHERE `id`=?";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(1,id);
         ps.executeUpdate();
    }
    @Override
    public List<Carte> afficher_t() throws SQLException
    {
        Statement st=connection.createStatement();
        List<Carte> cartes = new ArrayList<>();
        String sql="SELECT * From `carte`";
        ResultSet rs=st.executeQuery(sql);
        while (rs.next())
        {
            Carte carte =new Carte();
            carte.setId(rs.getInt("id"));
            carte.setRarite(rs.getString("rarite"));
            carte.setScore(rs.getInt("score"));
            carte.setImage(rs.getString("image"));
            carte.setId_user(rs.getInt("id_user"));
            cartes.add(carte);
        }
        return cartes;
    }
    public boolean VerifierCarte(String rarite1, int id) throws SQLException {
        String sql = "SELECT * FROM `carte` WHERE `rarite`=? AND `id_user`=?";
        PreparedStatement s = connection.prepareStatement(sql);
        s.setString(1, rarite1);
        s.setInt(2, id);
        ResultSet rs = s.executeQuery();
        if (rs.next()) {
            return false;
        } else {
            return true;
        }
    }
    public List<Carte> afficher(int id) throws SQLException {
        Statement st = connection.createStatement();
        List<Carte> cartes = new ArrayList<>();
        String sql = "SELECT * FROM `carte` WHERE `id_user` = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Carte carte =new Carte();
            carte.setId(rs.getInt("id"));
            carte.setRarite(rs.getString("rarite"));
            carte.setScore(rs.getInt("score"));
            carte.setImage(rs.getString("image"));
            carte.setId_user(rs.getInt("id_user"));
            cartes.add(carte);
        }
        return cartes;
    }

    @Override
    public void modifier_t(Carte carte) throws SQLException {

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
