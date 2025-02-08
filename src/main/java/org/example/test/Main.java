package org.example.test;
import org.example.services.ServiceTerrain;
import org.example.entities.Terrain;
import org.example.utils.MyDataBase;
import java.sql.SQLException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ServiceTerrain st = new ServiceTerrain();
        try{
          st.ajouter_t(new Terrain("Stade rades","ben arous","olympique","test img"));
            System.out.println("terrain ajouté");
            //st.modifier_t(new Terrain(1,"Camp nou","Barcelona","Barça","img"));
          //  System.out.println("terrain MODIFIED");
           //st.supprimer_t();
        //  System.out.println("Terrain supprimé");
            List<Terrain> terrains = st.afficher_t();
            System.out.println("\n📌 Liste des terrains enregistrés:");
            for (Terrain t : terrains) {
                System.out.println("🆔 ID: " + t.getId_terrain() +
                        " | 🏟 Nom: " + t.getNom() +
                        " | 📍 Lieu: " + t.getLieu() +
                        " | 📝 Description: " + t.getDes() +
                        " | 🖼 Image: " + t.getImg());
            }
            System.out.println(st.afficher_t());
        } catch (RuntimeException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}