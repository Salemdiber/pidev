package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.entities.Equipe;
import org.example.entities.Partie;
import org.example.entities.TypeMatch;
import org.example.services.ServiceEquipe;
import org.example.services.ServiceMatch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private ComboBox<TypeMatch> updtypecombobox; // Type sécurisé avec TypeMatch


    //private Partie currentPartie;
    private Partie selectedPartie;
    private DetailsMatchController detailsMatchController;
    private TeamhomePController TeamhomePController;
    private List<Equipe> equipesList = new ArrayList<>();
    private int oldEq1ID;
    private int oldEq2ID;

    @FXML
    public void initialize() {
        Updmatchbtn.setOnAction(event -> modifierMatch());
        quitmatchbtn.setOnAction(event -> annulerModification());
        updtypecombobox.setItems( FXCollections.observableArrayList(TypeMatch.values()));
        this.chargerEquipes();
    }

    public void setMatchData(Partie partie) {
        if (partie != null) {
            this.selectedPartie = partie;

            updtypecombobox.setValue(partie.getType());
            updresultxtfield.setText(partie.getResultat());
            updplacecombobox.setValue(partie.getLieu());

            // Assurez-vous que les équipes existent bien dans la liste avant de les sélectionner
            updcbteam1.setValue(partie.getEquipes().get(0).getNom());
            updcbteam2.setValue(partie.getEquipes().get(1).getNom());
            oldEq1ID = partie.getEquipes().get(0).getIdEquipe();
            oldEq2ID = partie.getEquipes().get(1).getIdEquipe();
        }
    }

    private void modifierMatch() {
        if (selectedPartie == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune partie sélectionnée !");
            return;
        }

        // Vérification des champs obligatoires
        if (updtypecombobox.getValue() == null || updresultxtfield.getText().isEmpty() ||
                updplacecombobox.getValue() == null ||
                updcbteam1.getValue() == null || updcbteam2.getValue() == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Mise à jour des données
        selectedPartie.setType(updtypecombobox.getValue());
        selectedPartie.setResultat(updresultxtfield.getText());
        selectedPartie.setLieu(updplacecombobox.getValue());

        // Ajout des équipes
        try {
            ServiceMatch serviceMatch = new ServiceMatch();
            serviceMatch.modifier_t(selectedPartie);
            serviceMatch.modifierPartieEquipes(selectedPartie.getIdMatch(), oldEq1ID , equipesList.stream().filter(p-> p.getNom().equals(updcbteam1.getValue()) ).map(Equipe::getIdEquipe).findFirst().get(),oldEq2ID, equipesList.stream().filter(p-> p.getNom().equals(updcbteam2.getValue()) ).map(Equipe::getIdEquipe).findFirst().get() );
//          serviceMatch.modifierPartieEquipe(selectedPartie.getIdMatch(), oldEq2ID, equipesList.stream().filter(p-> p.getNom().equals(updcbteam2.getValue()) ).map(Equipe::getIdEquipe).findFirst().get());
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Match modifié avec succès !");

            // ✅ Rafraîchir les détails du match
            if (detailsMatchController != null) {
                detailsMatchController.setMatch(selectedPartie);
            } else {
                System.out.println("❌ `detailsMatchController` est NULL");
            }

            // ✅ Rafraîchir la liste des matchs
            if (TeamhomePController != null)
                TeamhomePController.rafraichirAffichage();


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
    private void chargerEquipes() {
        equipesList.clear();
        ServiceEquipe serviceEquipe = new ServiceEquipe();
        try {
            equipesList = serviceEquipe.afficher_t();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<String> equipeNames = new ArrayList<>();
        for (Equipe equipe : equipesList) {
            equipeNames.add(equipe.getNom());
        }
        ObservableList<String> observableEquipeNames = FXCollections.observableArrayList(equipeNames);
        updcbteam1.setItems(observableEquipeNames);
        updcbteam2.setItems(observableEquipeNames);
    }
}
