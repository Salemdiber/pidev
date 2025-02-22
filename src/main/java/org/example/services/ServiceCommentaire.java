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

    public void ajouter_t(Commentaire commentaire) throws SQLException {
        Connection conn = MyDataBase.getInstance().getConnection();  // Utilisation de la connexion valide
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            throw new SQLException("La connexion à la base de données est fermée.");
        }

        String sql = "INSERT INTO commentaire (`desc_com`, id_user, id_pub) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, commentaire.getDesc());
            ps.setInt(2, commentaire.getIdUser());
            ps.setInt(3, commentaire.getIdPub());
            ps.executeUpdate(); // Exécution de la requête SQL
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            throw new SQLException("Impossible d'ajouter le commentaire : " + e.getMessage(), e);
        }
    }



    @Override
    public void modifier_t(Commentaire commentaire) throws SQLException {
        String sql = "UPDATE commentaire SET id_pub = ?, desc_com = ?, id_user = ? WHERE id_com = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, commentaire.getIdPub());
            ps.setString(2, commentaire.getDesc());
            ps.setInt(3, commentaire.getIdUser());
            ps.setInt(4, commentaire.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la modification du commentaire : " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer_t(int id) throws SQLException {
        String sql = "DELETE FROM commentaire WHERE id_com = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la suppression du commentaire : " + e.getMessage(), e);
        }
    }

    public List<Commentaire> afficher_t() throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        Connection conn = MyDataBase.getInstance().getConnection();  // Assurez-vous que la connexion est valide
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            throw new SQLException("La connexion à la base de données est fermée.");
        }

        String sql = "SELECT c.id_com, c.desc_com, c.id_user, c.id_pub, u.nom " +
                "FROM commentaire c " +
                "JOIN user u ON c.id_user = u.id_user";

        try (Statement statement = conn.createStatement();
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
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des commentaires : " + e.getMessage());
            throw new SQLException("Impossible de charger les commentaires : " + e.getMessage(), e);
        }

        return commentaires;
    }


    // Méthodes inutilisées : vous pouvez les supprimer si elles ne sont pas nécessaires
    @Override
    public int modifier(Commentaire commentaire, int id) throws SQLException {
        return 0; // Si la méthode n'est pas utilisée, il est préférable de la supprimer ou de l'implémenter si nécessaire
    }

    @Override
    public boolean ajouteru(Commentaire commentaire) throws SQLException {
        return false; // Cette méthode semble ne pas être utilisée, à supprimer si non nécessaire
    }

    @Override
    public boolean modifieru(Commentaire commentaire) throws SQLException {
        return false; // Cette méthode semble ne pas être utilisée, à supprimer si non nécessaire
    }

    @Override
    public boolean supprimeru(int id) throws SQLException {
        return false; // Cette méthode semble ne pas être utilisée, à supprimer si non nécessaire
    }

    @Override
    public void ajouterEquipeAMatch(int idMatch, int idEquipe) throws SQLException {
        // Implémentation vide, à supprimer si non utilisée
    }

    @Override
    public void supprimerEquipeDuMatch(int idMatch, int idEquipe) throws SQLException {
        // Implémentation vide, à supprimer si non utilisée
    }

    @Override
    public List<Integer> getEquipesParMatch(int idMatch) throws SQLException {
        return List.of(); // Implémentation vide, à supprimer si non utilisée
    }

    @Override
    public List<Integer> getMatchsParEquipe(int idEquipe) throws SQLException {
        return List.of(); // Implémentation vide, à supprimer si non utilisée
    }
}
