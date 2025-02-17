package tn.esprit.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import tn.esprit.entities.Equipe;
import tn.esprit.entities.Partie;

import java.io.File;

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
    @javafx.fxml.FXML
    private Label datelabel;
    private Partie selectedPartie;

    @javafx.fxml.FXML
    public void initialize() {
    }

    public void setTeams(Partie partie) {
        this.selectedPartie = partie; // Stocker l'équipe
        if (partie != null) {
            typelabel.setText(String.valueOf(partie.getType()));
            resultlabel.setText(String.valueOf(partie.getResultat()));
            datelabel.setText(String.valueOf(partie.getDateMatch()));
            placelabel.setText(String.valueOf(partie.getLieu()));

        }
    }



}