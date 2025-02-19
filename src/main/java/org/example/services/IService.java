package org.example.services;

import org.example.entities.Carte;
import org.example.entities.MiniGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface IService<T> {
    void ajouter_t(T t) throws SQLException;
    void supprimer_t(int id) throws SQLException;
    void modifier_t(T t) throws SQLException;
    List<T> afficher_t() throws SQLException;
    int modifier(T t,int id)throws SQLException;

    void ajouterEquipeAMatch(int idMatch, int idEquipe) throws SQLException;
    void supprimerEquipeDuMatch(int idMatch, int idEquipe) throws SQLException;
    List<Integer> getEquipesParMatch(int idMatch) throws SQLException;
    List<Integer> getMatchsParEquipe(int idEquipe) throws SQLException;

}

