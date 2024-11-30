package cbp.oops;

import java.sql.*;
public class PrintRows{
    public static void printTable(ResultSet rs, ResultSetMetaData rsMd) {
        try {
            int columnCount = rsMd.getColumnCount();
            int tableWidth = 15;
            System.out.print("+");
            for (int i = 0; i < (tableWidth + 2) * columnCount + columnCount-1; i++) {
                System.out.print("-");
            }
            System.out.println("+");
            System.out.print("| ");
            for (int col = 1; col < columnCount + 1; col++) {
                System.out.print(rsMd.getColumnName(col));
                for (int i = 0; i < tableWidth - rsMd.getColumnName(col).length(); i++) {
                    System.out.print(" ");
                }
                System.out.print(" | ");
            }
            System.out.println();
            System.out.print("+");
            for (int i = 0; i < (tableWidth + 2) * columnCount + columnCount-1; i++) {
                System.out.print("_");
            }
            System.out.println("+");

            while (rs.next()) {
                System.out.print("| ");
                for (int col = 1; col < columnCount + 1; col++) {
                    System.out.print(rs.getString(col));
                    if (rs.getString(col) != null) {
                        for (int i = 0; i < tableWidth - rs.getString(col).length(); i++) {
                            System.out.print(" ");
                        }
                    }
                    else {
                        for (int i = 0; i < tableWidth - 4; i++) {
                            System.out.print(" ");
                        }
                    }
                    System.out.print(" | ");
                }
                System.out.println();
            }
            System.out.print("|");
            for (int i = 0; i < (tableWidth + 2) * columnCount + columnCount-1; i++) {
                System.out.print("_");
            }
            System.out.println("|");

        }catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void printRows(String sqlQuery) {
        try (Connection con = ConnectToServer.connectToServer()){
            if (con == null) {
                System.out.println("Connection was not Established");
            }
            else {
                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlQuery);
                    ResultSetMetaData rsMd = rs.getMetaData();
                    printTable(rs, rsMd);

                } catch (NullPointerException | SQLException e) {
                    System.out.println("ERROR:");
                    System.out.println(e.getMessage());
                }
            }
        } catch(NullPointerException | SQLException e ){
            System.out.println("ERROR: In PrintStatement ");
            System.out.println(e.getMessage());
        }
    }
}