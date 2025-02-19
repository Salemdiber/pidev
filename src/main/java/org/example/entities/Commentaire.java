package org.example.entities;

public class Commentaire {
    private int id;
    private String desc;
    private int idUser=1;
    private int idPub;
    private String nom; // New field to store user's name

    public Commentaire(int id, String desc, int idUser, int idPub, String nom) {
        this.id = id;
        this.desc = desc;
        this.idUser = idUser;
        this.idPub = idPub;
        this.nom = nom; // Store user’s name
    }

    public Commentaire(String desc, int idUser, int idPub, String nom) {
        this.desc = desc;
        this.idUser = idUser;
        this.idPub = idPub;
        this.nom = nom; // Store user’s name
    }

    public int getId() { return id; }
    public String getDesc() { return desc; }
    public int getIdUser() { return idUser; }
    public int getIdPub() { return idPub; }
    public String getNom() { return nom; } // Getter for user's name

    public void setId(int id) { this.id = id; }
    public void setDesc(String desc) { this.desc = desc; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
    public void setIdPub(int idPub) { this.idPub = idPub; }
    public void setNom(String nom) { this.nom = nom; } // Setter for user's name

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", idUser=" + idUser +
                ", idPub=" + idPub +
                ", nom='" + nom + '\'' +
                '}';
    }
}