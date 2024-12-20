package cbp.oops;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.SQLException;

import java.sql.Statement;


class Person {
    String name, id;

    Person(String name, String id) {
        this.name = name;
        this.id = id;
    }
}

class Student extends Person {
	String course;
    int yr;
    int feePending;
    String password;
    String mobilenumber;
    String parentmobilenumber;

    Student(String name, int age, String id, String course, int yr, String password, String mobilenumber, String parentmobilenumber) {
        super(name, id);
        this.yr = yr;
        this.course = course;
        this.password = password;
        this.mobilenumber = mobilenumber;
        this.parentmobilenumber = parentmobilenumber;
        this.feePending = 0;

        String sqlQuery = String.format(
            "INSERT INTO student (id, name, course, year, feepending, password, room, mobilenumber, parentmobilenumber) " +
            "VALUES ('%s', '%s', '%s', %d, %d, '%s', null, '%s', '%s')",
            this.id, this.name, this.course, this.yr, this.feePending, this.password, this.mobilenumber, this.parentmobilenumber
        );
        ReadAndRemoveRows.addRow(sqlQuery);
    }
    public static void getDetails(String studentId) {
        String sqlQuery = String.format("select * from student where id = '%s'",studentId);
        PrintRows.printRows(sqlQuery);
    }

    public static void payFee(String id) {
        Scanner inp = new Scanner(System.in);
        int fees = 0;
        String query = String.format("SELECT feepending FROM student WHERE id = '%s'", id);

        try {
            Connection con = ConnectToServer.connectToServer();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                fees = Integer.parseInt(rs.getString(1));
            }

            System.out.println("Remaining fee is: " + fees);
            System.out.println("Enter amount");

            try {
                int paidAmount = inp.nextInt();
                if (paidAmount > fees) {
                    System.out.println("Amount cannot be more than the pending fee.");
                } else if (paidAmount <= 0) {
                    System.out.println("Amount should be positive.");
                } else {
                    // Update the student table to reflect the fee payment
                    String sqlQuery = String.format("UPDATE student SET feepending = feepending - %d WHERE id = '%s'", paidAmount, id);
                    ReadAndRemoveRows.updateRow(sqlQuery);
                    System.out.println("Fee paid.");

                    // Insert fee payment details into the fee table
                    String insertFeeQuery = "INSERT INTO fee (stid, amount_paid, payment_date) VALUES (?, ?, ?)";

                    // Prepare statement to insert fee payment details
                    PreparedStatement pstmt = con.prepareStatement(insertFeeQuery);

                    pstmt.setString(1, id);  // Set student ID
                    pstmt.setDouble(2, paidAmount);   // Set the fee amount
                    pstmt.setDate(3, java.sql.Date.valueOf("2024-11-01")); // Set the payment date

                    pstmt.executeUpdate();
                    System.out.println("Fee payment details added to the fee table.");

                    // Retrieve and display updated remaining fees
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int updatedFees = Integer.parseInt(rs.getString(1));
                        System.out.println("Updated remaining fee is: " + updatedFees);
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                inp.nextLine(); // Clear the scanner buffer
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changePassword(String id) {
        System.out.println("Enter new Password");
        Scanner inp = new Scanner(System.in);
        String pass = inp.nextLine();
        String sqlQuery = String.format("update student set password = '%s' where id = '%s'", pass, id);
        ReadAndRemoveRows.updateRow(sqlQuery);
        System.out.println("Password updated.");
    }
    public static void issueComplaint(String id) {
        System.out.println("Enter Complaint");
        Scanner inp = new Scanner(System.in);
        String complaint = inp.nextLine();
        System.out.println("Enter department");
        String department = inp.nextLine();

        // Correctly format the SQL query, handling boolean and passing all variables
        String sqlQuery = String.format(
            "INSERT INTO complaint (cname, cstatus, department, Student_id) VALUES ('%s', false, '%s', '%s')",
            complaint, department, id
        );
        ReadAndRemoveRows.updateRow(sqlQuery);
        System.out.println("Complaint issued.");
    }

    public static void getComplaintStatus(String studentId) {
        String query = String.format("SELECT cname, cstatus FROM complaint WHERE Student_id = '%s'", studentId);

        try (Connection con = ConnectToServer.connectToServer();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            boolean foundComplaints = false;

            System.out.println("Complaints placed by student (ID: " + studentId + "):");
            while (rs.next()) {
                foundComplaints = true;
                String complaintName = rs.getString("cname");
                boolean status = rs.getBoolean("cstatus");

                System.out.println("- Complaint: " + complaintName + " | Status: " + (status ? "Resolved" : "Not Resolved"));
            }

            if (!foundComplaints) {
                System.out.println("No complaints found for the student (ID: " + studentId + ").");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while fetching the complaints for the student.");
            e.printStackTrace();
        }
    }

}

class ComplaintResolver extends Thread {
    private final String id;

    public ComplaintResolver(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            // Simulate some work or delay (if necessary)
            Thread.sleep(5000); // You can adjust the sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String query = String.format(
                "UPDATE complaint SET cstatus = true WHERE Student_id = '%s' AND cstatus = false",
                id
        );

        try (Connection con = ConnectToServer.connectToServer();
             Statement stmt = con.createStatement()) {

            int rowsUpdated = stmt.executeUpdate(query);

            if (rowsUpdated > 0) {
                System.out.println("Complaint(s) for ID '" + id + "' resolved by " + Thread.currentThread().getName());
            } else {
                System.out.println("No unresolved complaints for ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error resolving complaint(s) for ID: " + id);
            e.printStackTrace();
        }
    }
}

class Admin extends Person {
    private String name;

    Admin(String name, String id) {
        super(name, id);
    }

    public static void insertStudent() {
        Scanner inp = new Scanner(System.in);

        System.out.println("Enter student id: ");
        String sid = inp.nextLine();

        System.out.println("Enter Student name: ");
        String sname = inp.nextLine();

        System.out.println("Enter course: ");
        String scourse = inp.nextLine();

        System.out.println("Enter year: ");
        int year = inp.nextInt();
        inp.nextLine(); // Clear the newline

        System.out.println("Enter mobile number (10 digits): ");
        String mobilenumber = inp.nextLine();

        System.out.println("Enter parent's mobile number (10 digits): ");
        String parentmobilenumber = inp.nextLine();

        System.out.println("Enter room preference: (1: Single Room, 2: Two Sharing, 3: Three Sharing, 4: Four Sharing)");
        int rp = inp.nextInt();
        inp.nextLine(); // Clear the newline

        String rno = null;
        String query = String.format("SELECT rno FROM room WHERE rsize = %d AND vacancies > 0", rp);

        try {
            Connection con = ConnectToServer.connectToServer();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                rno = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rno != null) {
            int roomFee = switch (rp) {
                case 1 -> 10000;
                case 2 -> 9000;
                case 3 -> 8000;
                case 4 -> 7000;
                default -> {
                    System.out.println(rp + " sharing not available.");
                    yield 0;
                }
            };

            String insertQuery = String.format(
                "INSERT INTO student (id, name, course, year, feepending, password, room, mobilenumber, parentmobilenumber) " +
                "VALUES ('%s', '%s', '%s', %d, %d, '%s', %d, '%s', '%s')",
                sid, sname, scourse, year, roomFee, sid, Integer.parseInt(rno), mobilenumber, parentmobilenumber
            );
            String updateQuery = String.format("UPDATE room SET vacancies = vacancies - 1 WHERE rno = %d", Integer.parseInt(rno));

            try (Connection con = ConnectToServer.connectToServer()) {
                con.setAutoCommit(false);

                try (Statement stmt1 = con.createStatement();
                     Statement stmt2 = con.createStatement()) {

                    stmt1.executeUpdate(insertQuery);
                    stmt2.executeUpdate(updateQuery);

                    con.commit();
                    System.out.println("Student added and room allocated.");
                } catch (SQLException e) {
                    con.rollback();
                    e.printStackTrace();
                    System.out.println("Error adding student or updating room. Transaction rolled back.");
                }

                query = "SELECT * FROM student ORDER BY id";
                System.out.println("Student Added.");
                PrintRows.printRows(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database connection error.");
            }

        } else {
            System.out.println("No rooms available as per your preference.");
        }
    }

    public static void removeStudent() {
        System.out.println("Enter student id: ");
        Scanner inp = new Scanner(System.in);
        String sid = inp.nextLine();
        String query = String.format("update room set vacancies = vacancies + 1 where rno = (select room from student where id = '%s')", sid);
        ReadAndRemoveRows.updateRow(query);
        query = String.format("delete from student where id = '%s'",sid);
        ReadAndRemoveRows.removeRow(query);
        query = "select * from student order by id";
        System.out.println("Student Removed.");
        PrintRows.printRows(query);
    }

    public static void availableSlots() {
        String query = "select rno, vacancies from room where vacancies != 0 order by rno";
        PrintRows.printRows(query);
    }
    public static void feePendingList() {
        String query = "select id, name, course, feepending from student where feepending != 0 order by id";
        PrintRows.printRows(query);
    }
    public static void resolveComplaints() {
        Scanner inp = new Scanner(System.in);
        String query = "select * from complaint where cstatus='false'";
        PrintRows.printRows(query);
        System.out.println("Enter Student ID: ");
        String id = inp.nextLine();
        ComplaintResolver resolver = new ComplaintResolver(id);
        resolver.setName("ComplaintResolver");
        resolver.start();
    }
    public static void viewRoomDetails() {
        System.out.println("Enter room number: ");
        Scanner inp = new Scanner(System.in);
        int rno = inp.nextInt();
        String query = String.format("select * from student where room = %d order by id",rno);
        PrintRows.printRows(query);
    }
    public static void getAllStudents() {
        String sqlQuery = "select * from student order by id";
        PrintRows.printRows(sqlQuery);
    }
}

class Room {
    int roomNumber;
    int capacity;
    int roomFee;

    int studentCount = 0;

    Room(int roomNumber, int capacity, int roomFee) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomFee = roomFee;
    }
}

public class HostelManagementSystem {

    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        System.out.println("+******************************************+");
        System.out.println("|               MENU FOR HOSTEL            |");
        System.out.println("+******************************************+");
        int n = 0;
        boolean cont = true;
        do {
        	System.out.println("+******************************************+");
            System.out.println("|           Enter 1 for Student            |");
            System.out.println("|           Enter 2 for Admin              |");
            System.out.println("|           Enter 3 for Exit               |");
            System.out.println("+******************************************+");
            System.out.print("Enter your choice: ");	
            int user = inp.nextInt();
            switch (user) {
                case 1:
                    IsStudent.forStudent();
                    break;
                case 2:
                    IsAdmin.forAdmin();
                    break;
                case 3: cont = false;
                    break;
                default:
                    System.out.println("Wrong choice");
            }
        }while(cont);
    }
}

