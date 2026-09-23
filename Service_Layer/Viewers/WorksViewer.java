package Service_Layer.Viewers;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import DAO.PositionsDAO;
import DAO.WorksDAO;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Positions;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class WorksViewer {

    private final WorksDAO worksDAO = new WorksDAOImp();
    private final PositionsDAO positionDAO = new PositionsDAOImp();

    public void runWorksViewer(Scanner scanner, User currentUser) {

        boolean back = false;
        boolean isElevatedUser = currentUser.isAdmin() || currentUser.isManager();

        while (!back) {

            ConsoleFormatter.printHeader("WORKS & SCHEDULE DIRECTORY");

            if (currentUser.isAdmin()) {

                System.out.println("1. View All System Works");
                System.out.println("2. Search Work Record by User ID & Pos ID");
                System.out.println("3. Back to Main Menu");

            } else if (currentUser.isManager()) {

                System.out.println("1. View My Department Works");
                System.out.println("2. Search Work Record by User ID & Pos ID");
                System.out.println("3. Back to Main Menu");
            } else {

                System.out.println("1. View My Works");
                System.out.println("2. Back to Main Menu");
            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (isElevatedUser) {
                switch (choice) {

                case 1 -> {
                        displayWorksByRole(currentUser);
                        InputUtils.readString(scanner, "\nPress Enter to return...");
                    }

                    case 2 -> {
                        searchWorkById(scanner, currentUser);
                        InputUtils.readString(scanner, "\nPress Enter to return...");
                }

                    case 3 -> back = true;

                    default -> System.out.println("Invalid selection. Please try again.");
                }
            } else{

            switch (choice) {

                case 1 -> {
                        displayWorksByRole(currentUser);
                        InputUtils.readString(scanner, "\nPress Enter to return...");
                 }

                case 2 -> back = true;

                    default -> System.out.println("Invalid selection. Please try again.");
                }
            }
        }
    }

    public void displayWorksByRole(User currentUser) {

        if (currentUser.isAdmin()) {

            List<Works> allWorks = worksDAO.findAll();
            displayWorksList(allWorks, "ALL SYSTEM WORKS (ADMIN VIEW)");

        } else if (currentUser.isManager()) {

            List<Integer> deptIds = getDepartmentIdsForUser(currentUser.getId());

            if (deptIds == null || deptIds.isEmpty()) {
                ConsoleFormatter.printHeader("DEPARTMENT WORKS (MANAGER VIEW)");
                System.out.println("No assigned department found.");
                return;
            }

            for (int deptId : deptIds) {
                List<Works> deptWorks = worksDAO.findByDepartmentId(deptId);
                displayWorksList(deptWorks, "DEPARTMENT #" + deptId + " WORKS");
            }

        } else {

            List<Works> myWorks = worksDAO.findByUserId(currentUser.getId());
            displayWorksList(myWorks, "MY ASSIGNED WORKS");
        }
    }

    private void searchWorkById(Scanner scanner, User currentUser) {

        ConsoleFormatter.printHeader("Search Work Assignment");

        int userId = InputUtils.readInt(scanner, "Enter User ID: ");
        int posId = InputUtils.readInt(scanner, "Enter Position ID: ");

        Works work = worksDAO.findByUserIdPosId(userId, posId);

        if (work == null) {
            System.out.println("\nError: Work assignment not found.");
            return;
        }

        Positions pos = positionDAO.findById(work.getPosId());

        // ensure target position belongs to a department they manage
        if (!currentUser.isAdmin() && currentUser.isManager()) {

            List<Integer> managerDeptIds = getDepartmentIdsForUser(currentUser.getId());

            if (pos == null || !managerDeptIds.contains(pos.getDeptId())) {
                System.out.println("\nAccess Denied: Work record belongs to another department.");
                return;
            }
        }

        displayWorkDetail(work);
    }

    public void displayWorksList(List<Works> worksList, String headerTitle) {

        ConsoleFormatter.printHeader(headerTitle);

        if (worksList == null || worksList.isEmpty()) {
            System.out.println("No work records found.");
            return;
        }

        System.out.printf("%-10s | %-10s | %-10s | %-25s%n", 

                "User ID", "Pos ID", "Dept ID", "Position Name");


        ConsoleFormatter.printDivider();

        for (Works w : worksList) {

            Positions pos = positionDAO.findById(w.getPosId());

            int deptId = (pos != null) ? pos.getDeptId() : 0;

            String posTitle = (pos != null && pos.getName() != null) ? pos.getName() : "N/A";


            System.out.printf("%-10d | %-10d | %-10d | %-25s%n",

                w.getUserId(),  w.getPosId(), deptId,posTitle);
        }

        ConsoleFormatter.printDivider();
    }

    public void displayWorkDetail(Works work) {

        ConsoleFormatter.printHeader("WORK DETAIL RECORD (User #" + work.getUserId() + ", Pos #" + work.getPosId() + ")");

        Positions pos = positionDAO.findById(work.getPosId());

        System.out.printf("  User ID       : %d%n", work.getUserId());
        System.out.printf("  Position ID   : %d%n", work.getPosId());
        System.out.printf("  Department ID : %d%n", (pos != null ? pos.getDeptId() : 0));
        System.out.printf("  Position Title: %s%n", (pos != null && pos.getName() != null ? pos.getName() : "N/A"));
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