package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.entities.SessionManager;
import org.example.entities.User;
import org.example.services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ProfilController {

    @FXML
    private Button btnRec;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    private User currentUser;

    public void initialize() {
        currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            setUserDetails(currentUser);
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void setUserDetails(User currentUser) {
        System.out.println("User is logged in: " + currentUser.getEmail());

        nomField.setText(currentUser.getNom());
        prenomField.setText(currentUser.getPrenom());
        emailField.setText(currentUser.getEmail());
        roleField.setText(currentUser.getRole());
    }

    public void handleEditProfil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/update.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleEditPassword(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update_password.fxml"));
        Parent root = loader.load();

        // Get the controller and set the current user
        UpdatePasswordController controller = loader.getController();
        controller.setUserDetails(currentUser);

        // Set the new scene
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    public void del(ActionEvent actionEvent) {
        currentUser = SessionManager.getCurrentUser();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Confirmation de la suppression");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer votre compte ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ServiceUser serviceuser = new ServiceUser();
                    serviceuser.supprimer(currentUser.getIdUser());

                    // Show success message
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                    infoAlert.setTitle("Suppression réussie");
                    infoAlert.setHeaderText(null);
                    infoAlert.setContentText("Votre compte a été supprimé avec succès !");
                    infoAlert.showAndWait();

                    // Navigate to login screen
                    goToLogin(actionEvent);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the error, possibly show an error message
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText("Erreur lors de la suppression du compte");
                    errorAlert.setContentText("Une erreur est survenue lors de la suppression du compte. Veuillez réessayer.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private void goToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login.fxml")));
            Scene scene = new Scene(root);
            ((Node) event.getSource()).getScene().getWindow().hide(); // Close the current window
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load login scene.");
        }
    }

    public void versRec(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/reclammationHome.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}
