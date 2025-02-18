package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.entities.Carte;
import org.example.services.ServiceCarte;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class afficherCartesController {

    @FXML
    private ListView<Carte> carteList;
    @FXML
    private Button back;
    private final ServiceCarte serviceCarte = new ServiceCarte();

    @FXML
    public void initialize() {
        loadCarte();
        carteList.setCellFactory(param -> new ListCell<Carte>() {
            @Override
            protected void updateItem(Carte carte, boolean empty) {
                super.updateItem(carte, empty);

                if (empty || carte == null) {
                    setGraphic(null);
                } else {
                    Label typeLabel = new Label("Rarité: " + carte.getRarite());
                    typeLabel.setFont(new Font("Arial", 14));
                    typeLabel.setTextFill(Color.BLACK);

                    Label scoreLabel = new Label("Score: " + carte.getScore());
                    scoreLabel.setFont(new Font("Arial", 14));
                    scoreLabel.setTextFill(Color.BLACK);

                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);

                    String imagePath = carte.getImage();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        File imageFile = new File(imagePath);
                        if (imageFile.exists()) {
                            Image image = new Image(imageFile.toURI().toString());
                            imageView.setImage(image);
                        }
                    }

                    HBox cardDetails = new HBox(15, imageView, new VBox(typeLabel, scoreLabel));
                    setGraphic(cardDetails);
                }
            }
        });
    }

    private void loadCarte() {
        try {
            List<Carte> cartes = serviceCarte.afficher(1);
            if (cartes.isEmpty()) {
                System.out.println("No data found.");
                return;
            }
            ObservableList<Carte> carteDataList = FXCollections.observableArrayList(cartes);
            carteList.setItems(carteDataList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void backPage(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/gameHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1450, 720);
            Stage primaryStage = (Stage) back.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
