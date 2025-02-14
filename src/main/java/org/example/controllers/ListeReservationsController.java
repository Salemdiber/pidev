package org.example.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.entities.Reservation;
import org.example.entities.Terrain;
import org.example.services.ServiceReservation;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListeReservationsController implements Initializable {

    @FXML
    private TableView<Reservation> tableViewReservations;



    @FXML
    private TableColumn<Reservation, String> colDateRes;

    @FXML
    private TableColumn<Reservation, Integer> colIduser;

    @FXML
    private TableColumn<Reservation, Integer> colIdterrain;

    private final ServiceReservation serviceReservation = new ServiceReservation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Liaison des colonnes avec les propriétés de Reservation

        colDateRes.setCellValueFactory(cellData -> {
            java.sql.Date dateRes = cellData.getValue().getDate_res(); // Récupérer la date
            String dateString = dateRes.toString(); // Convertir la date en String
            return new ReadOnlyStringWrapper(dateString); // Encapsuler la chaîne
        });
        colIduser.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId_user()));
        colIdterrain.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId_terrain()));

        // Charger les réservations
        chargerReservations();
    }

    private void chargerReservations() {
        try {
            List<Reservation> reservations = serviceReservation.afficher_t();
            ObservableList<Reservation> data = FXCollections.observableArrayList(reservations);
            tableViewReservations.setItems(data);
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
        Reservation reservationSelectionne = tableViewReservations.getSelectionModel().getSelectedItem();
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
            tableViewReservations.setItems(FXCollections.observableArrayList(reservations));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }

    private void afficherAlerte(Alert.AlertType alertType, String avertissement, String s) {
    }
}