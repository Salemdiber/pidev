package org.example.services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter_t(T t) throws SQLException;
    void supprimer_t(int id) throws SQLException;
    void modifier_t(T t) throws SQLException;
    List<T> afficher_t() throws SQLException;
}
