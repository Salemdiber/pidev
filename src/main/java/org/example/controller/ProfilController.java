package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.example.model.SessionManager;
import org.example.model.User;

import java.io.IOException;
import java.util.Objects;

public class ProfilController {

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
            // displayUserImage(currentUser);

        }
    }

    public void setUserDetails(User currentUser) {
        System.out.println("User is logged in: " + SessionManager.getCurrentUser().getEmail());

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

}
