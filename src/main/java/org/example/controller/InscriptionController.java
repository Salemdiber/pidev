package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.example.dao.UserDAO;
import org.example.model.User;

import java.io.IOException;

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
    private void handleSignupButtonAction(ActionEvent event) {
        // Retrieve data from input fields
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String role = roleComboBox.getValue();
        String mdp = mdpField.getText();

        // Perform validation
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || role == null || mdp.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Create a new User object
        User newUser = new User(nom, prenom, email, role, mdp);

        // Insert the user into the database
        UserDAO userDAO = new UserDAO();
        if (userDAO.insertUser(newUser)) {
            System.out.println("Signup successful!");
            clearInputFields();
        } else {
            System.out.println("Signup failed. Please try again.");
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
}
