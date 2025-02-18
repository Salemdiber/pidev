package org.example.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/gameHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1450, 720);
            primaryStage.setTitle("SportNexus");
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


