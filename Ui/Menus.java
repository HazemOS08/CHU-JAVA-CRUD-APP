package Ui;

import java.util.Scanner;

import DAO.UserDAO;
import DAO.DAO_Implementation.UserDAOImp;
import Service_Layer.Controllers.AvailabilityController;
import Service_Layer.Controllers.DepartmentController;
import Service_Layer.Controllers.PositionsController;
import Service_Layer.Controllers.RequestController;
import Service_Layer.Controllers.ScheduleController;
import Service_Layer.Controllers.UserController;
import Service_Layer.Controllers.WorksController;
import Service_Layer.Viewers.AvailabilityViewer;
import Service_Layer.Viewers.PositionsViewer;
import Service_Layer.Viewers.RequestsViewer;
import Service_Layer.Viewers.ScheduleViewer;
import Service_Layer.Viewers.UserViewer;
import Service_Layer.Viewers.WorksViewer;
import Table_Classes.User;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;
import Utilities.PasswordRulesCheck;

public class Menus {

    //Controllers
    private static final UserController userCtrl = new UserController();
    private static final DepartmentController deptCtrl = new DepartmentController();
    private static final PositionsController posCtrl = new PositionsController();
    private static final ScheduleController schedCtrl = new ScheduleController();
    private static final WorksController worksCtrl = new WorksController();
    private static final AvailabilityController availCtrl = new AvailabilityController();
    private static final RequestController reqCtrl = new RequestController();
    
    //Viewers
    private static final UserViewer userView = new UserViewer();
    private static final PositionsViewer posView = new PositionsViewer();
    private static final WorksViewer worksView = new WorksViewer();
    private static final ScheduleViewer schedView = new ScheduleViewer();
    private static final AvailabilityViewer availView = new AvailabilityViewer();
    private static final RequestsViewer reqView = new RequestsViewer();

     private static final UserDAO userDAO = new UserDAOImp();


     public static User login(Scanner scanner) {

       

        ConsoleFormatter.printHeader("Welcome to CHU SAINTE JUSTINE Employee Portal");

        while (true){

            System.out.println("\n1. Log In");

            System.out.println("0. Exit Application");
            
            int choice = InputUtils.readInt(scanner, "Select an option: ");

            if (choice == 0){
                System.out.println("Exiting system. Goodbye!");
                return null;
            }

            if (choice == 1){

                String username = InputUtils.readString(scanner, "Enter Username: ");
                String Password = InputUtils.readString(scanner, "Enter Password: ");

                
                User user = userDAO.findByUsername(username);

                
                String hashedPasswordAttempt = PasswordRulesCheck.hashPassword(Password.trim());

                if (user != null && user.getPassword().equals(hashedPasswordAttempt)) {

                    System.out.println("\nLogin successful! Welcome, " 

                                       + user.getFirstName() + " " + user.getLastName());

                    return user;

                } else{
                    System.out.println("\n[!] Invalid username or password. Please try again.");
                }
            } else{
                System.out.println("\n[!] Invalid selection. Enter 1 or 0.");
        }

       }

    }
     
    private static int promptAccessMode(Scanner scanner) {

        System.out.println("\n--- Choose Access Mode ---");
        System.out.println("1. View Only");
        System.out.println("2. Modify / Manage");
        System.out.println("0. Back");

        return InputUtils.readInt(scanner, "Select Mode: ");

    }

    public static void runAdminMenu(Scanner scanner, User currentUser) {
        while (true) {
            ConsoleFormatter.printHeader("Admin Menu");
            System.out.println("1. User Account Management");
            System.out.println("2. Department Management");
            System.out.println("3. Positions Management");
            System.out.println("4. Works & Assignments Management");
            System.out.println("5. Schedule & Shift Management");
            System.out.println("6. Availability Management");
            System.out.println("7. Request Management");
            System.out.println("0. Log Out");

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            switch (choice) {
                case 1 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) userView.runUserViewer(scanner, currentUser);
                    else if (mode == 2) userCtrl.runUserController(scanner, currentUser);
                }
                case 2 -> deptCtrl.runDepartmentController(scanner, currentUser);
                case 3 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) posView.runPositionsViewer(scanner, currentUser);
                    else if (mode == 2) posCtrl.runPositionsController(scanner, currentUser);
                }
                case 4 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) worksView.runWorksViewer(scanner, currentUser);
                    else if (mode == 2) worksCtrl.runWorksController(scanner, currentUser);
                }
                case 5 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) schedView.runScheduleMenu(scanner, currentUser);
                    else if (mode == 2) schedCtrl.runManagementMenu(scanner, currentUser);
                }
                case 6 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) availView.runAvailabilityViewer(scanner, currentUser);
                    else if (mode == 2) availCtrl.runAvailabilityController(scanner, currentUser);
                }
                case 7 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) reqView.runRequestViewer(scanner, currentUser);
                    else if (mode == 2) reqCtrl.runRequestController(scanner, currentUser);
                }
                case 0 -> {
                    System.out.println("\nLogging out...");
                    return;
                }
                default -> System.out.println("\n[!] Invalid selection. Try again.");
            }
        }
    }


    public static void runManagerMenu(Scanner scanner, User currentUser) {
        while (true) {
            ConsoleFormatter.printHeader("Manager Menu");
            System.out.println("1. My Profile & User Info");
            System.out.println("2. Positions Directory");
            System.out.println("3. Department Works & Assignments");
            System.out.println("4. Department Shifts & Schedules");
            System.out.println("5. Staff Availability");
            System.out.println("6. Employee Requests");
            System.out.println("0. Log Out");

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            switch (choice) {
                case 1 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) userView.runUserViewer(scanner, currentUser);
                    else if (mode == 2) userCtrl.runUserController(scanner, currentUser);
                }
                case 2 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) posView.runPositionsViewer(scanner, currentUser);
                    else if (mode == 2) posCtrl.runPositionsController(scanner, currentUser);
                }
                case 3 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) worksView.runWorksViewer(scanner, currentUser);
                    else if (mode == 2) worksCtrl.runWorksController(scanner, currentUser);
                }
                case 4 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) schedView.runScheduleMenu(scanner, currentUser);
                    else if (mode == 2) schedCtrl.runManagementMenu(scanner, currentUser);
                }
                case 5 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) availView.runAvailabilityViewer(scanner, currentUser);
                    else if (mode == 2) availCtrl.runAvailabilityController(scanner, currentUser);
                }
                case 6 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) reqView.runRequestViewer(scanner, currentUser);
                    else if (mode == 2) reqCtrl.runRequestController(scanner, currentUser);
                }
                case 0 -> {
                    System.out.println("\nLogging out...");
                    return;
                }
                default -> System.out.println("\n[!] Invalid selection. Try again.");
            }
        }
    }

    
    public static void runStaffMenu(Scanner scanner, User currentUser) {

        while (true) {

            ConsoleFormatter.printHeader("Staff Menu");
            System.out.println("1. My Profile & Account Info");
            System.out.println("2. View / Manage My Works");
            System.out.println("3. View / Manage My Schedules");
            System.out.println("4. View / Manage My Availability");
            System.out.println("5. View / Submit Requests");
            System.out.println("0. Log Out");

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            switch (choice) {
                case 1 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) userView.runUserViewer(scanner, currentUser);
                    else if (mode == 2) userCtrl.runUserController(scanner, currentUser);
                }
                case 2 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) worksView.runWorksViewer(scanner, currentUser);
                    else if (mode == 2) worksCtrl.runWorksController(scanner, currentUser);
                }
                case 3 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) schedView.runScheduleMenu(scanner, currentUser);
                    else if (mode == 2) schedCtrl.runManagementMenu(scanner, currentUser);
                }
                case 4 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) availView.runAvailabilityViewer(scanner, currentUser);
                    else if (mode == 2) availCtrl.runAvailabilityController(scanner, currentUser);
                }
                case 5 -> {
                    int mode = promptAccessMode(scanner);
                    if (mode == 1) reqView.runRequestViewer(scanner, currentUser);
                    else if (mode == 2) reqCtrl.runRequestController(scanner, currentUser);
                }
                case 0 -> {
                    System.out.println("\nLogging out...");
                    return;
                }
                default -> System.out.println("\n[!] Invalid selection. Try again.");
            }
        }
    }
}




