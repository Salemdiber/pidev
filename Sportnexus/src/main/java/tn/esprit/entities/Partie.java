package tn.esprit.entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Partie {
    private int idMatch;
    private TypeMatch type; // Changement de String à TypeMatch
    private String resultat;
    private Date dateMatch;
    private String lieu;
    private List<Equipe> equipes = new ArrayList<>(); // Liste des équipes participant à la partie

    // Constructeur avec Enum TypeMatch
    public Partie(int idMatch, TypeMatch type, String resultat, Date dateMatch, String lieu) {
        this.idMatch = idMatch;
        this.type = type;
        this.resultat = resultat;
        this.dateMatch = dateMatch;
        this.lieu = lieu;
    }

    // Constructeur avec Enum TypeMatch
    public Partie(TypeMatch type, String resultat, Date dateMatch, String lieu) {
        this.type = type;
        this.resultat = resultat;
        this.dateMatch = dateMatch;
        this.lieu = lieu;
    }

    public Partie(TypeMatch type, String resultat, java.sql.Date dateMatch, String place, List<Equipe> listequipe) {
        this.type = type;
        this.resultat = resultat;
        this.dateMatch = dateMatch;
        this.lieu = place;
        this.equipes.addAll(listequipe);
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

    public Date getDateMatch() {
        return dateMatch;
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

    public void setDateMatch(Date dateMatch) {
        this.dateMatch = dateMatch;
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
                ", dateMatch=" + dateMatch +
                ", lieu='" + lieu + '\'' +
                '}';
    }
}

