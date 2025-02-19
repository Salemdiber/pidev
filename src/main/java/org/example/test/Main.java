package org.example.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.utils.DataBase;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inscription.fxml"));
        Parent root = loader.load();

        // Set the scene and show the stage
        primaryStage.setTitle("Inscription");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Establish database connection
        try (Connection connection = DataBase.getConnection()) {
            if (connection != null) {
                System.out.println("Connexion à la base de données réussie !");
            } else {
                System.out.println("Échec de la connexion !");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données: " + e.getMessage());
            return;
        }

        // Launch the JavaFX application
        launch(args);
    }
}
