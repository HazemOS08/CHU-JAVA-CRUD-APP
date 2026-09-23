package Service_Layer.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import DAO.PositionsDAO;
import DAO.RequestTypesDAO;
import DAO.RequestsDAO;
import DAO.UserDAO;
import DAO.WorksDAO;
import DAO.DAO_Implementation.PositionsDAOImp;
import DAO.DAO_Implementation.RequestTypesDAOImp;
import DAO.DAO_Implementation.RequestsDAOImp;
import DAO.DAO_Implementation.UserDAOImp;
import DAO.DAO_Implementation.WorksDAOImp;
import Table_Classes.Positions;
import Table_Classes.RequestTypes;
import Table_Classes.Requests;
import Table_Classes.User;
import Table_Classes.Works;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class RequestController {

    private final RequestsDAO reqDAO = new RequestsDAOImp();
    private final RequestTypesDAO reqTypesDAO = new RequestTypesDAOImp();
    private final UserDAO userDAO = new UserDAOImp();
    private final WorksDAO worksDAO = new WorksDAOImp();
    private final PositionsDAO posDAO = new PositionsDAOImp();

    public void runRequestController(Scanner scanner, User currentUser) {
        boolean back = false;

        while (!back) {
            ConsoleFormatter.printHeader("REQUEST MANAGEMENT CONTROLLER");

            System.out.println("1. View All Request Types");
            System.out.println("2. Submit a New Request");

            if (currentUser.isManager() || currentUser.isAdmin()) {
                System.out.println("3. Process/Update Request Status (Manager)");
                System.out.println("4. Back to Main Menu");
            } else {
                System.out.println("3. Back to Main Menu");
            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isManager() || currentUser.isAdmin()) {
                switch (choice) {
                    case 1 -> {
                        displayRequestTypes();
                        InputUtils.readString(scanner, "\nPress Enter to return...");
                    }
                    case 2 -> submitRequest(scanner, currentUser);
                    case 3 -> processRequest(scanner, currentUser);
                    case 4 -> back = true;
                    default -> System.out.println("Invalid selection");
                }
            } else {
                switch (choice) {
                    case 1 -> {
                        displayRequestTypes();
                        InputUtils.readString(scanner, "\nPress Enter to return...");
                    }
                    case 2 -> submitRequest(scanner, currentUser);
                    case 3 -> back = true;
                    default -> System.out.println("Invalid selection");
                }
            }
        }
    }

    private void displayRequestTypes() {
        ConsoleFormatter.printHeader("AVAILABLE REQUEST TYPES");

        List<RequestTypes> types = reqTypesDAO.findAll();

        if (types == null || types.isEmpty()) {
            System.out.println("No request types found in the database.");
        } else {
            for (RequestTypes t : types) {
                System.out.println(t);
            }
        }
    }

    private void submitRequest(Scanner scanner, User currentUser) {
        displayRequestTypes();

        int typeId = InputUtils.readInt(scanner, "\nEnter Request Type ID (0 to cancel): ");
        if (typeId == 0) return;

        RequestTypes selectedType = reqTypesDAO.findById(typeId);
        if (selectedType == null) {
            System.out.println("Invalid Request Type ID");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        String details = InputUtils.readString(scanner, "Enter Request Details / Reason: ");

        Requests req = new Requests();

        req.setUserId(currentUser.getId());

        req.setTypeId(selectedType.getId()); 

        req.setMessage(details);

        req.setStatus(false); 

        req.setDate(LocalDate.now());

        boolean inserted = reqDAO.insert(req);

        if (inserted) {
            System.out.println("\nRequest submitted successfully!");
        } else {
            System.out.println("\nFailed to submit request to database");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void processRequest(Scanner scanner, User currentUser) {
        ConsoleFormatter.printHeader("PROCESS REQUEST");

        int requestId = InputUtils.readInt(scanner, "Enter Request ID to process (0 to cancel): ");
        if (requestId == 0) return;

        Requests request = reqDAO.findById(requestId);

        if (request == null) {
            System.out.println("Error: Request ID not found");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        if (!hasPermissionToManageRequest(currentUser, request)) {
            System.out.println("Access Denied, request belongs to an employee outside your managed departments");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        User targetUser = userDAO.findById(request.getUserId());

        String requesterName = (targetUser != null) 
            ? targetUser.getFirstName() + " " + targetUser.getLastName() 
            : "User ID: " + request.getUserId();

        System.out.println("\nSelected Request Details:");
        System.out.println("Requester  : " + requesterName);
        System.out.println("Details    : " + request);

        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Cancel Action");

        int action = InputUtils.readInt(scanner, "Choose action: ");

        switch (action) {
            case 1 -> {
                request.setStatus(true);
                boolean success = reqDAO.update(request);
                if (success) {
                    System.out.println("\nRequest #" + requestId + " has been SOLVED");
                } else {
                    System.out.println("\nFailed to update request status in database");
                }
            }
            case 2 -> {
                boolean success = reqDAO.delete(request.getId());
                if (success) {
                    System.out.println("\nRequest #" + requestId + " has been REJECTED (Deleted)");
                } else {
                    System.out.println("\nFailed to update request status in database");
                }
            }
            case 3 -> System.out.println("\nOperation canceled");
            default -> System.out.println("\nInvalid choice");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    public boolean hasPermissionToManageRequest(User currentUser, Requests request) {
        if (currentUser.isAdmin()) {
            return true;
        }

        if (currentUser.isManager()) {
            List<Integer> managerDeptIds = getDepartmentIdsForUser(currentUser.getId());
            List<Integer> requesterDeptIds = getDepartmentIdsForUser(request.getUserId());

            for (Integer deptId : requesterDeptIds) {
                if (managerDeptIds.contains(deptId)) {
                    return true;
                }
            }
        }

        return false;
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