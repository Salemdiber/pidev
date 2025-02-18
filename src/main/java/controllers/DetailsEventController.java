package controllers;

import entities.Participant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import entities.Event;
import services.ServiceEvent;
import services.ServiceParticipant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class DetailsEventController {
    @FXML
    private Label nomLabelDetail;
    @FXML
    private Label lieuLabelDetail;
    @FXML
    private Label desLabelDetail;
    @FXML
    private Label dateLabelDetail;

    private Event event;
    private final ServiceParticipant serviceParticipant=new ServiceParticipant();
    private final ServiceEvent serviceevent = new ServiceEvent();
    private AfficherEventController affichereventcontroller ;


    @FXML
    private Button btnsupp;
    @FXML
    private Pane mainPageContainer;
    @FXML
    private ImageView btnsupp1;
    @FXML
    private ImageView ImageViewDetail;
    private AfficherEventController afficherEventController;
    @FXML
    private Button btnmodifier;
    @FXML
    private ImageView btnmodifier1;
    @FXML
    private Button btnpart;

    @FXML
    public void initialize() {
        btnmodifier.setOnAction(event -> modifierEvent());
        btnsupp.setOnAction(event->supprimerEvent());
        affichereventcontroller = new AfficherEventController();
    }



    public void setEvent(Event event) {
        this.event = event;
        System.out.println("✅ Détails mis à jour pour l'événement ID : " + event.getId_event());

        if (event != null) {
            nomLabelDetail.setText(event.getNom());
            lieuLabelDetail.setText(event.getLieu());
            desLabelDetail.setText(event.getDesc());
            dateLabelDetail.setText(event.getDate().toString());

            // Charger l'image
            if (event.getImage() != null && !event.getImage().isEmpty()) {
                Image image = new Image("file:" + event.getImage());
                ImageViewDetail.setImage(image);
            }
        }
    }

    @FXML
    private void Participer() {
        if (event == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Aucun event sélectionné.");
            return;
        }

        int idEvent = event.getId_event();
        int idUser = getCurrentUserId();

        try {
            serviceevent.participer(idUser,idEvent);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "event participé avec succès !");
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de participer : " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private int getCurrentUserId() {
        return 1;
    }


    // Méthode de suppression mise à jour
    @FXML
    private void supprimerEvent() {
        if (event == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun événement sélectionné !");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer cet événement ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceevent.supprimer(event.getId_event());

                if (afficherEventController != null) {
                    afficherEventController.rafraichirAffichage();  // ✅ Rafraîchir la liste après suppression
                } else {
                    System.out.println("⚠️ afficherEventController est NULL, impossible de rafraîchir !");
                }

                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé avec succès !");

                // Fermer la fenêtre après suppression
                Stage stage = (Stage) btnsupp.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer l'événement : " + e.getMessage());
            }
        }
    }



    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setAfficherEventController(AfficherEventController controller) {
        if (controller != null) {
            this.afficherEventController = controller;
        } else {
            System.out.println("⚠️ ERREUR : afficherEventController reçu est NULL !");
        }
    }

    @FXML
    private void modifierEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifierEvent.fxml"));
            Parent root = loader.load();

            ModifierEventController controller = loader.getController();
            controller.setDetailsEventController(this);
            controller.setEvent(event); // 🔹 Passe l'événement à modifier
            controller.setAfficherEventController(afficherEventController); // ✅ Pour rafraîchir après modification

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'Événement");
            stage.setResizable(false);

            // 🔹 Rafraîchir après fermeture de la fenêtre de modification
            stage.setOnHidden(e -> {
                if (afficherEventController != null) {
                    afficherEventController.rafraichirAffichage();
                }
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherListeParticipants() {
        if (event == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Aucun événement sélectionné.");
            return;
        }

        System.out.println("🔹 ID de l'événement envoyé : " + event.getId_event()); // Debug

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/listeParticipants.fxml"));
            Parent root = loader.load();

            listeParticipantControlle controller = loader.getController();
            controller.setEvent(event); // Passer l'événement sélectionné

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Participants de " + event.getNom());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible d'afficher la liste des participants.");
        }
    }








}
