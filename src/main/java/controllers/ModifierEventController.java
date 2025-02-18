package controllers;

import entities.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceEvent;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModifierEventController {

    @FXML
    private DatePicker datetf;
    @FXML
    private Button modifbtn;
    @FXML
    private Button annulerbtn;
    @FXML
    private TextField nametxtfield;
    @FXML
    private TextField lieutxtfield;
    @FXML
    private TextField desctxtfield;
    @FXML
    private ImageView imageview;
    @FXML
    private ComboBox hourComboBox;
    @FXML
    private ComboBox minuteComboBox;
    @FXML
    private Button btnChoisirImage;
    private AfficherEventController afficherEventController;
    @FXML
    private TextField txtImg;
    @FXML
    private Button btnreturn;

    public void setAfficherEventController(AfficherEventController controller) {
        this.afficherEventController = controller;
    }

    private Event eventSelected;
    private final ServiceEvent serviceEvent = new ServiceEvent();



    @FXML
    public void initialize() {
        btnChoisirImage.setOnAction(event -> choisirImage());
        modifbtn.setOnAction(event -> modifierEvent());
        annulerbtn.setOnAction(event -> annulerModification());

        // Remplir les ComboBox d'heures et de minutes
        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            minuteComboBox.getItems().add(String.format("%02d", i));
        }
    }

    public void setEvent(Event event) {
        this.eventSelected = event;
        if (event != null) {
            nametxtfield.setText(event.getNom());
            lieutxtfield.setText(event.getLieu());
            desctxtfield.setText(event.getDesc());
            txtImg.setText(event.getImage());
            datetf.setValue(event.getDate().toLocalDate());

            // ✅ Extraire et afficher l'heure et les minutes
            hourComboBox.setValue(String.format("%02d", event.getDate().getHour()));
            minuteComboBox.setValue(String.format("%02d", event.getDate().getMinute()));
        }
    }

    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Choisir une image pour l'event");

        File selectedFile = fileChooser.showOpenDialog(txtImg.getScene().getWindow());

        if (selectedFile != null) {
            // ✅ Met à jour le champ texte avec le chemin absolu
            txtImg.setText(selectedFile.getAbsolutePath());

            // ✅ Met à jour l'ImageView avec l'image sélectionnée
            Image image = new Image(selectedFile.toURI().toString());
            imageview.setImage(image);
        } else {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune sélection", "Aucune image n'a été sélectionnée !");
        }
    }
    private void modifierEvent() {
        if (eventSelected == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun event sélectionné !");
            return;
        }

        String nom = nametxtfield.getText();
        String lieu = lieutxtfield.getText();
        String description = desctxtfield.getText();
        String img = txtImg.getText();

        // Vérifier si tous les champs sont remplis
        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || img.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Vérification que le lieu commence par une majuscule
        if (!Character.isUpperCase(lieu.charAt(0))) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le lieu doit commencer par une majuscule !");
            return;
        }

        // Vérification de la longueur du nom et de la description
        if (nom.length() < 3) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le nom doit comporter au moins 3 caractères.");
            return;
        }

        if (description.length() < 10) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit comporter au moins 10 caractères.");
            return;
        }

        int hour = Integer.parseInt(hourComboBox.getValue().toString());
        int minute = Integer.parseInt(minuteComboBox.getValue().toString());
        LocalDateTime dateTime = datetf.getValue().atTime(hour, minute);
        LocalDateTime today = LocalDateTime.now();
        if (dateTime.isBefore(today)) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Vous ne pouvez pas planifier un event pour une date passée.");
            return;
        }
        eventSelected.setNom(nom);
        eventSelected.setLieu(lieu);
        eventSelected.setDesc(description);
        eventSelected.setImage(img);
        eventSelected.setDate(dateTime);

        try {
            serviceEvent.modifier(eventSelected);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "event modifié avec succès !");
            fermerFenetre(); // Fermer la fenêtre de modification
            if (afficherEventController != null) {
                afficherEventController.rafraichirAffichage();
            }
            // Rafraîchir l'affichage
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier le event : " + e.getMessage());
        }
    }


    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private DetailsEventController detailsEventController;

    private void fermerFenetre() {
        if (detailsEventController != null) {
            detailsEventController.setEvent(eventSelected); // 🔹 Met à jour l'événement dans la page des détails
        } else {
            System.out.println("⚠️ `detailsEventController` est NULL, impossible de mettre à jour l'affichage !");
        }

        Stage stage = (Stage) modifbtn.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void annulerModification() {
        Stage stage = (Stage) annulerbtn.getScene().getWindow();
        stage.close();
    }
    public void setDetailsEventController(DetailsEventController controller) {
        this.detailsEventController = controller;
    }
    @FXML
    private void handleReturnButtonClick() {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) btnreturn.getScene().getWindow();
        stage.close(); // Cela fermera la fenêtre actuelle
    }

}
