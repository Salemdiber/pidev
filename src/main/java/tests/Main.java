package tests;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import entities.Participant;
import services.ServiceEvent;
import services.ServiceParticipant;

import java.sql.SQLException;

public  class Main {



    public static void main(String[] args) {
        new ServiceEvent();
        ServiceParticipant sp = new ServiceParticipant();

        try {
            sp.ajouter(new Participant(1,1));
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }
}
