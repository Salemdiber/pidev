package tn.esprit.entities;

public class User {
    private int idUser;
    private String nom;
    private Role role;

    public enum Role {
        ENTRAINEUR, JOUEUR, ADMIN, AUTRE;

        public static Role fromString(String role) {
            try {
                return Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return AUTRE;
            }
        }
    }

    public User(int idUser, String nom, Role role) {
        this.idUser = idUser;
        this.nom = nom;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nom='" + nom + '\'' +
                ", role=" + role +
                '}';
    }
}

