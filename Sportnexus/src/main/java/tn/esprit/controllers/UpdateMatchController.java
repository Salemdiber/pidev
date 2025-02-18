package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tn.esprit.entities.Equipe;
import tn.esprit.entities.Partie;
import tn.esprit.entities.TypeMatch;
import tn.esprit.services.ServiceMatch;

import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;

public class UpdateMatchController {

    @FXML
    private Pane updmatchpane;
    @FXML
    private Button quitmatchbtn;
    @FXML
    private Button Updmatchbtn;
    @FXML
    private ComboBox<String> updcbteam1;
    @FXML
    private ComboBox<String> updcbteam2;
    @FXML
    private TextField updresultxtfield;
    @FXML
    private ComboBox<String> updplacecombobox;
    @FXML
    private DatePicker upddatedatepicker;
    @FXML
    private ComboBox<TypeMatch> updtypecombobox; // Type sécurisé avec TypeMatch

    //private Partie currentPartie;
    private Partie selectedPartie;
    private DetailsMatchController detailsMatchController;
    private TeamhomePController TeamhomePController;

    @FXML
    public void initialize() {
        Updmatchbtn.setOnAction(event -> modifierMatch());
        quitmatchbtn.setOnAction(event -> annulerModification());
    }

    public void setMatchData(Partie partie) {
        if (partie != null) {
            this.selectedPartie = partie;

            updtypecombobox.setValue(partie.getType());
            updresultxtfield.setText(partie.getResultat());
//            if (partie.getDateMatch() != null) {
//                java.sql.Date sqlDate = new java.sql.Date(partie.getDateMatch().getTime());
//                upddatedatepicker.setValue(sqlDate.toLocalDate());
//            }
            updplacecombobox.setValue(partie.getLieu());

            // Assurez-vous que les équipes existent bien dans la liste avant de les sélectionner
            updcbteam1.setValue(partie.getEquipes().get(0).getNom());
            updcbteam2.setValue(partie.getEquipes().get(1).getNom());
        }
    }

    private void modifierMatch() {
        if (selectedPartie == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune partie sélectionnée !");
            return;
        }

        // Vérification des champs obligatoires
        if (updtypecombobox.getValue() == null || updresultxtfield.getText().isEmpty() /*||
                upddatedatepicker.getValue() == null */ || updplacecombobox.getValue() == null ||
                updcbteam1.getValue() == null || updcbteam2.getValue() == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Mise à jour des données
        selectedPartie.setType(updtypecombobox.getValue());
        selectedPartie.setResultat(updresultxtfield.getText());
        //Date date = Date.valueOf(upddatedatepicker.getValue());
        selectedPartie.setLieu(updplacecombobox.getValue());

        // Ajout des équipes
        try {
            ServiceMatch serviceMatch = new ServiceMatch();
            serviceMatch.modifier(selectedPartie);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Match modifié avec succès !");

            // ✅ Rafraîchir les détails du match
            if (detailsMatchController != null) {
                detailsMatchController.setMatch(selectedPartie);
            } else {
                System.out.println("❌ `detailsMatchController` est NULL");
            }

            // ✅ Rafraîchir la liste des matchs
            if (TeamhomePController != null) {
                TeamhomePController.rafraichirAffichage();
            } else {
                System.out.println("❌ `TeamhomePController` est NULL");
            }

            // ✅ Fermer la fenêtre
            fermerFenetre();
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier le match : " + e.getMessage());
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) updresultxtfield.getScene().getWindow();
        stage.close();

        if (TeamhomePController != null) {
            TeamhomePController.rafraichirAffichage();
        }
    }

    @FXML
    private void annulerModification() {
        Stage stage = (Stage) quitmatchbtn.getScene().getWindow();
        stage.close();
    }

    public void setDetailsMatchController(DetailsMatchController detailsMatchController) {
        this.detailsMatchController = detailsMatchController;
    }

    public void setHomeAfficheTerrainController(TeamhomePController controller) {
        this.TeamhomePController = controller;
    }
}
