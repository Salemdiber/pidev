package org.example.entities;

public class participant {
    public int id_part,id_user,id_event;

    public participant(int id_user, int id_event) {
        this.id_user = id_user;
        this.id_event = id_event;
    }
    public participant(int id_part, int id_user, int id_event) {
        this.id_part = id_part;
        this.id_user = id_user;
        this.id_event = id_event;
    }

    public int getId_part() {
        return id_part;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_part(int id_part) {
        this.id_part = id_part;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    @Override
    public String toString() {
        return "participant{" +
                "id_part=" + id_part +
                ", id_user=" + id_user +
                ", id_event=" + id_event +
                '}';
    }
}
