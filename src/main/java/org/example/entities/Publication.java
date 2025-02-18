package org.example.entities;

public class Publication {
    private int idPub;
    private String titre;
    private String describ;
    private int idUser;

    public Publication(int idPub, String titre, String describ, int idUser) {
        this.idPub = idPub;
        this.titre = titre;
        this.describ = describ;
        this.idUser = idUser;
    }

    public Publication(String titre, String describ, int idUser) {
        this.titre = titre;
        this.describ = describ;
        this.idUser = idUser;
    }
    public Publication(String titre, String describ) {
        this.titre = titre;
        this.describ = describ;
    }

    public Publication() {
    }

    public int getIdPub() {
        return idPub;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescrib() {
        return describ;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdPub(int idPub) {
        this.idPub = idPub;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "idPub=" + idPub +
                ", titre='" + titre + '\'' +
                ", describ='" + describ + '\'' +
                ", idUser=" + idUser +
                '}';
    }
}
