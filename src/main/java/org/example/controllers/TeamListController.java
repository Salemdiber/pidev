package org.example.controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import org.example.entities.Equipe;
import org.example.services.ServiceEquipe;

import java.io.File;
import java.io.IOException;


public class TeamListController extends ListCell<Equipe> {

    private final HBox content;
    private final ImageView imageView;
    private final Label nomLabel;
    private final Label pointsLabel;
    private final Button detailsButton;
    @javafx.fxml.FXML
    private ListView<Equipe> listViewTeams;

    public TeamListController() {
        super();


        imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);


        nomLabel = new Label();
        nomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        pointsLabel = new Label();
        pointsLabel.setStyle("-fx-font-size: 16px;");


        detailsButton = new Button("View Details");
        detailsButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px;");
        detailsButton.setOnAction(event -> {
            Equipe equipe = getItem();
            if (equipe != null) {
                afficherDetailsEquipe(equipe);
            }
        });


        VBox textContainer = new VBox(nomLabel, pointsLabel);
        textContainer.setSpacing(10);
        textContainer.setAlignment(Pos.CENTER_LEFT);


        content = new HBox(15, imageView, textContainer, detailsButton);
        content.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Equipe equipe, boolean empty) {
        super.updateItem(equipe, empty);
        if (empty || equipe == null) {
            setGraphic(null);
        } else {

            nomLabel.setText("Nom : " + equipe.getNom());
            pointsLabel.setText("Points : " + equipe.getPoints());


            String imagePath = equipe.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                } else {
                    System.err.println("Image introuvable : " + imagePath);
                    imageView.setImage(null);
                }
            }

            setGraphic(content);
        }
    }


    private void afficherDetailsEquipe(Equipe equipe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsTeam.fxml"));
            Parent root = loader.load();
            DetailTeamController controller = loader.getController();
            controller.setTeams(equipe);
            controller.setTeamListController(this);
            Stage stage = new Stage();
            stage.setTitle("details Team");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AddTeam.fxml");
        }
    }
    public Equipe rafraichirAffichage(int id) {
        ServiceEquipe serviceEquipe = new ServiceEquipe();
        updateItem(serviceEquipe.getOne(id),false);
        return serviceEquipe.getOne(id);
    }

}
