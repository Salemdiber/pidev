package controllers;

import entities.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import entities.Participant;
import services.ServiceParticipant;
import services.ServiceEvent;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import java.awt.event.ActionEvent;

public class listeParticipantControlle implements Initializable {

    @FXML
    private Button btnsupprimerParticipants;
    @FXML
    private Button btnreturn;
    @FXML
    private ImageView btnsupprimerParticipant1;
    @FXML
    private ListView<Participant> listViewParticipant;

    private final ServiceParticipant serviceparticipant = new ServiceParticipant();
    private final ServiceEvent serviceevent = new ServiceEvent();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Charger les réservations
        chargerEvents();

        listViewParticipant.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        // 🔹 Récupérer le nom de l'utilisateur à partir de l'ID
                        String userName = serviceparticipant.getUserNameById(item.getId_user());

                        Label userLabel = new Label("👤 Utilisateur: " + userName);
                        userLabel.getStyleClass().add("event-label");

                        HBox hBox = new HBox(15, userLabel);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        setGraphic(hBox);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        setText("Erreur chargement nom utilisateur");
                    }
                }
            }

        });


    }
    private Event event;

    public void setEvent(Event event) {
        if (event == null) {
            System.out.println("❌ `setEvent()` reçu un `null` !");
            return;
        }

        this.event = event;
        System.out.println("✅ Événement reçu avec ID : " + event.getId_event()); // Debug

        chargerParticipants(); // Charger les participants
    }


    private void chargerParticipants() {
        try {
            List<Participant> participants = serviceevent.getParticipantsByEvent(event.getId_event());
            ObservableList<Participant> data = FXCollections.observableArrayList(participants);
            listViewParticipant.setItems(data);

            // Vérification du contenu réel de ListView
            System.out.println("📌 Vérification ListView après chargement :");
            for (Participant p : listViewParticipant.getItems()) {
                System.out.println("🔹 Participant dans ListView : " + p);
            }

            System.out.println("✅ Participants chargés !");
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Impossible de charger les participants : " + e.getMessage());
        }
    }


    private void chargerEvents() {
        try {
            List<Participant> participants = serviceparticipant.afficher();
            // Récupérer le nom  pour chaque participation
            for (Participant participant : participants) {
                String nomEvent = serviceevent.getEventById(participant.getId_event()).getNom();
                System.out.println("📌 Participant chargé : ID = " + participant.getId_part());
            }

            ObservableList<Participant> data = FXCollections.observableArrayList(participants);
            listViewParticipant.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Impossible de charger les participants : " + e.getMessage());
        }
    }

    private void afficherAlerte(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void supprimerParticipant() {
        Participant participantSelectionne = listViewParticipant.getSelectionModel().getSelectedItem();

        if (participantSelectionne == null) {
            afficherAlerte("Avertissement", "Veuillez sélectionner un participant valide.");
            return;
        }

        System.out.println("🔹 ID sélectionné pour suppression : " + participantSelectionne.getId_part());

        if (participantSelectionne.getId_part() <= 0) {
            afficherAlerte("Erreur", "L'ID du participant est invalide.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce participant ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceparticipant.supprimer(participantSelectionne.getId_part()); // Suppression par ID
                afficherAlerte("Succès", "Participant supprimé avec succès !");
                listViewParticipant.getItems().remove(participantSelectionne); // Supprimer de la liste immédiatement
                rafraichirAffichage();
            } catch (SQLException e) {
                afficherAlerte("Erreur", "Impossible de supprimer le participant : " + e.getMessage());
            }
        }
    }


    private void rafraichirAffichage() {
        Task<ObservableList<Participant>> task = new Task<>() {
            @Override
            protected ObservableList<Participant> call() throws SQLException {
                List<Participant> participants = serviceevent.getParticipantsByEvent(event.getId_event());
                for (Participant participant : participants) {
                    String nomEvant = serviceevent.getEventById(participant.getId_event()).getNom();
                }
                return FXCollections.observableArrayList(participants);
            }
        };

        task.setOnSucceeded(event -> listViewParticipant.setItems(task.getValue()));
        task.setOnFailed(event -> afficherAlerte("Erreur SQL", "Impossible de rafraîchir l'affichage"));

        new Thread(task).start(); // Run in background thread
    }


    private void afficherAlerte(String avertissement, String s) {
    }
    @FXML
    private void handleReturnButtonClick() {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) btnreturn.getScene().getWindow();
        stage.close(); // Cela fermera la fenêtre actuelle
    }

}
