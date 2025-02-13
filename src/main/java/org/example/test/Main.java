package org.example.test;

import org.example.entities.event;
import org.example.entities.participant;
import org.example.services.ServiceEvent;
import org.example.services.ServiceParticipant;
import org.example.utils.MyDataBase;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        ServiceEvent se = new ServiceEvent();
        ServiceParticipant sp = new ServiceParticipant();
        try {
            //LocalDateTime dateEvent = LocalDateTime.of(2025, 5, 10, 15, 30);
            //se.ajouter(new event("tournois","aa","super", "sokra",dateEvent));
            //event eventToUpdate = new event("Tournoi Modifié", "new_image_url", "Nouvelle description", "Paris", LocalDateTime.of(2025, 6, 15, 18, 0));
            //eventToUpdate.setId_event(1);
            //se.modifier(eventToUpdate);
            /*List<event> events = se.afficher();
            for (event e : events) {
                System.out.println(e);
            }*/
            //se.supprimer(1);
            //sp.ajouter(new participant(1,1));
            //sp.modifier(new participant(1,2,1));
            //System.out.println(sp.afficher());
            sp.supprimer(1);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        }

}