package org.example.services;
import org.example.entities.Equipe;
import org.example.utils.MyDB;
import org.example.utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceEquipe implements IService<Equipe> {

    private final Connection cnx;

    public ServiceEquipe() {
        cnx = MyDB.getInstance().getCon();
    }

    @Override
    public void ajouter_t(Equipe equipe) throws SQLException {
        String req = "INSERT INTO equipe (nom_equipe, classement, image, description, points, id_entraineur) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, equipe.getNom());
            pst.setInt(2, equipe.getClassement());
            pst.setString(3, equipe.getImage());
            pst.setString(4, equipe.getDescription());
            pst.setInt(5, equipe.getPoints());
            pst.setInt(6, equipe.getIdEntraineur());

            pst.executeUpdate();
            System.out.println("✅ Équipe ajoutée avec succès !");
        }
    }

    @Override
    public void supprimer_t(int id) throws SQLException {
        String req = "DELETE FROM equipe WHERE id_equipe = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Équipe supprimée avec succès !");
            } else {
                System.out.println("⚠️ Aucune équipe trouvée avec cet ID.");
            }
        }
    }

    @Override
    public void modifier_t(Equipe equipe) throws SQLException {
        String req = "UPDATE equipe SET nom_equipe=?, classement=?, image=?, description=?, points=?, id_entraineur=? WHERE id_equipe=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, equipe.getNom());
            pst.setInt(2, equipe.getClassement());
            pst.setString(3, equipe.getImage());
            pst.setString(4, equipe.getDescription());
            pst.setInt(5, equipe.getPoints());
            pst.setInt(6, equipe.getIdEntraineur());
            pst.setInt(7, equipe.getIdEquipe());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Équipe mise à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucune équipe trouvée avec cet ID.");
            }
        }
    }

    @Override
    public List<Equipe> afficher_t() throws SQLException {
        List<Equipe> equipes = new ArrayList<>();
        String req = "SELECT * FROM equipe";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Equipe equipe = new Equipe(
                        rs.getInt("id_equipe"),
                        rs.getString("nom_equipe"),
                        rs.getInt("classement"),
                        rs.getString("image"),
                        rs.getString("description"),
                        rs.getInt("points")
                );
                equipes.add(equipe);
            }
        }
        return equipes;
    }

    @Override
    public int modifier(Equipe equipe, int id) throws SQLException {
        return 0;
    }

    @Override
    public void ajouterEquipeAMatch(int idMatch, int idEquipe) throws SQLException {
        String req = "INSERT INTO partie_equipe (id_match, id_equipe) VALUES (?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idMatch);
            pst.setInt(2, idEquipe);
            pst.executeUpdate();
            System.out.println("✅ Équipe ajoutée au match avec succès !");
        }
    }

    @Override
    public void supprimerEquipeDuMatch(int idMatch, int idEquipe) throws SQLException {
        String req = "DELETE FROM partie_equipe WHERE id_match = ? AND id_equipe = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idMatch);
            pst.setInt(2, idEquipe);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Équipe retirée du match avec succès !");
            } else {
                System.out.println("⚠️ Aucun enregistrement trouvé.");
            }
        }
    }

    @Override
    public List<Integer> getMatchsParEquipe(int idEquipe) throws SQLException {
        List<Integer> matchs = new ArrayList<>();
        String req = "SELECT id_match FROM partie_equipe WHERE id_equipe = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idEquipe);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                matchs.add(rs.getInt("id_match"));
            }
        }
        return matchs;
    }

    @Override
    public List<Integer> getEquipesParMatch(int idMatch) throws SQLException {
        List<Integer> equipes = new ArrayList<>();
        String req = "SELECT id_equipe FROM partie_equipe WHERE id_match = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idMatch);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                equipes.add(rs.getInt("id_equipe"));
            }
        }
        return equipes;
    }
    public Equipe getOne(int id){
        String query = "SELECT * FROM equipe WHERE id_equipe = ?";
        Equipe equipe = new Equipe();
        try{
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1,id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                 equipe = new Equipe(
                        rs.getInt("id_equipe"),
                        rs.getString("nom_equipe"),
                        rs.getInt("classement"),
                        rs.getString("image"),
                        rs.getString("description"),
                        rs.getInt("points")
                );



            }
            stm.close();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return equipe;
    }

}