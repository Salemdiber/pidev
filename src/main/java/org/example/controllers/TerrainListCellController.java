package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Terrain;

import java.io.File;
import java.io.IOException;

public class TerrainListCellController extends ListCell<Terrain> {

    private final HBox content;
    private final ImageView imageView;
    private final Label nomLabel;
    private final Label lieuLabel;
    private final Label descriptionLabel;

    public TerrainListCellController() {
        super();


        imageView = new ImageView();
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        nomLabel = new Label();
        nomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        lieuLabel = new Label();

        descriptionLabel = new Label();
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(300);


        VBox textContainer = new VBox(nomLabel, lieuLabel, descriptionLabel);
        textContainer.setSpacing(5);


        content = new HBox(imageView, textContainer);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Terrain terrain, boolean empty) {
        super.updateItem(terrain, empty);
        if (empty || terrain == null) {
            setGraphic(null);
        } else {

            nomLabel.setText("Nom : " + terrain.getNom());
            lieuLabel.setText("Lieu : " + terrain.getLieu());
            descriptionLabel.setText("Description : " + terrain.getDes());


            String imagePath = terrain.getImg();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                } else {
                    System.err.println("❌ Image introuvable : " + imagePath);
                    imageView.setImage(null);
                }
            }

            setGraphic(content);
        }
    }
    @FXML
    private void handleReturnButtonClick(ActionEvent event) {
        try {

            Parent homePage = FXMLLoader.load(getClass().getResource("/view/homeAffiche.fxml"));
            Scene homeScene = new Scene(homePage);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
