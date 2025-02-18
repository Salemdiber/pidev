package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Equipe;
import tn.esprit.services.ServiceEquipe;
import java.io.File;
import java.sql.SQLException;

public class UpdateTeamController
{
    @javafx.fxml.FXML
    private TextField Updptstxtfield;
    @javafx.fxml.FXML
    private Button quitteambtn;
    @javafx.fxml.FXML
    private AnchorPane updteampane;
    @javafx.fxml.FXML
    private TextField Updclassementtxtfield;
    @javafx.fxml.FXML
    private Button Updteambtn;
    @javafx.fxml.FXML
    private TextField Updnametxtfield;
    @javafx.fxml.FXML
    private TextField Upddesctxtfield;
    @javafx.fxml.FXML
    private Button chooseimageUbtn;
    @javafx.fxml.FXML
    private ImageView teamImageU;
    private Equipe currentEquipe;
    @FXML
    private TextField imgtxtfield;

    @javafx.fxml.FXML
    public void initialize() {
        chooseimageUbtn.setOnAction(event -> choisirImage());
        Updteambtn.setOnAction(event -> modifierEquipe());
        quitteambtn.setOnAction(event -> annulerModification());
    }
    public void setTeamData(Equipe equipe) {
        if (equipe != null) {
            this.currentEquipe = equipe;
            Updnametxtfield.setText(equipe.getNom());
            Upddesctxtfield.setText(equipe.getDescription());
            Updclassementtxtfield.setText(String.valueOf(equipe.getClassement()));
            Updptstxtfield.setText(String.valueOf(equipe.getPoints()));
            imgtxtfield.setText(String.valueOf(equipe.getImage()));

            // Charger l'image
            String imagePath = equipe.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    teamImageU.setImage(new Image(file.toURI().toString()));
                }
            }
        }
    }
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Choisir une image pour l'equipe");

        File selectedFile = fileChooser.showOpenDialog(imgtxtfield.getScene().getWindow());

        if (selectedFile != null) {
            // ✅ Met à jour le champ texte avec le chemin absolu
            imgtxtfield.setText(selectedFile.getAbsolutePath());

            // ✅ Met à jour l'ImageView avec l'image sélectionnée
            Image image = new Image(selectedFile.toURI().toString());
            teamImageU.setImage(image);
        } else {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune sélection", "Aucune image n'a été sélectionnée !");
        }
    }

    private void modifierEquipe() {
        if (currentEquipe == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune équipe sélectionnée !");
            return;
        }

        String nom = Updnametxtfield.getText();
        String description = Upddesctxtfield.getText();
        String img = imgtxtfield.getText();

        int classement;
        int points;
        try {
            classement = Integer.parseInt(Updclassementtxtfield.getText());
            points = Integer.parseInt(Updptstxtfield.getText());
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Classement et points doivent être des nombres !");
            return;
        }

        if (nom.isEmpty() || description.isEmpty() || img.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        currentEquipe.setNom(nom);
        currentEquipe.setClassement(classement);
        currentEquipe.setDescription(description);
        currentEquipe.setPoints(points);
        currentEquipe.setImage(img);

        try {
            ServiceEquipe serviceEquipe = new ServiceEquipe();
            serviceEquipe.modifier(currentEquipe);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Équipe modifiée avec succès !");

            // ✅ Rafraîchir les détails
            if (detailTeamController != null) {
                detailTeamController.setTeams(currentEquipe);
            } else {
                System.out.println("❌ `detailTeamController` est NULL");
            }

            // ✅ Rafraîchir la liste des équipes
            if (TeamhomePController != null) {
                TeamhomePController.rafraichirAffichage();
            } else {
                System.out.println("❌ `TeamhomePController` est NULL");
            }

            // ✅ Fermer la fenêtre
            fermerFenetre();
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier l'équipe : " + e.getMessage());
        }
    }

    // Fusionne avec updateTeam()
    @FXML
    public void updateTeam(ActionEvent actionEvent) {
        modifierEquipe();
    }


    private DetailTeamController detailTeamController;

    public void setDetailTeamController(DetailTeamController controller) {
        this.detailTeamController = controller;
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void fermerFenetre() {
        Stage stage = (Stage) Updnametxtfield.getScene().getWindow();
        stage.close();

        if (TeamhomePController != null) {
            TeamhomePController.rafraichirAffichage();
        }
    }
    private TeamhomePController TeamhomePController;

    public void setHomeAfficheTerrainController(TeamhomePController controller) {
        this.TeamhomePController = controller;
    }
    @FXML
    private void annulerModification() {
        Stage stage = (Stage) quitteambtn.getScene().getWindow();
        stage.close();
    }



}