package entities;

public class Commentaire {
    private int id;
    private String desc;
    private int idUser;
    private int idPub;
    public Commentaire(String desc, int idUser, int idPub)
    {
        this.desc = desc;
        this.idUser = idUser;
        this.idPub = idPub;
    }
    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdPub() {
        return idPub;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setIdPub(int idPub) {
        this.idPub = idPub;
    }
    public Commentaire(int id, String desc, int idUser, int idPub) {
        this.id = id;
        this.desc = desc;
        this.idUser = idUser;
        this.idPub = idPub;
    }
    @Override
    public String toString() {
        return id + " " + desc + " " + idUser + " " + idPub;
    }
}
