package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Reservation;
import org.example.services.ServiceReservation;
import org.example.services.ServiceTerrain;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListeReservationsController implements Initializable {

    @FXML
    private ListView<Reservation> listViewReservations;
    @FXML
    private Button btnreturn;
    @FXML
    private Button btnmodifierreservation;

    private final ServiceReservation serviceReservation = new ServiceReservation();
    private final ServiceTerrain serviceTerrain = new ServiceTerrain();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Charger les réservations
        chargerReservations();
        btnmodifierreservation.setOnAction(event -> handleModifierButtonClick());
        // Configurer le ListView pour afficher les réservations
        listViewReservations.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label dateLabel = new Label("📅 DATE RESERVATION" +item.getDate_res() );
                    Label userLabel = new Label("👤 ID Utilisateur: " + item.getId_user());
                    Label terrainLabel = new Label("🏟️  Terrain: " + item.getNomTerrain());

                    dateLabel.getStyleClass().add("reservation-label");
                    userLabel.getStyleClass().add("reservation-label");
                    terrainLabel.getStyleClass().add("reservation-label");

                    HBox hBox = new HBox(15, dateLabel, userLabel, terrainLabel);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(hBox);
                }
            }

        });

    }


    private void chargerReservations() {
        try {
            List<Reservation> reservations = serviceReservation.afficher_t();
            // Récupérer le nom du terrain pour chaque réservation
            for (Reservation reservation : reservations) {
                String nomTerrain = serviceTerrain.getTerrainById(reservation.getId_terrain()).getNom();
                reservation.setNomTerrain(nomTerrain);  // Mettre à jour le nom du terrain dans la réservation
            }

            ObservableList<Reservation> data = FXCollections.observableArrayList(reservations);
            listViewReservations.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Impossible de charger les réservations : " + e.getMessage());
        }
    }


    private void afficherAlerte(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void supprimerReservation() {
        Reservation reservationSelectionne = listViewReservations.getSelectionModel().getSelectedItem();
        if (reservationSelectionne == null) {
            afficherAlerte("Avertissement", "Veuillez sélectionner un reservation à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce reservation ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceReservation.supprimer_t(reservationSelectionne.getId_res());
                afficherAlerte("Succès", "Reservation supprimé avec succès !");
                rafraichirAffichage();
            } catch (SQLException e){
                afficherAlerte("Erreur", "Impossible de supprimer le reservation : " + e.getMessage());
            }
        }
    }

    private void rafraichirAffichage() {
        Task<ObservableList<Reservation>> task = new Task<>() {
            @Override
            protected ObservableList<Reservation> call() throws SQLException {
                List<Reservation> reservations = serviceReservation.afficher_t();
                for (Reservation reservation : reservations) {
                    String nomTerrain = serviceTerrain.getTerrainById(reservation.getId_terrain()).getNom();
                    reservation.setNomTerrain(nomTerrain);
                }
                return FXCollections.observableArrayList(reservations);
            }
        };

        task.setOnSucceeded(event -> listViewReservations.setItems(task.getValue()));
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

    @FXML
    private void handleModifierButtonClick() {
        Reservation reservationSelectionne = listViewReservations.getSelectionModel().getSelectedItem();
        if (reservationSelectionne == null) {
            afficherAlerte("Avertissement", "Veuillez sélectionner une réservation à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateReservation.fxml"));
            Parent root = loader.load();

            ModifierReservationController modifierController = loader.getController();
            modifierController.setReservation(reservationSelectionne);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réservation");
            stage.showAndWait(); // Wait for the window to close before proceeding

            rafraichirAffichage(); // Refresh the list after the window is closed
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible d'ouvrir la page de modification.");
        }
    }

}