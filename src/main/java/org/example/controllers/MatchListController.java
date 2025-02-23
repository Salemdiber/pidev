package org.example.controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import org.example.entities.Equipe;
import org.example.entities.Partie;
import org.example.services.ServiceMatch;

import java.io.IOException;
import java.util.List;

public class MatchListController extends ListCell<Partie> {
    private final HBox content;
    private final Label typeLabel;
    private final Label resultatLabel;
    private final Button detailsButton;
    @javafx.fxml.FXML
    private ListView<Partie> listViewMatchs;

    public MatchListController() {
        super();

        typeLabel = new Label();
        typeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        resultatLabel = new Label();
        resultatLabel.setStyle("-fx-font-size: 16px;");


        detailsButton = new Button("View Details");
        detailsButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px;");
        detailsButton.setOnAction(event -> {
            Partie partie = getItem();
            if (partie != null) {
                afficherDetailsMatch(partie);
            }
        });


        VBox textContainer = new VBox(typeLabel, resultatLabel);
        textContainer.setSpacing(10);
        textContainer.setAlignment(Pos.CENTER_LEFT);


        content = new HBox(15, textContainer, detailsButton);
        content.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Partie partie, boolean empty) {
        super.updateItem(partie, empty);
        if (empty || partie == null) {
            setGraphic(null);
        } else {

            String teamNames = "";
            List<Equipe> equipes = partie.getEquipes();
            if (equipes.size() >= 2) {
                teamNames = equipes.get(0).getNom() + " vs " + equipes.get(1).getNom();
            }
            typeLabel.setText("Teams: " + teamNames);


            resultatLabel.setText("Resultat: " + partie.getResultat() + " \nType: " + partie.getType());

            setGraphic(content);
        }
    }



    private void afficherDetailsMatch(Partie partie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsMatch.fxml"));
            Parent root = loader.load();


            DetailsMatchController controller = loader.getController();
            controller.setMatch(partie);
            controller.setSelectedPartie(partie);
            controller.setMatchListController(this);


            Stage stage = new Stage();
            stage.setTitle("details Match");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AddTeam.fxml");
        }
    }

    public Partie rafraichirAffichage(int id) {
        ServiceMatch serviceMatch = new ServiceMatch();
        updateItem(serviceMatch.getOne(id),false);
        return serviceMatch.getOne(id);
    }
}