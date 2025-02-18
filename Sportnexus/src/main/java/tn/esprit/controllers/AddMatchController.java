package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tn.esprit.entities.Equipe;
import tn.esprit.entities.Partie;
import tn.esprit.entities.TypeMatch;
import tn.esprit.services.ServiceEquipe;
import tn.esprit.services.ServiceMatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddMatchController {
    @FXML
    private Pane AddMatchpane;
    @FXML
    private Button quitmatchbtn;
    @FXML
    private DatePicker datedatepicker;
    @FXML
    private Button addmatchbtn;
    private List<Equipe> equipesList = new ArrayList<>();
    @FXML
    private TextField resultxtfield;
    @FXML
    private ComboBox<String> placecombobox; // Spécifiez le type pour le ComboBox
    @FXML
    private ComboBox<TypeMatch> typecombobox;

    private final ServiceMatch serviceMatch = new ServiceMatch();

    @FXML
    private ComboBox<String> cbteam1;

    @FXML
    private ComboBox<String> cbteam2;


    @FXML
    public void initialize() {
        typecombobox.setItems(FXCollections.observableArrayList(TypeMatch.values()));
        addmatchbtn.setOnMouseClicked(e-> {
            try {
                this.ajouterMatch();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        chargerEquipes();
    }
    private void ajouterMatch() throws SQLException {
        TypeMatch type = typecombobox.getValue();
        String resultat = resultxtfield.getText();
        Date date_match = java.sql.Date.valueOf(datedatepicker.getValue());
        String place = placecombobox.getValue();
        String team1Name = cbteam1.getValue();
        String team2Name = cbteam2.getValue();
        if (type == null || resultat.isEmpty() || date_match == null || place == null || team1Name == null || team2Name == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }
            Partie match = new Partie(type, resultat, date_match, place);
            Equipe team1 = getEquipeByName(team1Name);
            Equipe team2 = getEquipeByName(team2Name);
            serviceMatch.ajouter(match,team1.getIdEquipe(),team2.getIdEquipe());
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Match ajouté avec succès !");
            clearFields();


    }

    private void clearFields() {
        typecombobox.setValue(null);
        resultxtfield.clear();
        datedatepicker.setValue(null);
        placecombobox.setValue(null);

    }
    private Equipe getEquipeByName(String name) {
        for (Equipe equipe : equipesList) {
            if (equipe.getNom().equals(name)) {
                return equipe;
            }
        }
        return null;
    }
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void quitter(Event event) {
        Stage stage = (Stage) quitmatchbtn.getScene().getWindow();
        stage.close();


    }
    private void chargerEquipes() {
            equipesList.clear();
            ServiceEquipe serviceEquipe = new ServiceEquipe();
        try {
            equipesList = serviceEquipe.afficher();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<String> equipeNames = new ArrayList<>();
            for (Equipe equipe : equipesList) {
                equipeNames.add(equipe.getNom());
            }
        ObservableList<String> observableEquipeNames = FXCollections.observableArrayList(equipeNames);
            cbteam1.setItems(observableEquipeNames);
            cbteam2.setItems(observableEquipeNames);
        }


}