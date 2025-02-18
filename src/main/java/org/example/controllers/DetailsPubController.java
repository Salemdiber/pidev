package org.example.controllers;

import org.example.entities.Commentaire;
import org.example.entities.Publication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.services.ServiceCommentaire;
import org.example.services.ServicePublication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DetailsPubController {

    @FXML
    private Label nomLabelDetail; // User's name
    @FXML
    private Label titreLabelDetail; // Publication title
    @FXML
    private Label desLabelDetail; // Publication description
    @FXML
    private Button btnsupp;
    @FXML
    private Button btnmodifier;
    @FXML
    private Button ajoutcom;
    @FXML
    private Pane mainPageContainer;
    @FXML
    private ListView<String> listcom; // ListView to display comments with names

    private Publication publication;
    private final ServicePublication servicePublication = new ServicePublication();
    private final ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private AfficherPubController afficherPubController;

    @FXML
    public void initialize() {
        btnmodifier.setOnAction(event -> ouvrirFenetreModification());
        btnsupp.setOnAction(event -> supprimerPublication());
        ajoutcom.setOnAction(event -> ouvrirAjoutCommentaire());

        listcom.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click event
                String selectedComment = listcom.getSelectionModel().getSelectedItem();
                if (selectedComment != null) {
                    ouvrirDetailsCommentaire(selectedComment);
                }
            }
        });

        if (publication != null) {
            chargerCommentaires();
        }
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
        if (publication != null) {
            nomLabelDetail.setText( publication.getNom()); // Show user’s name
            titreLabelDetail.setText(publication.getTitre()); // Show title
            desLabelDetail.setText(publication.getDescrib()); // Show description
            chargerCommentaires();
        }
    }

    public void setAfficherPubController(AfficherPubController controller) {
        this.afficherPubController = controller;
    }

    /**
     * Loads comments for the selected publication, including user names.
     */
    public void chargerCommentaires() {
        if (publication == null) return;

        try {
            List<Commentaire> commentaires = serviceCommentaire.afficher_t();
            ObservableList<String> commentList = FXCollections.observableArrayList();

            for (Commentaire commentaire : commentaires) {
                if (commentaire.getIdPub() == publication.getIdPub()) {
                    commentList.add(commentaire.getNom() + " : " + commentaire.getDesc()); // Show name and comment
                }
            }

            listcom.setItems(commentList);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les commentaires : " + e.getMessage());
        }
    }

    /**
     * Opens the comment addition window.
     */
    private void ouvrirAjoutCommentaire() {
        if (publication == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune publication sélectionnée !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjoutCom.fxml"));
            Parent root = loader.load();

            AjoutComController controller = loader.getController();
            controller.setIdPub(publication.getIdPub());
            controller.setDetailsPubController(this); // Pass reference to refresh comments

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Commentaire");
            stage.setScene(new Scene(root));

            stage.setOnHidden((WindowEvent event) -> chargerCommentaires());

            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre d'ajout de commentaire !");
            e.printStackTrace();
        }
    }

    /**
     * Opens the comment details window when a comment is double-clicked.
     */
    private void ouvrirDetailsCommentaire(String commentText) {
        if (publication == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune publication sélectionnée !");
            return;
        }

        try {
            List<Commentaire> commentaires = serviceCommentaire.afficher_t();
            Commentaire selectedCommentaire = null;

            for (Commentaire commentaire : commentaires) {
                if ((commentaire.getNom() + " : " + commentaire.getDesc()).equals(commentText)
                        && commentaire.getIdPub() == publication.getIdPub()) {
                    selectedCommentaire = commentaire;
                    break;
                }
            }

            if (selectedCommentaire == null) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de retrouver ce commentaire !");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsCom.fxml"));
            Parent root = loader.load();

            DetailsComController controller = loader.getController();
            controller.setCommentaire(selectedCommentaire);
            controller.setDetailsPubController(this);

            Stage stage = new Stage();
            stage.setTitle("Détails du Commentaire");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de détails du commentaire !");
            e.printStackTrace();
        }
    }

    private void supprimerPublication() {
        if (publication == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune publication sélectionnée !");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer cette publication ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                servicePublication.supprimer_t(publication.getIdPub());
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Publication supprimée avec succès !");

                if (afficherPubController != null) {
                    afficherPubController.rafraichirAffichage();
                }

                Stage stage = (Stage) btnsupp.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la publication : " + e.getMessage());
            }
        }
    }

    private void ouvrirFenetreModification() {
        if (publication == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune publication sélectionnée !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifierPub.fxml"));
            Parent root = loader.load();

            ModifierPubController controller = loader.getController();
            controller.setPublication(publication);
            controller.setDetailsPubController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier Publication");
            stage.setScene(new Scene(root));

            stage.setOnHidden((WindowEvent event) -> {
                if (afficherPubController != null) {
                    afficherPubController.rafraichirAffichage();
                }
            });

            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de modification !");
            e.printStackTrace();
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}