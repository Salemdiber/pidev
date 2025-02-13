package org.example.test;

import org.example.entities.PacMan;
import org.example.entities.MiniGame;
import org.example.services.ServiceMiniGame;
import javax.swing.JFrame;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws SQLException {
        List<MiniGame> miniGames = new ArrayList<>();
        ServiceMiniGame s = new ServiceMiniGame();
        JFrame frame =new JFrame("MiniGame_PacMan");
        //frame.setVisible(true);
        //frame.setSize(row*tileSize,col*tileSize);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);

        /*try{
            MiniGame miniGame = new MiniGame(3,1333,1,"winner",null);
            s.ajouter(pacmanGame);
            //s.modifier(miniGame,10);
           // s.supprimer(1);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }*/
    }
}