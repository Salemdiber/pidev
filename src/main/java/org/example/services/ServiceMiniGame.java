package org.example.services;

import org.example.entities.MiniGame;
import org.example.utilis.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceMiniGame implements IService<MiniGame> {
    public Connection connection;
    public ServiceMiniGame() {connection= MyDataBase.getInstance().getConnection();}

    @Override
    public int ajouter(MiniGame miniGame) throws SQLException
    {
        String sql="INSERT INTO `minigame`(`result`, `type`, `userId`, `data`,`score`) VALUES (?,?,?,?,?)";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setString(1,miniGame.getResult());
        ps.setInt(2,miniGame.getType());
        ps.setInt(3,miniGame.getUser());
        ps.setString(4,miniGame.getData());
        ps.setInt(5,miniGame.getScore());
        return ps.executeUpdate();

    }
    @Override
    public int modifier(MiniGame miniGame,int id)throws SQLException
    {
        String sql="UPDATE `minigame` SET `result`=?,`type`=?,`userId`=?,`data`=? ,`score`=? WHERE `id`=?";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setString(1,miniGame.getResult());
        ps.setInt(2,miniGame.getType());
        ps.setInt(3,miniGame.getUser());
        ps.setString(4,miniGame.getData());
        ps.setInt(5,miniGame.getScore());
        ps.setInt(6,id);

        return ps.executeUpdate();
    }
    @Override
    public int supprimer(int id)throws SQLException
    {
        String sql="DELETE FROM `minigame` WHERE `id`=?";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(1,id);
        return ps.executeUpdate();
    }
    @Override
    public List<MiniGame> afficher() throws SQLException
    {
        Statement st=connection.createStatement();
        List<MiniGame> miniGames = new ArrayList<>();
        String sql="SELECT * From `minigame`";
        ResultSet rs=st.executeQuery(sql);
        while (rs.next())
        {
            MiniGame miniGame = new MiniGame();
            miniGame.setId(rs.getInt("id"));
            miniGame.setType(rs.getInt("type"));
            miniGame.setScore(rs.getInt("score"));
            miniGame.setUser(rs.getInt("userId"));
            miniGame.setResult(rs.getString("result"));
            miniGame.setData(rs.getString("data"));
            miniGames.add(miniGame);
        }
        return miniGames;
    }
    public int lastSavedGame() throws SQLException {
        System.out.println("lastSavedGame");
        int lastGameId = -1;
        Statement st=connection.createStatement();
        String query = "SELECT id FROM minigame ORDER BY id DESC LIMIT 1";  // Modify column and table names
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                System.out.println("lastSavedGame");
                lastGameId = rs.getInt("id");
            }

        return lastGameId;
    }
    public String getGameDataById(int id) throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT data FROM minigame WHERE `id` = ?";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs=ps.executeQuery();
        String data="";
        if(rs.next())
        {
            data=rs.getString("data");
        }
        return data;
    }
}
