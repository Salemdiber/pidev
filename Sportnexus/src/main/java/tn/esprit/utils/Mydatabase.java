package tn.esprit.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mydatabase {
    private final String URL ="jdbc:mysql://localhost:3306/sportnexus";
    private final String USER ="root";
    private final String PWD ="";
    private static Mydatabase instance;

    private static Connection connection;

    public Mydatabase() {
        try{
            connection = DriverManager.getConnection(URL,USER,PWD);
            System.out.println("connected");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    public static Mydatabase getInstance() {
        if (instance == null) {
            instance = new Mydatabase();
        }
        return instance;
    }


}

