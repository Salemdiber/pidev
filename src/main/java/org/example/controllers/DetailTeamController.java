package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.entities.Equipe;
import org.example.entities.SessionManager;
import org.example.services.ServiceEquipe;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DetailTeamController
{
    @javafx.fxml.FXML
    private Label namelabel;
    @javafx.fxml.FXML
    private Label descriptionlabel;
    @javafx.fxml.FXML
    private Button quitteambtn;
    @javafx.fxml.FXML
    private Label classementlabel;
    @javafx.fxml.FXML
    private Pane detailsteamspane;
    @javafx.fxml.FXML
    private Label pointslabel;
    @javafx.fxml.FXML
    private Button updteambtn;
    @javafx.fxml.FXML
    private Button deleteteambtn;
    @FXML
    private ImageView imagelabel;
    private Equipe selectedEquipe;
    private TeamListController teamListController;

    public void setTeamListController(TeamListController teamListController) {
        this.teamListController = teamListController;
    }

    @javafx.fxml.FXML
    public void initialize() {
        String role = SessionManager.getInstance().getUserRole();
        if (!"Admin".equals(role)) {
            deleteteambtn.setDisable(true);
            updteambtn.setDisable(true);
        }
    }
    public void setTeams(Equipe equipe) {
        this.selectedEquipe = equipe;
        if (equipe != null) {
            namelabel.setText(equipe.getNom());
            classementlabel.setText(String.valueOf(equipe.getClassement()));
            descriptionlabel.setText(equipe.getDescription());
            pointslabel.setText(String.valueOf(equipe.getPoints()));


            String imagePath = equipe.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    try {
                        Image image = new Image(file.toURI().toString());
                        imagelabel.setImage(image);
                    } catch (Exception e) {
                        System.err.println("Erreur de chargement de l'image : " + e.getMessage());
                        setDefaultImage();
                    }
                } else {
                    setDefaultImage();
                }
            } else {
                setDefaultImage();
            }
        }
    }


    private void setDefaultImage() {

        String defaultImagePath = "file:src/assets/default.jpg";
        imagelabel.setImage(new Image(defaultImagePath));
    }

    @FXML
    public void openUpdateTeamWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateTeam.fxml"));
            Parent root = loader.load();

            UpdateTeamController updateController = loader.getController();
            updateController.setTeamData(selectedEquipe);
            updateController.setDetailTeamController(this);

            Stage stage = new Stage();
            stage.setTitle("Update Team");
            stage.setScene(new Scene(root));


            stage.setOnHidden(event -> setTeams(selectedEquipe));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteTeam(ActionEvent event) throws SQLException {
        if (selectedEquipe != null) {

            ServiceEquipe serviceEquipe = new ServiceEquipe();
            serviceEquipe.supprimer_t(selectedEquipe.getIdEquipe());

                Stage stage = (Stage) deleteteambtn.getScene().getWindow();

                stage.close();


        }
    }
    private void fermerFenetre() {
        Stage stage = (Stage) namelabel.getScene().getWindow();
        stage.close();
    }


    @javafx.fxml.FXML
    public void quitDetails(ActionEvent actionEvent) {
        this.teamListController.rafraichirAffichage(selectedEquipe.getIdEquipe());
        Stage stage = (Stage) quitteambtn.getScene().getWindow();
        stage.close();
    }
}


