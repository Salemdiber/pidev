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

    private final HBox content; // Conteneur principal pour la cellule
    private final ImageView imageView; // Pour afficher l'image de l'équipe
    private final Label nomLabel; // Pour afficher le nom de l'équipe
    private final Label pointsLabel; // Pour afficher les points de l'équipe
    private final Button detailsButton; // Bouton "View Details"
    @javafx.fxml.FXML
    private ListView<Equipe> listViewTeams;

    public TeamListController() {
        super();

        // Initialisation de l'image
        imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        // Initialisation des labels
        nomLabel = new Label();
        nomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Taille et style du texte
        pointsLabel = new Label();
        pointsLabel.setStyle("-fx-font-size: 16px;"); // Taille du texte

        // Bouton "View Details"
        detailsButton = new Button("View Details");
        detailsButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px;");
        detailsButton.setOnAction(event -> {
            Equipe equipe = getItem();
            if (equipe != null) {
                afficherDetailsEquipe(equipe);
            }
        });

        // Organisation des labels dans un VBox
        VBox textContainer = new VBox(nomLabel, pointsLabel);
        textContainer.setSpacing(10);
        textContainer.setAlignment(Pos.CENTER_LEFT); // Alignement des labels au centre verticalement

        // HBox contenant image + textes + bouton
        content = new HBox(15, imageView, textContainer, detailsButton);
        content.setAlignment(Pos.CENTER_LEFT); // Alignement des éléments au centre verticalement
    }

    @Override
    protected void updateItem(Equipe equipe, boolean empty) {
        super.updateItem(equipe, empty);
        if (empty || equipe == null) {
            setGraphic(null);
        } else {
            // Mettre à jour le texte des labels
            nomLabel.setText("Nom : " + equipe.getNom());
            pointsLabel.setText("Points : " + equipe.getPoints());

            // Vérifier et charger l'image
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

    // Méthode pour afficher les détails de l'équipe
    private void afficherDetailsEquipe(Equipe equipe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsTeam.fxml"));
            Parent root = loader.load();


            DetailTeamController controller = loader.getController();
            controller.setTeams(equipe);
            controller.setTeamListController(this);
            // Créer une nouvelle fenêtre pour AjouterTerrain
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
