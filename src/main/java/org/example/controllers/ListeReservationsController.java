package org.example.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.entities.Reservation;
import org.example.services.ServiceReservation;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListeReservationsController implements Initializable {

    @FXML
    private ListView<Reservation> listViewReservations;



    @FXML
    private TableColumn<Reservation, String> colDateRes;

    @FXML
    private TableColumn<Reservation, Integer> colIduser;

    @FXML
    private TableColumn<Reservation, Integer> colIdterrain;
    @FXML
    private Button btnreturn;

    private final ServiceReservation serviceReservation = new ServiceReservation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Charger les réservations
        chargerReservations();

        // Configurer le ListView pour afficher les réservations
        listViewReservations.setCellFactory(param -> new ListCell<Reservation>() {
            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Personnaliser l'affichage de chaque réservation
                    setText("Date de Reservation : " + item.getDate_res() + ", USER ID: " + item.getId_user() + ", Terrain ID: " + item.getId_terrain());
                }
            }
        });
    }

    private void chargerReservations() {
        try {
            List<Reservation> reservations = serviceReservation.afficher_t();
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
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un reservation à supprimer.");
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
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Reservation supprimé avec succès !");
                rafraichirAffichage();
            } catch (SQLException e){
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le reservation : " + e.getMessage());
            }
        }
    }

    private void rafraichirAffichage() {
        try {
            List<Reservation> reservations = serviceReservation.afficher_t();
            listViewReservations.setItems(FXCollections.observableArrayList(reservations));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }

    private void afficherAlerte(Alert.AlertType alertType, String avertissement, String s) {
    }
    @FXML
    private void handleReturnButtonClick() {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) btnreturn.getScene().getWindow();
        stage.close(); // Cela fermera la fenêtre actuelle
    }
}