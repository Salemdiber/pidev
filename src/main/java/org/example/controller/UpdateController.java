package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.example.entities.User;

import org.example.entities.SessionManager;
import org.example.services.ServiceUser;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    @FXML
    private PasswordField mdpField;

    @FXML
    private Button submitButton;

    @FXML
    private Label errorLabel;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getCurrentUser();
        System.out.println("Currqqqqent user ID: " + currentUser.getIdUser());

        if (currentUser != null) {

            setUserDetails(currentUser);
            // displayUserImage(currentUser);

        }
    }

    public void setUserDetails(User user) {
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        roleField.setText(user.getRole());
        // Avoid setting the password field for security reasons
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) throws SQLException {
        // Récupérer les données des champs
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String role = roleField.getText();


        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || role == null) {
            showAlert("Error","Les champs sont vides");
            return;
        }
        if (!isEmailValid(email))
        {
            showAlert("Error","Email est invalide");
            return;
        }
        if (nom.isBlank() || prenom.isBlank() || email.isBlank()) {
            showAlert("Error","Les champs sont des chaines vides");
            return;
        }
        currentUser = SessionManager.getCurrentUser();
        System.out.println("Current user ID: " + currentUser.getIdUser());

        // Créer un utilisateur mis à jour sans toucher au mot de passe
        User updatedUser = new User(nom, prenom, email, role, currentUser.getMdp()); // Utiliser le mot de passe actuel
        updatedUser.setIdUser(currentUser.getIdUser()); // S'assurer que l'ID est conservé pour la mise à jour
        System.out.println("Current user ID: " + currentUser.getIdUser());

        ServiceUser serviceUser = new ServiceUser();
        if (serviceUser.modifier(updatedUser)) {
            errorLabel.setText("Update successful!");
            // Optionnellement, naviguer vers une autre page ou afficher un message de succès
        } else {
            errorLabel.setText("Update failed. Please try again.");
        }
    }


    @FXML
    private void handleEditProfile(ActionEvent event) {
        // Handle edit profile action, e.g., reload the current scene or navigate to another
    }

    @FXML
    private void handleAdminDashboard(ActionEvent event) {
        // Handle admin dashboard action, e.g., navigate to the admin dashboard
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
