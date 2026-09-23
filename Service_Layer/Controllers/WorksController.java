package Service_Layer.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import DAO.WorksDAO;
import DAO.PositionsDAO;
import DAO.UserDAO;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.UserDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Service_Layer.Viewers.WorksViewer;
import Table_Classes.Positions;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class WorksController {

    private final WorksDAO worksDAO = new WorksDAOImp();
    private final WorksViewer worksViewer = new WorksViewer();
    private final UserDAO userDAO = new UserDAOImp();
    private final PositionsDAO positionDAO = new PositionsDAOImp();

    public void runWorksController(Scanner scanner, User currentUser) {

        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("WORKS MANAGEMENT");

            if (currentUser.isAdmin()) {
                System.out.println("1. View All System Works");
                System.out.println("2. Assign Position to User");
                System.out.println("3. Delete Work Assignment");
                System.out.println("4. Back to Main Menu");
            } else if (currentUser.isManager()) {
                System.out.println("1. View Department Works");
                System.out.println("2. Assign Position to User");
                System.out.println("3. Delete Work Assignment");
                System.out.println("4. Back to Main Menu");
            } else {
                System.out.println("1. View My Works");
                System.out.println("2. Back to Main Menu");
            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isAdmin() || currentUser.isManager()) {
                switch (choice) {
                    case 1 -> {
                        worksViewer.displayWorksByRole(currentUser);
                        InputUtils.readString(scanner, "\nPress Enter to return...");
                    }
                    case 2 -> createWork(scanner, currentUser);
                    case 3 -> deleteWork(scanner, currentUser);
                    case 4 -> back = true;
                    default -> System.out.println("Invalid selection");
                }
            } else {
                switch (choice) {
                    case 1 -> {
                        worksViewer.displayWorksByRole(currentUser);
                        InputUtils.readString(scanner, "\nPress Enter to return...");
                    }
                    case 2 -> back = true;
                    default -> System.out.println("Invalid selection");
                }
            }
        }
    }

    private void createWork(Scanner scanner, User currentUser) {

        ConsoleFormatter.printHeader("ASSIGN POSITION TO USER");

        int targetUserId = InputUtils.readInt(scanner, "Enter Target User ID: ");

        if (currentUser.getId() == targetUserId) {
            System.out.println("\nError: You cannot create a work assignment for yourself.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        User targetUser = userDAO.findById(targetUserId);

        if (targetUser == null) {
            System.out.println("\nError: User ID #" + targetUserId + " does not exist in the system.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        int targetPosId = InputUtils.readInt(scanner, "Enter Target Position ID (pos_id): ");
        Positions targetPosition = positionDAO.findById(targetPosId);

        if (targetPosition == null) {
            System.out.println("\nError: Position ID #" + targetPosId + " does not exist in the system.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        if (!currentUser.isAdmin() && currentUser.isManager() && !hasPermission(currentUser, targetUserId, targetPosId)) {
            System.out.println("\nError: You can only create work assignments for regular users (non-manager/admin) within your department.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        if (exist(targetUserId, targetPosId)) {
            System.out.println("\nError: User #" + targetUserId + " is already assigned to Position #" + targetPosId + ".");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        Works newWork = new Works();
        newWork.setUserId(targetUserId);
        newWork.setPosId(targetPosId);

        boolean added = worksDAO.insert(newWork);

        if (added) {
            System.out.println("\nWork assignment created successfully!");
        } else {
            System.out.println("\nError: Could not save work assignment.");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void deleteWork(Scanner scanner, User currentUser) {

        ConsoleFormatter.printHeader("DELETE WORK ASSIGNMENT");

        if (currentUser.isAdmin()) {
            List<Works> worksList = worksDAO.findAll();
            worksViewer.displayWorksList(worksList, "ALL WORKS");
        } else {
            List<Integer> deptIds = getDepartmentIdsForUser(currentUser.getId());

            if (deptIds == null || deptIds.isEmpty()) {
                System.out.println("No assigned department found.");
                InputUtils.readString(scanner, "\nPress Enter to return...");
                return;
            }

            for (int deptId : deptIds) {
                List<Works> worksList = worksDAO.findByDepartmentId(deptId);
                worksViewer.displayWorksList(worksList, "DEPARTMENT #" + deptId + " WORKS");
            }
        }

        int userId = InputUtils.readInt(scanner, "\nEnter user ID (0 to cancel): ");
        if (userId == 0) return;

        int posId = InputUtils.readInt(scanner, "Enter pos ID to delete (0 to cancel): ");
        if (posId == 0) return;

        Works target = worksDAO.findByUserIdPosId(userId, posId);

        if (target == null) {
            System.out.println("\nError: Work assignment not found.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        //cannot delete own work 
        if (currentUser.isManager() && !currentUser.isAdmin() && target.getUserId() == currentUser.getId()) {
            System.out.println("\nError: You cannot delete your own work assignment.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        //must belong to manager's department and target must be non-admin/non-manager
        if (!currentUser.isAdmin() && currentUser.isManager() && !hasPermission(currentUser, userId, posId)) {
            System.out.println("\nError: You can only delete non-admin/manager work assignments belonging to your department.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        String confirm = InputUtils.readString(scanner, "Are you sure you want to remove Work Assignment (User #" + userId + ", Pos #" + posId + ")? (Y/N): ");

        if (confirm.equalsIgnoreCase("Y")) {
            boolean deleted = worksDAO.delete(new Works(userId, posId));

            if (deleted) {
                System.out.println("\nWork assignment deleted successfully!");
            } else {
                System.out.println("\nError: Could not delete work assignment.");
            }
        } else {
            System.out.println("Operation canceled.");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private boolean exist(int userId, int posId) {
        return worksDAO.findByUserIdPosId(userId, posId) != null;
    }

    boolean hasPermission(User currentUser, int targetUserId, int posId) {
    
        if (currentUser.isAdmin()) return true;
        if (!currentUser.isManager()) return false;

        //manager cannot manage other managers or admins
        User targetUser = userDAO.findById(targetUserId);
        if(targetUser==null){
            return false;
        }else if ((targetUser.isManager() || targetUser.isAdmin())) {
            return false;
        }

        Positions p = positionDAO.findById(posId);
        if (p == null) return false;

    
        List<Integer> deptIds = getDepartmentIdsForUser(currentUser.getId());

        return deptIds.contains(p.getDeptId());
    }

    private List<Integer> getDepartmentIdsForUser(int userId) {

        List<Works> userWorks = worksDAO.findByUserId(userId);
        List<Integer> deptIds = new ArrayList<>();

        if (userWorks != null) {
            for (Works w : userWorks) {
                Positions p = positionDAO.findById(w.getPosId());
                if (p != null && !deptIds.contains(p.getDeptId())) {
                    deptIds.add(p.getDeptId());
                }
            }
        }

        return deptIds;
    }
}