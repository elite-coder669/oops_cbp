package cbp.oops;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class IsAdmin{
    public static boolean authenticate(String un, String pa){
        if ((un.compareTo("admin") == 0)&& (pa.compareTo("root") == 0)) {
            return true;
        }
        return false;
    }

    public static void forAdmin() {
        Scanner inp = new Scanner(System.in);
        System.out.println("Enter userName: ");
        String un = inp.nextLine();
        System.out.println("Enter password");
        String pa = inp.nextLine();
        adminMenu(un, pa);
    }
    public static void printMenu() {
        System.out.println("+******************************************+");
        System.out.println("|               MENU FOR ADMIN             |");
        System.out.println("|   Enter 1 to Insert a Students           |");
        System.out.println("|   Enter 2 to Remove a Students           |");
        System.out.println("|   Enter 3 to Fetch Available Slots       |");
        System.out.println("|   Enter 4 to Fetch Fee Pending List      |");
        System.out.println("|   Enter 5 to Resolve Complaints          |");
        System.out.println("|   Enter 6 to Display Students in a Room  |");
        System.out.println("|   Enter 7 to Display All Students        |");
        System.out.println("|   Enter 8 to Exit                        |");
        System.out.println("+******************************************+");
    }
    public static void adminMenu(String un, String pa) {
        boolean cont = true;
        int n = 0;
        Scanner inp = new Scanner(System.in);

        if (authenticate(un, pa)) {
            System.out.println("+******************************************+");
            System.out.println("|           MENU FOR HOSTEL ADMIN          |");
            System.out.println("+******************************************+");
            do {
                printMenu();
                System.out.println("Enter your Choice: ");

                // Validate input
                if (!inp.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 8.");
                    inp.next(); // Clear invalid input
                    continue;
                }

                n = inp.nextInt();
                inp.nextLine(); // Consume the newline character

                switch (n) {
                    case 1:
                        Admin.insertStudent();
                        break;
                    case 2:
                        Admin.removeStudent();
                        break;
                    case 3:
                        Admin.availableSlots();
                        break;
                    case 4:
                        Admin.feePendingList();
                        break;
                    case 5:
                        Admin.resolveComplaints();
                        break;
                    case 6:
                        Admin.viewRoomDetails();
                        break;
                    case 7:
                        Admin.getAllStudents();
                        break;
                    case 8:
                        cont = false;
                        System.out.println("Exiting Admin Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 8.");
                }

                if (cont) {
                    System.out.println("Continue y/n");
                    String conti = inp.nextLine();
                    if (conti.equalsIgnoreCase("n")) {
                        cont = false;
                    }
                }

            } while (cont);
        } else {
            System.out.println("Authentication failed. Please check your username and password.");
        }
    }
}