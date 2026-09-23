package Service_Layer.Viewers;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import DAO.PositionsDAO;
import DAO.RequestsDAO;
import DAO.UserDAO;
import DAO.WorksDAO;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.RequestsDAOImp;
import DAO.DAO_Implementation.UserDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Positions;
import Table_Classes.Requests;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class RequestsViewer {

    private final RequestsDAO reqDAO = new RequestsDAOImp();
    private final UserDAO userDAO = new UserDAOImp();
    private final WorksDAO worksDAO = new WorksDAOImp();
    private final PositionsDAO posDAO = new PositionsDAOImp();

    public void runRequestViewer(Scanner scanner, User currentUser) {
        boolean back = false;

        while (!back) {
            ConsoleFormatter.printHeader("REQUEST VIEWER SYSTEM");

            System.out.println("1. View My Submitted Requests");

            if (currentUser.isAdmin()) {

                System.out.println("2. View Department Requests (All)");
                System.out.println("3. View Requests by Employee ID (Global)");
                System.out.println("4. Back to Main Menu");

            } else if (currentUser.isManager()) {

                System.out.println("2. View My Department Requests");
                System.out.println("3. View Requests by Employee ID (My Departments)");
                System.out.println("4. Back to Main Menu");

            } else {

                System.out.println("2. Back to Main Menu");
            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isAdmin()) {

                switch (choice) {

                    case 1 -> displayUserRequests(currentUser, scanner);
                    case 2 -> {

                        int deptId = InputUtils.readInt(scanner, "Enter Department ID (0 to cancel): ");
                        if (deptId != 0) {
                            displayDepartmentRequests(deptId, scanner);
                        }
                    }
                    case 3 -> {

                        int targetUserId = InputUtils.readInt(scanner, "Enter Employee User ID (0 to cancel): ");
                        if (targetUserId != 0) {
                            User targetUser = userDAO.findById(targetUserId);
                            if (targetUser != null) {
                                displayUserRequests(targetUser, scanner);
                            } else {
                                System.out.println("User ID not found.");
                                InputUtils.readString(scanner, "\nPress Enter to return...");
                            }
                        }
                    }

                    case 4 -> back = true;

                    default -> System.out.println("Invalid selection.");
                }

            } else if (currentUser.isManager()) {

                List<Integer> myDeptIds = getDepartmentIdsForUser(currentUser.getId());

                switch (choice) {
                    case 1 -> displayUserRequests(currentUser, scanner);
                    case 2 -> {
                        int selectedDept = promptForManagerDept(scanner, myDeptIds);
                        if (selectedDept != 0) {
                            displayDepartmentRequests(selectedDept, scanner);
                        }
                    }
                    case 3 -> {

                        int targetUserId = InputUtils.readInt(scanner, "Enter Employee User ID (0 to cancel): ");
                        if (targetUserId != 0) {

                            User targetUser = userDAO.findById(targetUserId);

                            if (targetUser == null) {

                                System.out.println("User ID not found.");

                                InputUtils.readString(scanner, "\nPress Enter to return...");
                                
                                break;
                            }

                            List<Integer> targetUserDepts = getDepartmentIdsForUser(targetUserId);
                            boolean sharesDept = false;

                            for (Integer deptId : targetUserDepts) {
                                if (myDeptIds.contains(deptId)) {
                                    sharesDept = true;
                                    break;
                                }
                            }

                            if (sharesDept) {
                                displayUserRequests(targetUser, scanner);
                            } else {
                                System.out.println("Access Denied: User not found or not in any of your departments.");
                                InputUtils.readString(scanner, "\nPress Enter to return...");
                            }
                        }
                    }
                    case 4 -> back = true;
                    default -> System.out.println("Invalid selection.");
                }

            } else {
                switch (choice) {
                    case 1 -> displayUserRequests(currentUser, scanner);
                    case 2 -> back = true;
                    default -> System.out.println("Invalid selection.");
                }
            }
        }
    }

    private int promptForManagerDept(Scanner scanner, List<Integer> myDeptIds) {
        if (myDeptIds == null || myDeptIds.isEmpty()) {
            System.out.println("You are not currently assigned to any departments.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return 0;
        }

        if (myDeptIds.size() == 1) {
            return myDeptIds.get(0);
        }

        System.out.println("\nYour Managed Departments: " + myDeptIds);
        int choice = InputUtils.readInt(scanner, "Select Department ID (0 to cancel): ");

        if (choice != 0 && !myDeptIds.contains(choice)) {
            System.out.println("Access Denied: You do not manage Department ID " + choice);
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return 0;
        }

        return choice;
    }

    public void displayUserRequests(User user, Scanner scanner) {
        List<Requests> requests = reqDAO.findByUserId(user.getId());

        if (requests == null || requests.isEmpty()) {
            System.out.println("No requests found for this user.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        ConsoleFormatter.printHeader("All Requests for User: " + user.getFirstName() + " " + user.getLastName());

        for (Requests r : requests) {
            System.out.println(r);
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    public void displayDepartmentRequests(int deptId, Scanner scanner) {
        List<Requests> deptRequests = reqDAO.findByDepartmentId(deptId);

        if (deptRequests == null || deptRequests.isEmpty()) {
            System.out.println("No requests found for Department ID: " + deptId);
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        ConsoleFormatter.printHeader("REQUESTS FOR DEPARTMENT ID: " + deptId);

        for (Requests r : deptRequests) {
            System.out.println(r);
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

