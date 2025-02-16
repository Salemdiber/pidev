package org.example.services;
import org.example.entities.Terrain;
import org.example.utils.MyDataBase;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceTerrain implements IService<Terrain> {
    private final Connection connection;

    public ServiceTerrain() {
        connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter_t(Terrain terrain) throws SQLException {
        String sql = "INSERT INTO `terrain`(`nom`,`lieu`,`des`,`img`) VALUES ('"+terrain.getNom()+"','"+terrain.getLieu()+"','"+terrain.getDes()+"','"+terrain.getImg()+"')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void supprimer_t(int id_terrain) throws SQLException {
String sql = "DELETE FROM `terrain` WHERE id_terrain =?";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setInt(1, id_terrain);
ps.executeUpdate();
    }

    @Override
    public void modifier_t(Terrain terrain) throws SQLException {
        String sql = "UPDATE `terrain` SET `nom`=?, `lieu`=?, `des`=?, `img`=? WHERE id_terrain=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, terrain.getNom());
        ps.setString(2, terrain.getLieu());
        ps.setString(3, terrain.getDes());
        ps.setString(4, terrain.getImg());
        ps.setInt(5, terrain.getId_terrain());
        ps.executeUpdate();
    }

    @Override
    public List<Terrain> afficher_t() throws SQLException {
        List<Terrain> terrains = new ArrayList<>();
        String sql = "SELECT * FROM terrain";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            terrains.add(new Terrain(rs.getInt("id_terrain"), rs.getString("nom"), rs.getString("lieu"), rs.getString("des"),rs.getString("img")));
        }


        return terrains;
    }

    public void reserverTerrain(int idTerrain, int idUser, String dateReservation) throws SQLException {
        String sql = "INSERT INTO reservation (date_res, id_user, id_terrain, nomTerrain) VALUES (?, ?, ?, ?)"; // Ajouter nom_terrain

        // Récupérer le nom du terrain en utilisant le service terrain
        Terrain terrain = getTerrainById(idTerrain); // Méthode pour récupérer le terrain par son ID
        if (terrain == null) {
            throw new SQLException("Terrain avec ID " + idTerrain + " non trouvé.");
        }
        String nomTerrain = terrain.getNom(); // Assurez-vous que vous avez bien le nom du terrain

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dateReservation);
            stmt.setInt(2, idUser);
            stmt.setInt(3, idTerrain);
            stmt.setString(4, nomTerrain); // Ajouter le nom du terrain à la requête
            stmt.executeUpdate();
        }
    }

    public Terrain getTerrainById(int idTerrain) {
        Terrain terrain = null;
        String query = "SELECT * FROM terrain WHERE id_terrain = ?";  // Requête pour récupérer le terrain par ID

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, idTerrain);

            try (ResultSet rs = stmt.executeQuery()) {
                // Si un terrain est trouvé
                if (rs.next()) {
                    // Créer l'objet Terrain à partir des données récupérées
                    terrain = new Terrain();
                    terrain.setId_terrain(rs.getInt("id_terrain"));
                    terrain.setNom(rs.getString("nom"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return terrain;
    }


}
