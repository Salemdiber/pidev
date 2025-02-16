package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.model.User;

public class ProfilController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    public void setUserDetails(User user) {
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        roleField.setText(user.getRole());
    }
}
