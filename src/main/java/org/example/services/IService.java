package org.example.services;
import java.sql.SQLException;
import java.util.List;

public interface IService <T> {
    int ajouter(T t) throws SQLException;
    int modifier(T t,int id)throws SQLException;
    int supprimer(int id)throws SQLException;
    List<T> afficher() throws SQLException;
}
