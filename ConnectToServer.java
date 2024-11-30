package cbp.oops;

import java.sql.*;
import java.sql.DriverManager;

public class ConnectToServer {
    public static Connection connectToServer(){
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String pass = "root";
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, username, pass);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return con;
    }
}