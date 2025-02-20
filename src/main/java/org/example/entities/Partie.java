package org.example.entities;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Partie {
    private int idMatch;
    private TypeMatch type;
    private String resultat;
    private List<Equipe> equipes = new ArrayList<>();


    public Partie(int idMatch, TypeMatch type, String resultat) {
        this.idMatch = idMatch;
        this.type = type;
        this.resultat = resultat;

    }


    public Partie(int idMatch, TypeMatch type, String resultat, List<Equipe> equipes) {
        this.idMatch = idMatch;
        this.type = type;
        this.resultat = resultat;

        this.equipes = equipes;
    }


    public Partie(TypeMatch type, String resultat) {
        this.type = type;
        this.resultat = resultat;

    }

    public Partie(TypeMatch type, String resultat, List<Equipe> listequipe) {
        this.type = type;
        this.resultat = resultat;
        this.equipes.addAll(listequipe);
    }

    public Partie() {

    }


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

    public List<Equipe> getEquipes() {
        return equipes;
    }



    public void setType(TypeMatch type) {
        this.type = type;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }


    public void ajouterEquipe(Equipe equipe) {
        if (!equipes.contains(equipe)) {
            equipes.add(equipe);
            equipe.ajouterMatch(this);
        }
    }

    @Override
    public String toString() {
        return "Partie{" +
                "idMatch=" + idMatch +
                ", type=" + type +
                ", resultat='" + resultat + '\'' +
                '}';
    }
}