package tn.esprit.services;
import tn.esprit.entities.Equipe;
import tn.esprit.entities.Partie;
import tn.esprit.entities.TypeMatch;
import tn.esprit.utils.Mydatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceMatch implements Iservice<Partie> {

    private Connection cnx;

    public ServiceMatch() {
        cnx = Mydatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Partie match) throws SQLException {
        String req = "INSERT INTO partie (type, resultat, date_match, lieu) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            // En utilisant l'enum TypeMatch et la méthode toString()
            pst.setString(1, match.getType().toString());
            pst.setString(2, match.getResultat());
            pst.setDate(3, new java.sql.Date(match.getDateMatch().getTime()));
            pst.setString(4, match.getLieu());
            pst.executeUpdate();

            // Récupérer l'ID généré pour le match
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                match.setIdMatch(rs.getInt(1));
            }

            System.out.println("✅ Match ajouté avec succès !");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM partie WHERE id_match = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Match supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun match trouvé avec cet ID.");
            }
        }
    }

    @Override
    public void modifier(Partie match) throws SQLException {
        String req = "UPDATE partie SET type=?, resultat=?, date_match=?, lieu=? WHERE id_match=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            // En utilisant l'enum TypeMatch et la méthode toString()
            pst.setString(1, match.getType().toString());
            pst.setString(2, match.getResultat());
            pst.setDate(3, new java.sql.Date(match.getDateMatch().getTime()));
            pst.setString(4, match.getLieu());
            pst.setInt(5, match.getIdMatch());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Match mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun match trouvé avec cet ID.");
            }
        }
    }

    @Override
    public List<Partie> afficher() throws SQLException {
        List<Partie> matchs = new ArrayList<>();
        String req = "SELECT * FROM partie";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                // Mapper l'enum TypeMatch à partir de la base de données
                TypeMatch type = TypeMatch.valueOf(rs.getString("type").toUpperCase());
                Partie match = new Partie(
                        rs.getInt("id_match"),
                        TypeMatch.valueOf(rs.getString("type").toUpperCase()),  // Utilisation de l'enum TypeMatch
                        rs.getString("resultat"),
                        rs.getDate("date_match"),
                        rs.getString("lieu")
                );
                matchs.add(match);
            }
        }
        return matchs;
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

}