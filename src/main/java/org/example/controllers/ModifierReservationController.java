package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.entities.Reservation;
import org.example.services.ServiceReservation;
import org.example.services.ServiceTerrain;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;



public class ModifierReservationController implements Initializable {

    @FXML
    private ComboBox<String> nameComboBox;

    @FXML
    private DatePicker datePickerRes;

    @FXML
    private ComboBox<String> heureComboBoxRes;

    @FXML
    private Button btnModifierRes;

    private Reservation reservationToModify;
    private final ServiceReservation serviceReservation = new ServiceReservation();
    private static final ServiceTerrain serviceTerrain = new ServiceTerrain();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les heures de réservation (8h00 à 23h00)
        heureComboBoxRes.getItems().addAll(
                "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
        );
        try {
            List<String> nomsTerrains = serviceTerrain.getAllTerrainNames(); // Assurez-vous que cette méthode existe
            nameComboBox.getItems().addAll(nomsTerrains);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de charger les noms des terrains : " + e.getMessage());
        }
        // Configurer l'action du bouton "Modifier"
        btnModifierRes.setOnAction(event -> modifierReservation());
    }

    /**
     * Méthode pour initialiser les champs avec les données de la réservation sélectionnée.
     *
     * @param reservation La réservation à modifier.
     */
    public void setReservation(Reservation reservation) {
        this.reservationToModify = reservation;

        // Remplir les champs avec les données de la réservation
        nameComboBox.setValue(reservation.getNomTerrain());
        LocalDateTime dateTime = reservation.getDate_res().toLocalDateTime();
        // Convertir java.sql.Date en LocalDate pour le DatePicker
        datePickerRes.setValue(dateTime.toLocalDate());

        // Sélectionner une heure par défaut (par exemple, 08:00)
        String heureExistante = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        heureComboBoxRes.setValue(heureExistante);
    }

    /**
     * Méthode pour modifier la réservation.
     */
    private void modifierReservation() {
        if (reservationToModify == null) {
            afficherAlerte("Erreur", "Aucune réservation sélectionnée.");
            return;
        }

        // Récupérer les nouvelles valeurs des champs
        String nouveauNomTerrain = nameComboBox.getValue();
        LocalDate nouvelleDate = datePickerRes.getValue();
        String nouvelleHeure = heureComboBoxRes.getValue();

        if (nouveauNomTerrain == null || nouvelleDate == null || nouvelleHeure == null) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier si la date sélectionnée est dans le passé
        LocalDate aujourdHui = LocalDate.now();
        if (nouvelleDate.isBefore(aujourdHui)) {
            afficherAlerte("Erreur", "La date sélectionnée ne peut pas être dans le passé.");
            return;
        }

        LocalTime heure = LocalTime.parse(nouvelleHeure, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dateTime = LocalDateTime.of(nouvelleDate, heure);

        // Mettre à jour la réservation
        reservationToModify.setNomTerrain(nouveauNomTerrain);
        reservationToModify.setDate_res(Timestamp.valueOf(dateTime));

        // Récupérer l'ID du terrain à partir du nom
        try {
            int idTerrain = serviceTerrain.getTerrainIdByName(nouveauNomTerrain); // Assurez-vous que cette méthode existe
            reservationToModify.setId_terrain(idTerrain);
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Impossible de récupérer l'ID du terrain : " + e.getMessage());
            return;
        }

        try {
            // Appeler le service pour mettre à jour la réservation dans la base de données
            serviceReservation.modifier_t(reservationToModify);

            // Afficher un message de succès
            afficherAlerte("Succès", "La réservation a été modifiée avec succès.");

            // Fermer la fenêtre de modification
            Stage stage = (Stage) btnModifierRes.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de modifier la réservation : " + e.getMessage());
        }
    }

    /**
     * Méthode pour afficher une alerte.
     *
     * @param titre   Le titre de l'alerte.
     * @param message Le message de l'alerte.
     */
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleReturnButtonClick(ActionEvent event) {
        try {
            // Charger la nouvelle scène pour homeAffiche.fxml
            Parent homePage = FXMLLoader.load(getClass().getResource("/view/homeAffiche.fxml"));
            Scene homeScene = new Scene(homePage);

            // Obtenir le stage actuel et définir la nouvelle scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Juste un print des erreurs sans afficher une alerte
        }
    }
}