package Service_Layer.Viewers;

import DAO.PositionsDAO;
import DAO.UserDAO;
import DAO.WorksDAO;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.UserDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Positions;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PositionsViewer {

    private final PositionsDAO posDAO = new PositionsDAOImp();
    private final WorksDAO worksDAO = new WorksDAOImp();
    private final UserDAO userDAO = new UserDAOImp();

    public void runPositionsViewer(Scanner scanner, User currentUser) {

        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("POSITIONS VIEWER SYSTEM");

            System.out.println("1. View My Assigned Positions");

            if (currentUser.isManager()) {
                System.out.println("2. View Department Positions");
                System.out.println("3. View Users by Position (Manager)");
                System.out.println("4. Back to Main Menu");

            } else {

                System.out.println("2. Back to Main Menu");

            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isManager() ) {

                switch (choice) {

                    case 1 -> displayUserPositions(currentUser, scanner);

                    case 2 -> {
                        int deptId = InputUtils.readInt(scanner, "Enter Department ID (0 to cancel): ");
                        if (deptId != 0) {
                            displayDepartmentPositions(deptId, currentUser, scanner);
                        }
                    }

                    case 3 -> {

                        int posId = InputUtils.readInt(scanner, "Enter Position ID (0 to cancel): ");
                        if (posId != 0) {
                            displayUsersByPosition(posId, currentUser, scanner);
                        }
                    }

                    case 4 -> back = true;

                    default -> System.out.println("Invalid selection");
                }

            } else {

                switch (choice) {

                    case 1 -> displayUserPositions(currentUser, scanner);

                    case 2 -> back = true;

                    default -> System.out.println("Invalid selection");
                }

            }
        }
    }

    public void displayUserPositions(User user, Scanner scanner) {

        List<Works> userWorks = worksDAO.findByUserId(user.getId());

        if (userWorks == null || userWorks.isEmpty()) {
            System.out.println("No positions assigned to this user");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        List<Positions> positions = new ArrayList<>();

        for (Works work : userWorks) {

            Positions p = posDAO.findById(work.getPosId());

            if (p != null) {

                positions.add(p);

            }
        }

        ConsoleFormatter.printHeader("All Positions for User: " + user.getFirstName() + " " + user.getLastName());

        for (Positions p : positions) {

            System.out.println(p);

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    public void displayDepartmentPositions(int deptId, User currentUser, Scanner scanner) {

        
        if (!currentUser.isAdmin()) {
            List<Integer> myDeptIds = getDepartmentIdsForUser(currentUser.getId());

            if (!myDeptIds.contains(deptId)) {

                System.out.println("Access Denied, you do not have permission to view positions for this department");
                InputUtils.readString(scanner, "\nPress Enter to return...");

                return;
            }
        }

        List<Positions> positions = posDAO.findByDeptId(deptId);

        if (positions == null || positions.isEmpty()) {

            System.out.println("No positions found for this department");

            InputUtils.readString(scanner, "\nPress Enter to return...");

            return;
        }

        ConsoleFormatter.printHeader("All Positions for Department ID: " + deptId);

        for (Positions p : positions) {

            System.out.println(p);

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    public void displayUsersByPosition(int posId, User currentUser, Scanner scanner) {

        Positions position = posDAO.findById(posId);

        if (position == null) {

            System.out.println("Position ID not found.");

            InputUtils.readString(scanner, "\nPress Enter to return...");

            return;
        }

        
        if (!currentUser.isAdmin()) {

            List<Integer> myDeptIds = getDepartmentIdsForUser(currentUser.getId());

            if (!myDeptIds.contains(position.getDeptId())) {

                System.out.println("Access Denied, position belongs to a department you do not manage");

                InputUtils.readString(scanner, "\nPress Enter to return...");
                return;
            }
        }

        List<Works> worksList = worksDAO.findByPosId(posId);

        if (worksList == null || worksList.isEmpty()) {

            System.out.println("No users assigned to this position");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        List<User> users = new ArrayList<>();

        for (Works work : worksList) {

            User u = userDAO.findById(work.getUserId());
            if (u != null) {
                users.add(u);
            }
        }

        ConsoleFormatter.printHeader("All Users for Position ID: " + posId + " (" + position.getName() + ")");

        for (User u : users) {
            System.out.println(u);
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