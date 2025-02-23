package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.entities.Partie;
import org.example.entities.SessionManager;
import org.example.services.ServiceEquipe;
import org.example.services.ServiceMatch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailsMatchController
{
    @javafx.fxml.FXML
    private Button Deletematchbtn;
    @javafx.fxml.FXML
    private Button quitmatchbtn;
    @javafx.fxml.FXML
    private Pane detailsmatchpane;
    @javafx.fxml.FXML
    private Label typelabel;
    @javafx.fxml.FXML
    private Label placelabel;
    @javafx.fxml.FXML
    private Label resultlabel;
    @javafx.fxml.FXML
    private Label team2label;
    @javafx.fxml.FXML
    private Label team1label;
    @javafx.fxml.FXML
    private Button Updmatchbtn;
    private Partie selectedPartie;
    private MatchListController matchListController;

    public void setMatchListController(MatchListController matchListController) {
        this.matchListController = matchListController;
    }

    public void setSelectedPartie(Partie selectedPartie) {
        this.selectedPartie = selectedPartie;
    }

    @javafx.fxml.FXML
    public void initialize() {
        String role = SessionManager.getInstance().getUserRole();
        if (!"Admin".equals(role)) {
            Updmatchbtn.setDisable(true);
            Deletematchbtn.setDisable(true);
        }


    }

    public void setMatch(Partie partie) {
        this.selectedPartie = partie;
        if (partie != null) {
            typelabel.setText(String.valueOf(partie.getType()));
            resultlabel.setText(String.valueOf(partie.getResultat()));

        }
        ServiceMatch serviceMatch = new ServiceMatch();
        ServiceEquipe serviceEquipe = new ServiceEquipe();
        List<Integer> eqIDS = new ArrayList<>();
        try {
            eqIDS = serviceMatch.getEquipesParMatch(selectedPartie.getIdMatch());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        team1label.setText(serviceEquipe.getOne(eqIDS.get(0)).getNom());
        team2label.setText(serviceEquipe.getOne(eqIDS.get(1)).getNom());
    }

    @FXML
    public void openUpdateMatchWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateMatch.fxml"));
            Parent root = loader.load();

            UpdateMatchController updateController = loader.getController();
            updateController.setMatchData(selectedPartie);
            updateController.setDetailsMatchController(this);

            Stage stage = new Stage();
            stage.setTitle("Update Team");
            stage.setScene(new Scene(root));


            stage.setOnHidden(event -> setMatch(selectedPartie));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteMatch(ActionEvent event) throws SQLException {
        if (selectedPartie != null) {

            ServiceMatch serviceMatch = new ServiceMatch();
            serviceMatch.supprimer_t(selectedPartie.getIdMatch());

            Stage stage = (Stage) Deletematchbtn.getScene().getWindow();

            stage.close();


        }
    }
    @javafx.fxml.FXML
    public void quitDetails(ActionEvent actionEvent) {
        this.matchListController.rafraichirAffichage(selectedPartie.getIdMatch());
        Stage stage = (Stage) quitmatchbtn.getScene().getWindow();
        stage.close();
    }


}