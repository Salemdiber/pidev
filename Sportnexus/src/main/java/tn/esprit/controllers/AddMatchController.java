package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import tn.esprit.entities.Equipe;
import tn.esprit.entities.Partie;
import tn.esprit.entities.TypeMatch;
import tn.esprit.services.ServiceMatch;
import tn.esprit.utils.Mydatabase;

import java.sql.*;
import java.util.List;

public class AddMatchController {
    @javafx.fxml.FXML
    private Pane AddMatchpane;
    @javafx.fxml.FXML
    private Button quitmatchbtn;
    @javafx.fxml.FXML
    private DatePicker datedatepicker;
    @javafx.fxml.FXML
    private Button addmatchbtn;
    private ObservableList<Equipe> equipesList = FXCollections.observableArrayList();
    @javafx.fxml.FXML
    private TextField resultxtfield;
    @javafx.fxml.FXML
    private ComboBox<String> placecombobox; // Spécifiez le type pour le ComboBox
    @javafx.fxml.FXML
    private ComboBox<TypeMatch> typecombobox;

    private final ServiceMatch serviceMatch = new ServiceMatch();

    @javafx.fxml.FXML
    private ComboBox<String> cbteam1;

    @javafx.fxml.FXML
    private ComboBox<String> cbteam2;


    @javafx.fxml.FXML
    public void initialize() {
        typecombobox.setItems(FXCollections.observableArrayList(TypeMatch.values()));
        addmatchbtn.setOnAction(event -> ajouterMatch());
        chargerEquipes();
    }

//    private void chargerEquipes() {
//        equipesList.clear();
//        String req = "SELECT * FROM equipe"; // Remplace par ta requête correcte
//
//        try (Connection cnx = Mydatabase.getInstance().getConnection();
//             Statement st = cnx.createStatement();
//             ResultSet rs = st.executeQuery(req)) {
//            while (rs.next()) {
//                Equipe equipe = new Equipe(
//                        rs.getInt("id_equipe"),
//                        rs.getString("nom_equipe")
//                );
//                equipesList.add(equipe);
//            }
//            equipeListView.setItems(equipesList);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private void ajouterMatch() {
        // Récupération des valeurs des champs
        TypeMatch type = typecombobox.getValue();
        String resultat = resultxtfield.getText();
        Date date_match = java.sql.Date.valueOf(datedatepicker.getValue());
        String place = placecombobox.getValue();

        // Récupérer les équipes sélectionnées (elles sont de type String)
        String team1Name = cbteam1.getValue();
        String team2Name = cbteam2.getValue();

        // Vérification si un des champs est vide
        if (type == null || resultat.isEmpty() || date_match == null || place == null || team1Name == null || team2Name == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        try {
            // Créer un nouvel objet Match
            Partie match = new Partie(type, resultat, date_match, place);
            // Enregistrer le match en base de données
            serviceMatch.ajouter(match);

            // Récupérer les IDs des équipes sélectionnées
            Equipe team1 = getEquipeByName(team1Name);
            Equipe team2 = getEquipeByName(team2Name);

            // Ajouter les équipes au match
            serviceMatch.ajouterEquipeAMatch(match.getIdMatch(), team1.getIdEquipe());
            serviceMatch.ajouterEquipeAMatch(match.getIdMatch(), team2.getIdEquipe());

            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Match ajouté avec succès !");
            clearFields();

        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le match : " + e.getMessage());
        }
    }

    private void clearFields() {
        typecombobox.setValue(null); // Réinitialiser le ComboBox
        resultxtfield.clear(); // Effacer le texte du TextField
        datedatepicker.setValue(null); // Réinitialiser le DatePicker
        placecombobox.setValue(null); // Réinitialiser un autre ComboBox

    }
    private Equipe getEquipeByName(String name) {
        for (Equipe equipe : equipesList) {
            if (equipe.getNom().equals(name)) {
                return equipe;
            }
        }
        return null; // Retourne null si aucune équipe n'a été trouvée
    }
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @javafx.fxml.FXML
    public void quitter(Event event) {
        // Logique pour quitter ou fermer la fenêtre
        // Par exemple, vous pouvez fermer la fenêtre actuelle

    }
    private void chargerEquipes() {
        equipesList.clear();
        String req = "SELECT * FROM equipe"; // Remplace par ta requête correcte

        try (Connection cnx = Mydatabase.getInstance().getConnection();
             Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Equipe equipe = new Equipe(
                        rs.getInt("id_equipe"),
                        rs.getString("nom_equipe")
                );
                equipesList.add(equipe);
            }

            // Peupler les ComboBoxes avec les noms des équipes (String)
            ObservableList<String> equipeNames = FXCollections.observableArrayList();
            for (Equipe equipe : equipesList) {
                equipeNames.add(equipe.getNom());
            }

            cbteam1.setItems(equipeNames);
            cbteam2.setItems(equipeNames);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}