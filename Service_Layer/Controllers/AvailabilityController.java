package Service_Layer.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import DAO.AvailabilityDAO;
import DAO.PositionsDAO;
import DAO.WorksDAO;
import DAO.DAO_Implementation.AvailabilityDAOImp;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Availability;
import Table_Classes.Positions;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class AvailabilityController {

    private final AvailabilityDAO availDAO = new AvailabilityDAOImp();
    private final WorksDAO worksDAO = new WorksDAOImp();
    private final PositionsDAO posDAO = new PositionsDAOImp();

    public void runAvailabilityController(Scanner scanner, User currentUser) {

        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("AVAILABILITY MANAGEMENT");

            System.out.println("1. Submit/Update My Availability");
            System.out.println("2. Delete My Availability");

            if (currentUser.isManager() || currentUser.isAdmin()) {
                System.out.println("3. Remove Employee Availability (Manager)");
                System.out.println("4. Back to Main Menu");
            } else {
                System.out.println("3. Back to Main Menu");
            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isManager() || currentUser.isAdmin()) {
                switch (choice) {
                    case 1 -> handleSetAvailability(currentUser.getId(), scanner);
                    case 2 -> handleDeleteOwnAvailability(currentUser.getId(), scanner);
                    case 3 -> handleManagerDeleteAvailability(currentUser, scanner);
                    case 4 -> back = true;
                    default -> System.out.println("Invalid selection.");
                }
            } else {
                switch (choice) {
                    case 1 -> handleSetAvailability(currentUser.getId(), scanner);
                    case 2 -> handleDeleteOwnAvailability(currentUser.getId(), scanner);
                    case 3 -> back = true;
                    default -> System.out.println("Invalid selection.");
                }
            }
        }
    }

    private void handleSetAvailability(int userId, Scanner scanner) {

        ConsoleFormatter.printHeader("SET AVAILABILITY");

        Availability existing = availDAO.findByUserId(userId);

        System.out.println("Enter availability details (e.g., 'Mon-Fri 08:00-16:00' or 'Weekends Only'):");
        
        String details = InputUtils.readString(scanner, "Availability Details (or 0 to cancel): ");

        if ("0".equals(details.trim())) return;

        if (existing == null) {
            Availability newAvail = new Availability();
            newAvail.setUserId(userId);
            newAvail.setInfo(details);

            boolean created = availDAO.insert(newAvail);
            if (created) {
                System.out.println("\nAvailability submitted successfully.");
            } else {
                System.out.println("\nFailed to submit availability.");
            }
        } else {
            existing.setInfo(details);
            boolean updated = availDAO.update(existing);
            if (updated) {
                System.out.println("\nAvailability updated successfully.");
            } else {
                System.out.println("\nFailed to update availability.");
            }
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void handleDeleteOwnAvailability(int userId, Scanner scanner) {
        Availability existing = availDAO.findByUserId(userId);

        if (existing == null) {
            System.out.println("No availability entry to delete.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        String confirm = InputUtils.readString(scanner, "Are you sure you want to delete your availability? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            boolean deleted = availDAO.delete(existing.getId());
            if (deleted) {
                System.out.println("Availability record deleted.");
            } else {
                System.out.println("Failed to delete availability record.");
            }
        } else {
            System.out.println("Operation canceled.");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void handleManagerDeleteAvailability(User currentUser, Scanner scanner) {
        ConsoleFormatter.printHeader("REMOVE EMPLOYEE AVAILABILITY");

        int targetUserId = InputUtils.readInt(scanner, "Enter Target Employee User ID (0 to cancel): ");
        if (targetUserId == 0) return;

        Availability targetAvail = availDAO.findByUserId(targetUserId);
        if (targetAvail == null) {
            System.out.println("No availability found for User ID: " + targetUserId);
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        // --- AUTHORIZATION CHECK ---
        if (!currentUser.isAdmin()) {
            List<Integer> managerDeptIds = getDepartmentIdsForUser(currentUser.getId());
            List<Integer> employeeDeptIds = getDepartmentIdsForUser(targetUserId);

            boolean shareDepartment = false;
            for (Integer deptId : employeeDeptIds) {
                if (managerDeptIds.contains(deptId)) {
                    shareDepartment = true;
                    break;
                }
            }

            if (!shareDepartment) {
                System.out.println("Access Denied: Employee does not belong to any of your managed departments.");
                InputUtils.readString(scanner, "\nPress Enter to return...");
                return;
            }
        }

        String confirm = InputUtils.readString(scanner, "Delete availability for User ID " + targetUserId + "? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            boolean deleted = availDAO.delete(targetAvail.getId());
            if (deleted) {
                System.out.println("Employee availability removed successfully.");
            } else {
                System.out.println("Failed to remove availability record.");
            }
        } else {
            System.out.println("Operation canceled.");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private List<Integer> getDepartmentIdsForUser(int userId) {

        List<Works> userWorks = worksDAO.findByUserId(userId);

        List<Integer> deptIds = new ArrayList<>();

        if (userWorks != null) {
            for (Works w : userWorks) {
                Positions p = posDAO.findById(w.getPosId());
                if (p != null && !deptIds.contains(p.getDeptId())) {
                    deptIds.add(p.getDeptId());
                }
            }
        }

        return deptIds;
    }
}
