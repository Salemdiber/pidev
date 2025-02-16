package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AdminController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    public void setUserDetails(String nom, String prenom, String email, String role) {
        nomField.setText(nom);
        prenomField.setText(prenom);
        emailField.setText(email);
        roleField.setText(role);
    }
}
