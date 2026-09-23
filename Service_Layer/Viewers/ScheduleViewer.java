package Service_Layer.Viewers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import DAO.DepartmentsDAO;
import DAO.PositionsDAO;
import DAO.SchedulesDAO;
import DAO.UserDAO;
import DAO.WorksDAO;
import DAO.DAO_Implementation.DepartmentsDAOImp;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.SchedulesDAOImp;
import DAO.DAO_Implementation.UserDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Departments;
import Table_Classes.Positions;
import Table_Classes.Schedules;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class ScheduleViewer {

    private final SchedulesDAO schDAO = new SchedulesDAOImp();

    private final UserDAO userDAO = new UserDAOImp();
    private final WorksDAO worksDAO = new WorksDAOImp();

    private final PositionsDAO posDAO = new PositionsDAOImp();

    private final DepartmentsDAO deptDAO = new DepartmentsDAOImp();

    public void runScheduleMenu(Scanner scanner, User currentUser) {
        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("SCHEDULE MANAGEMENT SYSTEM");

            System.out.println("1. View My Weekly Schedule");
            System.out.println("2. View My Monthly Schedule");

            
            if (currentUser.isManager() || currentUser.isAdmin()) {
                System.out.println("3. View Department Schedule");
                System.out.println("4. Back to Main Menu");
            } else {
                System.out.println("3. Back to Main Menu");
            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isManager() || currentUser.isAdmin()) {

                switch (choice) {
                    case 1 -> handleWeeklyView(scanner, currentUser.getId());
                    case 2 -> handleMonthlyView(scanner, currentUser.getId());
                    case 3 -> handleDepartmentView(scanner, currentUser);
                    case 4 -> back = true;
                    default -> System.out.println("Invalid selection.");
                }

            } else {

                switch (choice) {
                    case 1 -> handleWeeklyView(scanner, currentUser.getId());
                    case 2 -> handleMonthlyView(scanner, currentUser.getId());
                    case 3 -> back = true;
                    default -> System.out.println("Invalid selection.");
                }

            }
        }
    }

    private void handleWeeklyView(Scanner scanner, int userId) {

        LocalDate date = InputUtils.readDate(scanner, "Enter target date in the week");

        LocalDateTime targetDateTime = date.atStartOfDay(); // 00:00
        LocalDateTime monday = ConsoleFormatter.getPreviousOrCurrentMonday(targetDateTime);

        List<Schedules> shifts = schDAO.findByUserIdAndRange(monday, monday.plusWeeks(1), userId);

        if (shifts != null && !shifts.isEmpty()) {

            ConsoleFormatter.printHeader("WEEKLY SCHEDULE (" + date + ")");

            ConsoleFormatter.printWeeklyDays(monday);

            ConsoleFormatter.printDivider();

            ConsoleFormatter.printWeeklySchedule(shifts, monday);

            ConsoleFormatter.printDivider();

        } else {
            System.out.println("No schedule found.");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void handleMonthlyView(Scanner scanner, int userId) {

        int month = 0, year = 0;

        do {
            year = InputUtils.readInt(scanner, "Enter Year (e.g., 2026): ");

            month = InputUtils.readInt(scanner, "Enter Month (1-12): ");

            if (month < 1 || month > 12) {

                System.out.println("Please enter a valid month (1-12)");

            }

        } while (month < 1 || month > 12);

        LocalDateTime day = LocalDateTime.of(year, month, 1, 0, 0);

        List<Schedules> shifts = schDAO.findByUserIdAndRange(day, day.plusMonths(1), userId);

        if (shifts != null && !shifts.isEmpty()) {

            ConsoleFormatter.printHeader("MONTHLY SCHEDULE (" + year + "-" + String.format("%02d", month) + ")");

            ConsoleFormatter.printMonthlySchedule(shifts, year, month);

        } else {

            System.out.println("No schedule found.");

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void handleDepartmentView(Scanner scanner, User currentUser) {

        List<Departments> availableDepts = new ArrayList<>();

        if (currentUser.isAdmin()) {
            //admins can see all departments
            availableDepts = deptDAO.findAll(); 
        } else {
            //managers can only see their departments
            List<Integer> deptIds = getDepartmentIdsForUser(currentUser.getId());

            for (Integer dId : deptIds) {

                Departments d = deptDAO.findById(dId);

                if (d != null) {
                    availableDepts.add(d);
                }
            }
        }

        if (availableDepts == null || availableDepts.isEmpty()) {

            System.out.println("You are not assigned to manage any departments.");

            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        ConsoleFormatter.printHeader("Authorized Departments");

        for (Departments d : availableDepts) {

            System.out.println(d);

        }

        boolean validSelection = false;
        int deptId = 0;

        do {
            deptId = InputUtils.readInt(scanner, "Enter Department ID (0 to cancel): ");

            if (deptId == 0) return; 

            for (Departments d : availableDepts) {

                if (d.getId() == deptId) {

                    validSelection = true;

                    break;
                }
            }

            if (!validSelection) {
                System.out.println("Invalid Choice, permission Denied or non existing Department ID\n"
                        + "pls refer to the authorized list printed above");
            }

        } while (!validSelection);

        LocalDate date = InputUtils.readDate(scanner, "Enter target date in week");

        LocalDateTime monday = date.atStartOfDay();

        monday = ConsoleFormatter.getPreviousOrCurrentMonday(monday);

        List<Schedules> deptShifts = schDAO.findByDepartmentIdAndRange(monday, monday.plusWeeks(1), deptId);

        if (deptShifts == null || deptShifts.isEmpty()) {

            System.out.println("\nNo schedule found for this department during this week.");

            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        int startIndex = 0;

        ConsoleFormatter.printHeader("DEPARTMENT WEEKLY SCHEDULE");

        ConsoleFormatter.printWeeklyDays(monday);

        ConsoleFormatter.printDivider();

        for (int i = 0; i < deptShifts.size(); i++) {

            boolean isLastElement = (i == deptShifts.size() - 1);
            boolean isUserChange = (!isLastElement && deptShifts.get(i).getUserId() != deptShifts.get(i + 1).getUserId());

            if (isUserChange || isLastElement) {

                int currentUserId = deptShifts.get(startIndex).getUserId();
                List<Schedules> userShifts = deptShifts.subList(startIndex, i + 1);

                User emp = userDAO.findById(currentUserId);
                String empName = (emp != null) 
                    ? emp.getFirstName() + " " + emp.getLastName() + " (ID: " + currentUserId + ")" 

                    : "ID: " + currentUserId;

                System.out.println(empName);

               
                ConsoleFormatter.printWeeklySchedule(userShifts, monday);
                ConsoleFormatter.printDivider();

                
                startIndex = i + 1;
            }
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