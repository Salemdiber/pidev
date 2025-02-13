package org.example.services;

import java.sql.SQLException;
import java.util.List;

public interface IService <I>{

    void ajouter(I i)throws SQLException;
    void modifier(I i)throws SQLException;
    void supprimer(int id)throws SQLException;
    List<I> afficher()throws SQLException;

}
