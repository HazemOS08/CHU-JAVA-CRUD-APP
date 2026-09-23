package Service_Layer.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import DAO.PositionsDAO;
import DAO.SchedulesDAO;
import DAO.UserDAO;
import DAO.WorksDAO;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.SchedulesDAOImp;
import DAO.DAO_Implementation.UserDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Positions;
import Table_Classes.Schedules;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class ScheduleController {

    private final UserDAO userDAO = new UserDAOImp();
    private final SchedulesDAO schDAO = new SchedulesDAOImp();
    private final PositionsDAO posDAO = new PositionsDAOImp();
    private final WorksDAO worksDAO = new WorksDAOImp();

    public void runManagementMenu(Scanner scanner, User currentUser) {

        if (!currentUser.isManager() && !currentUser.isAdmin()) {
            System.out.println("Permission Denied: Access restricted to managers and administrators.");
            return;
        }

        boolean back = false;

        while (!back) {
            ConsoleFormatter.printHeader("SHIFT MANAGEMENT");

            System.out.println("1. Assign New Shift");
            System.out.println("2. Cancel Shift");
            System.out.println("3. Back to Schedule Menu");

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            switch (choice) {
                case 1 -> handleCreateShift(scanner, currentUser);
                case 2 -> handleDeleteShift(scanner, currentUser);
                case 3 -> back = true;
                default -> System.out.println("Invalid selection.");
            }
        }
    }

    private void handleCreateShift(Scanner scanner, User currentUser) {

        ConsoleFormatter.printHeader("CREATE NEW SHIFT");

        int targetUserId = InputUtils.readInt(scanner, "Enter Employee User ID (0 to cancel): ");
        if (targetUserId == 0) return;

        User targetUser = userDAO.findById(targetUserId);

        if (targetUser == null) {
            System.out.println("User ID not found.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        // --- AUTHORIZATION CHECK ---
        List<Integer> myDeptIds = currentUser.isAdmin() ? new ArrayList<>() : getDepartmentIdsForUser(currentUser.getId());
        
        if (!currentUser.isAdmin()) {
            List<Integer> targetUserDepts = getDepartmentIdsForUser(targetUserId);
            boolean sharesDept = false;

            for (Integer deptId : targetUserDepts) {
                if (myDeptIds.contains(deptId)) {
                    sharesDept = true;
                    break;
                }
            }

            if (!sharesDept) {
                System.out.println("Access Denied: Employee is not in any of your departments.");
                InputUtils.readString(scanner, "\nPress Enter to return...");
                return;
            }
        }

        // --- POSITION SELECTION & VALIDATION ---
        int posId = 0;
        while (true) {
            posId = InputUtils.readInt(scanner, "Enter Position ID for this shift (0 to cancel): ");

            if (posId == 0) return;

            Positions position = posDAO.findById(posId);
            if (position == null) {
                System.out.println("Position ID does not exist. Please try again.");
                continue;
            }

            // Ensure the manager actually manages the department this position belongs to
            if (!currentUser.isAdmin() && !myDeptIds.contains(position.getDeptId())) {
                System.out.println("Access Denied: Position belongs to a department you do not manage.");
                continue;
            }

            break;
        }

        // Verify the employee is actually assigned to this position
        List<Works> userWorks = worksDAO.findByUserId(targetUserId);
        boolean isAssigned = false;
        if (userWorks != null) {
            for (Works w : userWorks) {
                if (w.getPosId() == posId) {
                    isAssigned = true;
                    break;
                }
            }
        }

        if (!isAssigned) {
            System.out.println("\nEmployee ID " + targetUserId + " is not assigned to Position ID " + posId);
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        // --- TIMING & CONFLICT CHECKS ---
        LocalDateTime startTime = InputUtils.readDateTime(scanner, "Enter Shift Start Time (yyyy-MM-dd HH:mm)");
        LocalDateTime endTime = InputUtils.readDateTime(scanner, "Enter Shift End Time (yyyy-MM-dd HH:mm)");

        if (!endTime.isAfter(startTime)) {
            System.out.println("Invalid shift duration. End time must be after start time.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        if (hasScheduleConflict(targetUserId, startTime, endTime)) {
            System.out.println("The employee already has a shift overlapping this time slot.");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        // --- SAVE TO DATABASE ---
        Schedules newShift = new Schedules(targetUserId, startTime, endTime, posId);
        boolean success = schDAO.insert(newShift);

        if (success) {
            System.out.println("\nShift successfully created for " + targetUser.getFirstName() + " " + targetUser.getLastName());
        } else {
            System.out.println("\nFailed to save shift to database.");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    public boolean hasScheduleConflict(int userId, LocalDateTime start, LocalDateTime end) {

        List<Schedules> shifts = schDAO.findByUserIdAndRange(
            start.toLocalDate().minusDays(1).atStartOfDay(),

            start.toLocalDate().plusDays(2).atStartOfDay(),

            userId
        );

        if (shifts != null) {
            for (Schedules s : shifts) {
                if (start.isBefore(s.getEndTime()) && s.getStartTime().isBefore(end)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void handleDeleteShift(Scanner scanner, User currentUser) {

        ConsoleFormatter.printHeader("CANCEL SHIFT");

        int scheduleId = InputUtils.readInt(scanner, "Enter Shift ID to cancel (0 to exit): ");
        if (scheduleId == 0) return;

        Schedules shift = schDAO.findById(scheduleId);

        if (shift == null) {

            System.out.println("Shift ID not found.");

            InputUtils.readString(scanner, "\nPress Enter to return...");

            return;
        }

        int targetUserId = shift.getUserId();

        if (!currentUser.isAdmin()) {

            List<Integer> myDeptIds = getDepartmentIdsForUser(currentUser.getId());

            List<Integer> targetUserDepts = getDepartmentIdsForUser(targetUserId);

            boolean sharesDept = false;

            for (Integer deptId : targetUserDepts) {

                if (myDeptIds.contains(deptId)) {

                    sharesDept = true;
                    break;
                }
            }

            if (!sharesDept) {
                System.out.println("Access Denied, shift belongs to an employee outside your departments");

                InputUtils.readString(scanner, "\nPress Enter to return...");
                return;
            }
        }

        boolean success = schDAO.delete(scheduleId);

        if (success) {
            System.out.println("\nShift ID " + scheduleId + " successfully cancelled.");
        } else {
            System.out.println("\nFailed to delete shift from database.");
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