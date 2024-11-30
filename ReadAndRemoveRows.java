package cbp.oops;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadAndRemoveRows{
    public static void addRow(String sqlQuery) {
        try (Connection con = ConnectToServer.connectToServer()){
            if (con == null) {
                System.out.println("Connection was not established");
            } else {
                Statement st = con.createStatement();
                st.executeUpdate(sqlQuery);
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeRow(String sqlQuery) {
        try (Connection con = ConnectToServer.connectToServer()) {
            if (con == null) {
                System.out.println("Connection was not established");
            }
            else {
                try {
                    Statement st = con.createStatement();
                    st.executeUpdate(sqlQuery);
                } catch (SQLException | NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateRow(String sqlQuery) {
        try (Connection con = ConnectToServer.connectToServer()) {
            if (con == null) {
                System.out.println("Connection was not established");
            }
            else {
                try {
                    Statement st = con.createStatement();
                    st.executeUpdate(sqlQuery);
                } catch (SQLException | NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
}