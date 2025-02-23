package org.example.controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PhoneNumberInputController {


    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "2c778d99f0b3add3f31ceac5c87415dd";
    public static final String TWILIO_PHONE_NUMBER = "+18575984871";

    @FXML
    private TextField phoneNumberField;
    private String reservationDetails;
    public void setDetails(String userName) {

        this.reservationDetails = reservationDetails;
    }
    @FXML
    private void handleSend() {
        String phoneNumber = phoneNumberField.getText();

        // Validate the phone number
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            afficherAlerte(AlertType.WARNING, "Avertissement", "Veuillez entrer un numéro de téléphone.");
            return;
        }

        // Create a personalized message
        String messageBody = createMessageBody();

        // Send the SMS
        try {
            sendSms(phoneNumber, messageBody);
            afficherAlerte(AlertType.INFORMATION, "Succès", "SMS envoyé à " + phoneNumber + " !");
        } catch (Exception e) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Impossible d'envoyer le SMS : " + e.getMessage());
        }
    }

    private String createMessageBody() {
        // Customize the message body
        return "Hello"  + ",\n\n" + reservationDetails + "\n\nMerci pour votre réservation !";
    }
    private void sendSms(String toPhoneNumber, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber), // To number
                new PhoneNumber(TWILIO_PHONE_NUMBER), // From number
                messageBody // Message body
        ).create();

        System.out.println("SMS sent: " + message.getSid());
    }
    private void afficherAlerte(AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}