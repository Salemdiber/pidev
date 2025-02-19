package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.example.entities.User;
import org.example.services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private PasswordField mdpField;

    @FXML
    private Button signupButton;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        // Initialize the ComboBox with options
        roleComboBox.getItems().addAll("Admin", "User");
    }

    @FXML
    private void handleSignupButtonAction(ActionEvent event) throws SQLException {
        // Retrieve data from input fields
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String role = roleComboBox.getValue();
        String mdp = mdpField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || role == null || mdp.isEmpty()) {
            showAlert("Error","Les champs sont vides");
            return;
        }
        if (!isEmailValid(email))
        {
            showAlert("Error","Email est invalide");
            return;
        }
        if (nom.isBlank() || prenom.isBlank() || email.isBlank()  || mdp.isBlank()) {
            showAlert("Error","Les champs sont des chaines vides");
            return;
        }


        // Create a new User object
        User newUser = new User(nom, prenom, email, role, mdp);

        // Insert the user into the database
        ServiceUser usersevice = new ServiceUser();
        if (usersevice.ajouteru(newUser)) {
            System.out.println("Signup successful!");
            clearInputFields();
        } else {
            System.out.println("Signup failed. Please try again.");
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        try {
            // Charger le fichier login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            // Obtenir le stage actuel
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Ajuster la taille de la fenêtre pour qu'elle prenne toute la taille de l'écran
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            // Mettre à jour la scène avec la nouvelle vue
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load login.fxml");
        }
    }


    private void clearInputFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        roleComboBox.setValue(null);
        mdpField.clear();
    }
    private void showAlert(String chaine1,String chaine2)
    {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle(chaine1);
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(chaine2);
        infoAlert.showAndWait();
    }

    public boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
