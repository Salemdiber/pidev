package controllers;

import entities.Publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.ServicePublication;

import java.io.IOException;
import java.sql.SQLException;

public class AjoutPubController {

    @javafx.fxml.FXML
    private Button annulerbtn;

    @javafx.fxml.FXML
    private Button ajouterbtn;

    private final ServicePublication servicePublication = new ServicePublication();

    @javafx.fxml.FXML
    private TextField titretf;

    @javafx.fxml.FXML
    private TextField desctf;

    @javafx.fxml.FXML
    public void AjouterPublication(ActionEvent actionEvent) {
        String titre = titretf.getText().trim();
        String description = desctf.getText().trim();

        if (titre.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs vides");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        Publication publication = new Publication(titre, description);

        try {
            servicePublication.ajouter(publication);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Publication ajoutée avec succès !");
            alert.showAndWait();

            titretf.clear();
            desctf.clear();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la publication: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Échec de l'ajout de la publication.");
            alert.showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void annulerPub(ActionEvent actionEvent) {
        titretf.clear();
        desctf.clear();
    }

    @javafx.fxml.FXML
    public void AfficherPublications(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherPublications.fxml"));
            titretf.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
