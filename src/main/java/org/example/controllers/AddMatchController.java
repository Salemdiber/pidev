package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.entities.Equipe;
import org.example.entities.Partie;
import org.example.entities.SessionManager;
import org.example.services.ServiceEquipe;
import org.example.services.ServiceMatch;
import org.example.entities.TypeMatch;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddMatchController {
    @FXML
    private Pane AddMatchpane;
    @FXML
    private Button quitmatchbtn;

    @FXML
    private Button addmatchbtn;
    @FXML
    private Button btnreturn;
    private List<Equipe> equipesList = new ArrayList<>();
    @FXML
    private TextField resultxtfield;
    @FXML
    private ComboBox<String> placecombobox;
    @FXML
    private ComboBox<TypeMatch> typecombobox;

    private final ServiceMatch serviceMatch = new ServiceMatch();

    @FXML
    private ComboBox<String> cbteam1;

    @FXML
    private ComboBox<String> cbteam2;
    @FXML
    private Label errorType, errorResult, errorTeam1, errorTeam2;


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
        String role = SessionManager.getInstance().getUserRole();
        if (!"Admin".equals(role)) {
            addmatchbtn.setDisable(true);
        }

    }
    private void ajouterMatch() throws SQLException {
        // Réinitialiser la visibilité des messages d'erreur
        errorType.setText("");
        errorResult.setText("");
        errorTeam1.setText("");
        errorTeam2.setText("");

        boolean isValid = true;

        // Récupération des valeurs saisies
        TypeMatch type = typecombobox.getValue();
        String resultat = resultxtfield.getText();
        String team1Name = cbteam1.getValue();
        String team2Name = cbteam2.getValue();

        // Vérification des champs obligatoires
        if (type == null) {
            errorType.setText("Le type est requis !");
            isValid = false;
        }
        if (resultat.isEmpty()) {
            errorResult.setText("Le resultat est requis !");
            isValid = false;
        } else if (!resultat.matches("\\d+:\\d+")) {
            errorResult.setText("Le format du résultat est invalide (ex: 1:0) !");
            isValid = false;
        }
        if (team1Name == null) {
            errorTeam1.setText("Le nom d'equipe 1 est requis !");
            isValid = false;
        }
        if (team2Name == null) {
            errorTeam2.setText("Le nom d'equipe 2 est requis !");
            isValid = false;
        }
        if (team1Name != null && team2Name != null && team1Name.equals(team2Name)) {
            errorTeam2.setText("* Les équipes doivent être différentes");
            isValid = false;
        }

        if (!isValid) return;





        Partie match = new Partie(type, resultat);
        Equipe team1 = getEquipeByName(team1Name);
        Equipe team2 = getEquipeByName(team2Name);
        serviceMatch.ajouter_t(match, team1.getIdEquipe(), team2.getIdEquipe());

        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Match ajouté avec succès !");
        clearFields();
    }

    private void clearFields() {
        typecombobox.setValue(null);
        resultxtfield.clear();

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
            cbteam1.setItems(observableEquipeNames);
            cbteam2.setItems(observableEquipeNames);
        }
    @FXML
    private void handleReturnButtonClick(ActionEvent event) {
        try {

            Parent homePage = FXMLLoader.load(getClass().getResource("/view/team_home1.fxml"));
            Scene homeScene = new Scene(homePage);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}