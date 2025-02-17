package test;

import entities.Commentaire;
import entities.Publication;
import services.ServiceCommentaire;
import services.ServicePublication;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        //ServicePublication pub = new ServicePublication();
        ServiceCommentaire comm = new ServiceCommentaire();

        try {

            //pub.ajouter(new Publication("Match parfait", "Match wawwww", 1));
            //Publication pub1 = new Publication("mahmoud","maah",1);
            //pub1.setIdPub(8);
            //pub.modifier(pub1);
            //pub.supprimer(8);

            //System.out.println("Publication added successfully.");
            //Commentaire com= new Commentaire("wouah",1,9);
            //comm.ajouter(com);
            //com2.setId(1);
            Commentaire com = new Commentaire("ouah", 1, 9);
            com.setId(1);
            //comm.modifier(com);

            comm.supprimer(1);
            //System.out.println(com);
           // System.out.println(pub.afficher());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}