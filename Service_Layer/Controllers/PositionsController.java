package Service_Layer.Controllers;

import java.util.Scanner;

import DAO.DepartmentsDAO;
import DAO.PositionsDAO;
import DAO.DAO_Implementation.DepartmentsDAOImp;
import DAO.DAO_Implementation.PositionsDAOImp;
import Table_Classes.Positions;
import Table_Classes.User;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class PositionsController {

public void runPositionsController(Scanner scanner, User currentUser) {

        if (!currentUser.isManager()) {
            System.out.println("Access Denied: admin privilege required.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("POSITIONS CONTROL MANAGEMENT");

            System.out.println("1. Create New Position");
            System.out.println("2. Delete Position");
            System.out.println("3. Back to Main Menu");

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            switch (choice) {

                case 1 -> handleCreatePosition(scanner);

                case 2 -> handleDeletePosition(scanner);

                case 3 -> back = true;

                default -> System.out.println("Invalid selection.");
            }

        }
    }
    
    private void handleCreatePosition(Scanner scanner) {

        ConsoleFormatter.printHeader("CREATE NEW POSITION");

        PositionsDAO posDAO = new PositionsDAOImp();
        DepartmentsDAO depDAO = new DepartmentsDAOImp();

        String title = InputUtils.readString(scanner, "Enter Position Title (0 to cancel): ");
        if (title.equals("0")) return;

        int deptId = InputUtils.readInt(scanner, "Enter Department ID for this position (0 to cancel): ");
        if (deptId == 0) return;
        
        if(depDAO.findById(deptId) == null){
           System.out.println("Error, no department found with ID #"+deptId);
           InputUtils.readString(scanner, "\nPress Enter to return...");
           return;
        }

        Positions newPosition = new Positions( deptId,title);
        
        boolean success = posDAO.insert(newPosition);

        if (success) {

            System.out.println("\nPosition successfully created: " + title);

        } else {

            System.out.println("\nFailed to save position to database");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void handleDeletePosition(Scanner scanner) {

        ConsoleFormatter.printHeader("DELETE POSITION");

        PositionsDAO posDAO = new PositionsDAOImp();

        int posId = InputUtils.readInt(scanner, "Enter Position ID to delete (0 to exit): ");
        if (posId == 0) return;

        Positions position = posDAO.findById(posId);

        if (position == null) {

            System.out.println("Position ID not found.");

            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        boolean success = posDAO.delete(posId);

        if (success) {

            System.out.println("\nPosition ID " + posId + " successfully deleted.");

        } else {

            System.out.println("\nFailed to delete position from database.");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }
}
