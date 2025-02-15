package tn.esprit.services;
import java.sql.SQLException;
import java.util.List;

public interface Iservice<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(T t) throws SQLException;
    List<T> afficher() throws SQLException;

    // Gestion de la relation Many-to-Many entre Partie et Equipe
    void ajouterEquipeAMatch(int idMatch, int idEquipe) throws SQLException;
    void supprimerEquipeDuMatch(int idMatch, int idEquipe) throws SQLException;
    List<Integer> getEquipesParMatch(int idMatch) throws SQLException;
    List<Integer> getMatchsParEquipe(int idEquipe) throws SQLException;
}

