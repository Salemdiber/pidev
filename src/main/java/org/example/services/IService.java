package org.example.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IService <T> {

    public boolean ajouter(T t)throws SQLException;

    public boolean modifier(T t)throws SQLException;

    public boolean supprimer(int id) throws SQLException ;
    public List<T> afficher()throws SQLException;

}
