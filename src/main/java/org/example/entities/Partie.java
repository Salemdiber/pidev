package org.example.entities;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Partie {
    private int idMatch;
    private TypeMatch type; // Changement de String à TypeMatch
    private String resultat;
    private String lieu;
    private List<Equipe> equipes = new ArrayList<>(); // Liste des équipes participant à la partie

    // Constructeur avec Enum TypeMatch
    public Partie(int idMatch, TypeMatch type, String resultat, String lieu) {
        this.idMatch = idMatch;
        this.type = type;
        this.resultat = resultat;
        this.lieu = lieu;
    }


    public Partie(int idMatch, TypeMatch type, String resultat, String lieu, List<Equipe> equipes) {
        this.idMatch = idMatch;
        this.type = type;
        this.resultat = resultat;
        this.lieu = lieu;
        this.equipes = equipes;
    }

    // Constructeur avec Enum TypeMatch
    public Partie(TypeMatch type, String resultat, String lieu) {
        this.type = type;
        this.resultat = resultat;
        this.lieu = lieu;
    }

    public Partie(TypeMatch type, String resultat, String place, List<Equipe> listequipe) {
        this.type = type;
        this.resultat = resultat;
        this.lieu = place;
        this.equipes.addAll(listequipe);
    }

    public Partie() {

    }

    // Getters
    public int getIdMatch() {
        return idMatch;
    }

    public TypeMatch getType() {
        return type; // Retourne l'Enum
    }

    public String getResultat() {
        return resultat;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }

    public String getLieu() {
        return lieu;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }


    // Setters
    public void setType(TypeMatch type) {
        this.type = type; // Accepte l'Enum TypeMatch
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    // Ajouter une équipe au match
    public void ajouterEquipe(Equipe equipe) {
        if (!equipes.contains(equipe)) {
            equipes.add(equipe);
            equipe.ajouterMatch(this); // Mise à jour bidirectionnelle
        }
    }

    @Override
    public String toString() {
        return "Partie{" +
                "idMatch=" + idMatch +
                ", type=" + type + // Affichage de l'Enum
                ", resultat='" + resultat + '\'' +
                ", lieu='" + lieu + '\'' +
                '}';
    }
}