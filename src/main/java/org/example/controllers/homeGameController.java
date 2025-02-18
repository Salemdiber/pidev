package org.example.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.entities.FlappyBall;
import org.example.entities.PacMan;

import javax.swing.*;

public class homeGameController {
    @FXML
    private Button btnHistorique;
    @FXML
    private Button btnCarte;
    public void LancerPacMan(ActionEvent actionEvent) {
        JFrame frame =new JFrame("MiniGame_PacMan");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }

    public void LancerFlappy(ActionEvent actionEvent) {
        JFrame frame =new JFrame("MiniGame_FlappyBall");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FlappyBall fb = new FlappyBall();
        frame.add(fb);
        frame.pack();
        fb.requestFocus();
        frame.setVisible(true);
    }


    public void versAfficherHistorique(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/afficherGames.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1450, 720);
            Stage primaryStage = (Stage) btnHistorique.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void versAfficherCartes(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AfficherCartes.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1450, 720);

            Stage primaryStage = (Stage) btnCarte.getScene().getWindow();

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
