package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<I> {
        void ajouter(I var1) throws SQLException;

        void modifier(I var1) throws SQLException;

        void supprimer(int var1) throws SQLException;

        List<I> afficher() throws SQLException;
    }


