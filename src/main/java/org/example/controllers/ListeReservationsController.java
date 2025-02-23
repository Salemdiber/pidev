package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Reservation;
import org.example.services.ServiceReservation;
import org.example.services.ServiceTerrain;
import org.example.utils.MyDB;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
        chargerReservations();

        listViewReservations.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label dateLabel = new Label("📅 DATE RESERVATION: " + item.getDate_res());
                    Label userLabel = null;
                    userLabel = new Label("👤 Utilisateur: " + serviceReservation.getUserNameById(item.getId_user()));
                    Label terrainLabel = new Label("🏟️ Terrain: " + item.getNomTerrain());

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

            for (Reservation reservation : reservations) {
                String nomTerrain = serviceTerrain.getTerrainById(reservation.getId_terrain()).getNom();
                reservation.setNomTerrain(nomTerrain);
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
            afficherAlerte("Avertissement", "Veuillez sélectionner une réservation à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer cette réservation ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws SQLException {
                    try {
                        serviceReservation.supprimer_t(reservationSelectionne.getId_res());
                    } catch (SQLException e) {
                        throw new SQLException("Erreur lors de la suppression de la réservation : " + e.getMessage(), e);
                    }
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                afficherAlerte("Succès", "Réservation supprimée avec succès !");
                rafraichirAffichage();  // Mettez à jour l'affichage après la suppression

                // Redirection vers HomeAffiche.fxml après suppression réussie
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeAffiche.fxml"));
                    Scene scene = new Scene(loader.load());

                    // Obtenez la fenêtre actuelle
                    Stage stage = (Stage) listViewReservations.getScene().getWindow(); // Utilisez votre ListView ou un autre composant pour récupérer la fenêtre
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    afficherAlerte("Erreur", "Erreur lors du chargement de la page d'accueil.");
                }
            });

            task.setOnFailed(event -> {
                Throwable exception = task.getException();
                afficherAlerte("Erreur", exception.getMessage());
            });

            new Thread(task).start();
        }
    }



    private void rafraichirAffichage() {
        try {
            // Récupérer toutes les réservations après la suppression
            List<Reservation> reservations = serviceReservation.afficher_t();
            for (Reservation reservation : reservations) {
                String nomTerrain = serviceTerrain.getTerrainById(reservation.getId_terrain()).getNom();
                reservation.setNomTerrain(nomTerrain);
            }

            // Réinitialiser l'ObservableList et mettre à jour la ListView
            ObservableList<Reservation> data = FXCollections.observableArrayList(reservations);
            Platform.runLater(() -> listViewReservations.setItems(data));

        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Impossible de rafraîchir l'affichage.");
        }
    }




    private void afficherAlerte(String avertissement, String s) {
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



    @FXML
    private void handleModifierButtonClick(ActionEvent event) {
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

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            rafraichirAffichage();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible d'ouvrir la page de modification.");
        }
    }

}