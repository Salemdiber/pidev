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
        String sql = "INSERT INTO terrain (nom, lieu, des, img) VALUES (?, ?, ?, ?)";
        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, terrain.getNom());
            ps.setString(2, terrain.getLieu());
            ps.setString(3, terrain.getDes());
            ps.setString(4, terrain.getImg());
            ps.executeUpdate();
        }
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
        try (Connection conn = MyDataBase.getInstance().getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                terrains.add(new Terrain(
                        rs.getInt("id_terrain"),
                        rs.getString("nom"),
                        rs.getString("lieu"),
                        rs.getString("des"),
                        rs.getString("img")
                ));
            }
        }
        return terrains;
    }


    public void reserverTerrain(int idTerrain, int idUser, String dateReservation) throws SQLException {
        String sql = "INSERT INTO reservation (date_res, id_user, id_terrain, nomTerrain) VALUES (?, ?, ?, ?)";

        Terrain terrain = getTerrainById(idTerrain);
        if (terrain == null) {
            throw new SQLException("Terrain avec ID " + idTerrain + " non trouvé.");
        }

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateReservation);
            stmt.setInt(2, idUser);
            stmt.setInt(3, idTerrain);
            stmt.setString(4, terrain.getNom());
            stmt.executeUpdate();
        }
    }


    public Terrain getTerrainById(int idTerrain) throws SQLException {
        Terrain terrain = null;
        String query = "SELECT * FROM terrain WHERE id_terrain = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTerrain);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    terrain = new Terrain(
                            rs.getInt("id_terrain"),
                            rs.getString("nom"),
                            rs.getString("lieu"),
                            rs.getString("des"),
                            rs.getString("img")
                    );
                }
            }
        }
        return terrain;
    }

    public List<String> getAllTerrainNames() throws SQLException {
        List<String> terrainNames = new ArrayList<>();
        String query = "SELECT nom FROM terrain";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                terrainNames.add(resultSet.getString("nom"));
            }
        }
        return terrainNames;
    }


    public boolean isTerrainAvailable(int idTerrain, String dateHeureReservation) throws SQLException {
        String query = "SELECT COUNT(*) FROM reservation WHERE id_terrain = ? AND date_res = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idTerrain);
            preparedStatement.setString(2, dateHeureReservation);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        }
        return false;
    }
    public int getTerrainIdByName(String nomTerrain) throws SQLException {
        String sql = "SELECT id_terrain FROM terrain WHERE nom = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomTerrain);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_terrain");
            } else {
                throw new SQLException("Aucun terrain trouvé avec le nom : " + nomTerrain);
            }
        }
    }

    @Override
    public int modifier(Terrain terrain, int id) throws SQLException {
        return 0;
    }

    @Override
    public boolean ajouteru(Terrain terrain) throws SQLException {
        return false;
    }

    @Override
    public boolean modifieru(Terrain terrain) throws SQLException {
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
