package Service_Layer.Viewers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import DAO.PositionsDAO;
import DAO.WorksDAO;
import DAO.UserDAO;
import DAO.AvailabilityDAO;
import DAO.DAO_Implementation.AvailabilityDAOImp;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.UserDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Availability;
import Table_Classes.Positions;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class AvailabilityViewer {
    private final UserDAO userDAO=new UserDAOImp();
    private final PositionsDAO posDAO= new PositionsDAOImp();
    private final WorksDAO worksDAO= new WorksDAOImp();
    private final AvailabilityDAO availDAO= new AvailabilityDAOImp();

    
    public void runAvailabilityViewer(Scanner scanner, User currentUser) {

        
        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("AVAILABILITY VIEWER");

            System.out.println("1. View My Availability");

            if (currentUser.isManager()) {

                System.out.println("2. View Department Employee Availabilities");
                System.out.println("3. Back to Main Menu");

            } else {

                System.out.println("2. Back to Main Menu");
            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isManager()) {

                switch (choice) {

                    case 1 -> displayUserAvailability(currentUser.getId(), scanner);

                    case 2 -> displayDepartmentAvailabilities(currentUser, scanner);

                    case 3 -> back = true;

                    default -> System.out.println("Invalid selection.");
                }
            } else {

                switch (choice) {

                    case 1 -> displayUserAvailability(currentUser.getId(), scanner);

                    case 2 -> back = true;

                    default -> System.out.println("Invalid selection.");
                }
               }
    }
    }

    public void displayUserAvailability(int userId, Scanner scanner) {

        Availability avail = availDAO.findByUserId(userId);

        User user = userDAO.findById(userId);
        
        String name = (user != null) ? user.getFirstName() + " " + user.getLastName() : "User ID: " + userId;

        ConsoleFormatter.printHeader("AVAILABILITY FOR: " + name);

        if (avail == null) {

            System.out.println("No availability records found on file.");

        } else {

            System.out.println(avail);

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    public void displayDepartmentAvailabilities(User currentUser, Scanner scanner) {


        int deptId = InputUtils.readInt(scanner, "Enter Department ID (0 to cancel): ");
        if (deptId == 0) return;

        
        if (!currentUser.isAdmin()) {

            List<Integer> myDeptIds = getDepartmentIdsForUser(currentUser.getId());

            if (!myDeptIds.contains(deptId)) {

                System.out.println("Access Denied, you do not have permission to view availabilities for this department");

                InputUtils.readString(scanner, "\nPress Enter to return...");
                return;
            }
        }

        List<Availability> deptAvails = availDAO.findByDepartmentId(deptId);

        if (deptAvails == null || deptAvails.isEmpty()) {
            System.out.println("No availability records found for this department");

            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        ConsoleFormatter.printHeader("DEPARTMENT AVAILABILITY LIST (Dept ID: " + deptId + ")");

        for (Availability avail : deptAvails) {

            User emp = userDAO.findById(avail.getUserId());

            String empName = (emp != null) ? emp.getFirstName() + " " + emp.getLastName() : "ID: " + avail.getUserId();

            System.out.println("Employee: " + empName);

            System.out.println(avail);

            ConsoleFormatter.printDivider();

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
