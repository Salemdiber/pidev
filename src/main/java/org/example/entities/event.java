package org.example.entities;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class event {

    String nom,image,desc,lieu;
    int id_event;
    LocalDateTime date;

    public event(String nom, String image, String desc,String lieu,LocalDateTime date) {
        this.nom = nom;
        this.image = image;
        this.desc = desc;
        this.lieu = lieu;
        this.date = date;
    }


    public event(int id,String nom, String image, String desc,String lieu,LocalDateTime date) {
        this.nom = nom;
        this.image = image;
        this.desc = desc;
        this.id_event = id;
        this.lieu = lieu;
        this.date = date;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;

    }
    public String getImage() {
        return image;

    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public int getId_event() {
        return id_event;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "event{" +
                "id_event=" + id_event +
                ",nom='" + nom + '\'' +
                ", image='" + image + '\'' +
                ", desc='" + desc + '\'' +
                ", lieu='" + lieu + '\'' +
                ", date=" + date +
                '}';
    }
}
