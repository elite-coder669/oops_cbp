package cbp.oops;

import java.sql.*;
import java.util.Scanner;

public class IsStudent {

    // Authentication method for the student
    public static boolean authenticate(String username, String inputPassword) {
        String query = "SELECT password FROM public.student WHERE id = ?";
        try (Connection con = ConnectToServer.connectToServer();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(inputPassword);
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication:");
            e.printStackTrace();
        }
        return false; // Authentication failed
    }

    public static void forStudent() {
        Scanner inp = new Scanner(System.in);
        System.out.println("Enter userName: ");
        String un = inp.nextLine();
        System.out.println("Enter password");
        String pa = inp.nextLine();
        studentMenu(un, pa);
    }

    public static void printStudentMenu() {
        System.out.println("+******************************************+");
        System.out.println("|              MENU FOR STUDENT            |");
        System.out.println("|   Enter 1 for Issuing Complaint          |");
        System.out.println("|   Enter 2 for Paying Fee                 |");
        System.out.println("|   Enter 3 for Changing Password          |");
        System.out.println("|   Enter 4 to Check Complaint Status      |");
        System.out.println("|   Enter 5 to Exit                        |");
        System.out.println("+******************************************+");
    }

    public static void studentMenu(String un, String pa) {
        boolean cont = true;
        int n = 0;
        Scanner inp = new Scanner(System.in);

        if (authenticate(un, pa)) {
            System.out.println("MENU FOR STUDENT");
            do {
                printStudentMenu();
                System.out.println("Enter your Choice: ");
                
                // Validate input
                if (!inp.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    inp.next(); // Clear invalid input
                    continue;
                }

                n = inp.nextInt();
                inp.nextLine(); // Consume newline character

                switch (n) {
                    case 1:
                        Student.issueComplaint(un);
                        break;
                    case 2:
                        Student.payFee(un);
                        break;
                    case 3:
                        Student.changePassword(un);
                        break;
                    case 4:
                        Student.getComplaintStatus(un);
                        break;
                    case 5:
                        cont = false;
                        System.out.println("Exiting Student Menu...");
                        break;
                    default:
                        System.out.println("Wrong choice. Please select a number between 1 and 5.");
                }
            } while (cont);
        } else {
            System.out.println("Authentication failed. Please check your username and password.");
        }
    }
}
