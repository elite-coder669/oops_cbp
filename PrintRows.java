package cbp.oops;

import java.sql.*;

public class PrintRows {
    public static void printTable(ResultSet rs, ResultSetMetaData rsMd) {
        try {
            int columnCount = rsMd.getColumnCount();
            int[] columnWidths = new int[columnCount];

            for (int col = 1; col <= columnCount; col++) {
                columnWidths[col - 1] = Math.max(rsMd.getColumnName(col).length(), 15);
            }

            while (rs.next()) {
                for (int col = 1; col <= columnCount; col++) {
                    String value = rs.getString(col);
                    if (value != null) {
                        columnWidths[col - 1] = Math.max(columnWidths[col - 1], value.length());
                    }
                }
            }
            rs.beforeFirst();

            int totalWidth = 1;
            for (int width : columnWidths) {
                totalWidth += width + 3;
            }

            printLine(totalWidth, '+', '-');
            printRow(columnWidths, rsMd, null);
            printLine(totalWidth, '+', '-');

            while (rs.next()) {
                printRow(columnWidths, rsMd, rs);
            }
            printLine(totalWidth, '+', '-');
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printLine(int width, char edge, char fill) {
        System.out.print(edge);
        for (int i = 0; i < width - 2; i++) {
            System.out.print(fill);
        }
        System.out.println(edge);
    }

    private static void printRow(int[] columnWidths, ResultSetMetaData rsMd, ResultSet rs) throws SQLException {
        System.out.print("| ");
        for (int col = 1; col <= columnWidths.length; col++) {
            String value = rs == null ? rsMd.getColumnName(col) : rs.getString(col);
            value = value == null ? "NULL" : value;
            System.out.print(value);
            for (int i = value.length(); i < columnWidths[col - 1]; i++) {
                System.out.print(" ");
            }
            System.out.print(" | ");
        }
        System.out.println();
    }

    public static void printRows(String sqlQuery) {
        try (Connection con = ConnectToServer.connectToServer()) {
            if (con != null) {
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery(sqlQuery);
                ResultSetMetaData rsMd = rs.getMetaData();
                printTable(rs, rsMd);
            } else {
                System.out.println("Connection was not Established");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
