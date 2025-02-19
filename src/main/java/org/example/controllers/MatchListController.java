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
    private final HBox content; // Conteneur principal pour la cellule
    private final Label typeLabel; // Pour afficher le nom de l'équipe
    private final Label resultatLabel; // Pour afficher les points de l'équipe
    private final Button detailsButton; // Bouton "View Details"
    @javafx.fxml.FXML
    private ListView<Partie> listViewMatchs;

    public MatchListController() {
        super();
        // Initialisation des labels
        typeLabel = new Label();
        typeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Taille et style du texte
        resultatLabel = new Label();
        resultatLabel.setStyle("-fx-font-size: 16px;"); // Taille du texte

        // Bouton "View Details"
        detailsButton = new Button("View Details");
        detailsButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px;");
        detailsButton.setOnAction(event -> {
            Partie partie = getItem();
            if (partie != null) {
                afficherDetailsMatch(partie);
            }
        });

        // Organisation des labels dans un VBox
        VBox textContainer = new VBox(typeLabel, resultatLabel);
        textContainer.setSpacing(10);
        textContainer.setAlignment(Pos.CENTER_LEFT); // Alignement des labels au centre verticalement

        // HBox contenant image + textes + bouton
        content = new HBox(15, textContainer, detailsButton);
        content.setAlignment(Pos.CENTER_LEFT); // Alignement des éléments au centre verticalement
    }

    @Override
    protected void updateItem(Partie partie, boolean empty) {
        super.updateItem(partie, empty);
        if (empty || partie == null) {
            setGraphic(null);
        } else {
            // Display the teams instead of the match type
            String teamNames = "";
            List<Equipe> equipes = partie.getEquipes();
            if (equipes.size() >= 2) {
                teamNames = equipes.get(0).getNom() + " vs " + equipes.get(1).getNom();  // Assuming 'getNom()' is the method to get the team name
            }
            typeLabel.setText("Teams: " + teamNames);  // Display the teams in the type label

            // Display the match type in place of the team names
            resultatLabel.setText("Resultat: " + partie.getResultat() + " \nType: " + partie.getType());  // Display the type in the result label

            setGraphic(content);
        }
    }


    // Méthode pour afficher les détails de l'équipe
    private void afficherDetailsMatch(Partie partie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsMatch.fxml"));
            Parent root = loader.load();


            DetailsMatchController controller = loader.getController();
            controller.setMatch(partie);
            controller.setSelectedPartie(partie);
            controller.setMatchListController(this);

            // Créer une nouvelle fenêtre pour AjouterTerrain
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