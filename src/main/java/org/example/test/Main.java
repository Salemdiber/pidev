package org.example.test;
import javafx.scene.image.Image;
import org.example.entities.Reservation;
import org.example.services.ServiceReservation;
import org.example.services.ServiceTerrain;
import org.example.entities.Terrain;
import org.example.utils.MyDataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HomeAffiche.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1450, 720);
            primaryStage.setTitle("Gestion des Terrains");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


