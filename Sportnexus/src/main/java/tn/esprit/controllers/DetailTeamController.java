package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tn.esprit.entities.Equipe;
import tn.esprit.services.ServiceEquipe;

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

    @javafx.fxml.FXML
    public void initialize() {
    }
    public void setTeams(Equipe equipe) {
        this.selectedEquipe = equipe; // Stocker l'équipe
        if (equipe != null) {
            namelabel.setText(equipe.getNom());
            classementlabel.setText(String.valueOf(equipe.getClassement()));
            descriptionlabel.setText(equipe.getDescription());
            pointslabel.setText(String.valueOf(equipe.getPoints()));

            // Charger l'image
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
        // Remplacez "default.jpg" par un chemin valide d'image par défaut
        String defaultImagePath = "file:src/assets/default.jpg";
        imagelabel.setImage(new Image(defaultImagePath));
    }

    @FXML
    public void openUpdateTeamWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/UpdateTeam.fxml"));
            Parent root = loader.load();

            UpdateTeamController updateController = loader.getController();
            updateController.setTeamData(selectedEquipe);
            updateController.setDetailTeamController(this);  // Pass reference to DetailTeamController

            Stage stage = new Stage();
            stage.setTitle("Update Team");
            stage.setScene(new Scene(root));

            // ✅ Listen for window close and refresh team details
            stage.setOnHidden(event -> setTeams(selectedEquipe));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteTeam(ActionEvent event) throws SQLException {
        if (selectedEquipe != null) {
            // Appeler le service de suppression (à implémenter selon ton architecture)
            ServiceEquipe serviceEquipe = new ServiceEquipe(); // Assure-toi d’avoir un service pour gérer les équipes
            serviceEquipe.supprimer(selectedEquipe.getIdEquipe());
                // Fermer la fenêtre actuelle
                Stage stage = (Stage) deleteteambtn.getScene().getWindow();
                //System.err.println("Erreur lors de la suppression de l'équipe.")
                stage.close();


        }
    }
    private void fermerFenetre() {
        Stage stage = (Stage) namelabel.getScene().getWindow();
        stage.close();

        if (TeamhomePController != null) {
            TeamhomePController.rafraichirAffichage();
        }

    }
    private TeamhomePController TeamhomePController;




    @javafx.fxml.FXML
    public void quitDetails(ActionEvent actionEvent) {
        Stage stage = (Stage) quitteambtn.getScene().getWindow();
        stage.close();
    }
}


