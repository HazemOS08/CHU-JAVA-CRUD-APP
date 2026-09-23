package Service_Layer.Controllers;
import java.util.List;
import java.util.Scanner;
import DAO.UserDAO;
import DAO.DAO_Implementation.UserDAOImp;
import Service_Layer.Viewers.UserViewer;
import Table_Classes.User;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;
import Utilities.PasswordRulesCheck;

public class UserController {

    private final UserDAO userDAO = new UserDAOImp();

    private final UserViewer userViewer = new UserViewer();

    public void runUserController(Scanner scanner, User currentUser) {

        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("USER ACCOUNT MANAGEMENT");

            System.out.println("1. View My Profile");

            System.out.println("2. Update My Account Information");

            if (currentUser.isAdmin()) {

                System.out.println("3. Admin: Manage / Reset User Account");

                System.out.println("4. Back to Main Menu");

            } else {

                System.out.println("3. Back to Main Menu");

            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isAdmin()) {

                switch (choice) {

                    case 1 -> userViewer.displayMyProfile(currentUser, scanner);

                    case 2 -> updateSelfProfile(scanner, currentUser);

                    case 3 -> adminManageUser(scanner);

                    case 4 -> back = true;

                    default -> System.out.println("Invalid selection");

                }

            } else {

                switch (choice) {

                    case 1 -> userViewer.displayMyProfile(currentUser, scanner);

                    case 2 -> updateSelfProfile(scanner, currentUser);

                    case 3 -> back = true;

                    default -> System.out.println("Invalid selection.");

                }

            }

        }

    }

    //user profile update: username, email, cellular, or password

    private void updateSelfProfile(Scanner scanner, User currentUser) {

        boolean done = false;

        while (!done) {

            ConsoleFormatter.printHeader("UPDATE MY ACCOUNT INFO");

            System.out.println("1. Update Username (" + currentUser.getUsername() + ")");

            System.out.println("2. Update Email (" + currentUser.getEmail() + ")");

            System.out.println("3. Update Cellular (" + currentUser.getCellular() + ")");

            System.out.println("4. Change Password");

            System.out.println("5. Done / Back");

            int choice = InputUtils.readInt(scanner, "\nSelect field to update: ");

            switch (choice) {

                case 1 -> {

                    String newUsername = InputUtils.readUsername(scanner, "Enter new username");

                    if (isUsernameTaken(newUsername, currentUser.getId())) {

                        System.out.println("Error: Username '" + newUsername + "' is already in use.");

                    } else {

                        currentUser.setUsername(newUsername);

                        saveUser(currentUser, "Username updated successfully!");

                    }

                }

                case 2 -> {

                    String newEmail = InputUtils.readEmail(scanner, "Enter new email");

                    currentUser.setEmail(newEmail);

                    saveUser(currentUser, "Email updated successfully!");

                }

                case 3 -> {

                    String newCell = InputUtils.readCellular(scanner, "Enter new cellular number");

                    currentUser.setCellular(newCell);

                    saveUser(currentUser, "Cellular number updated successfully!");

                }

                case 4 -> {

                    String oldPass = InputUtils.readString(scanner, "Enter current password: ");

                    //compare hashed input against the stored password hash using 
                    if (!PasswordRulesCheck.hashPassword(oldPass).equals(currentUser.getPassword())) {

                        System.out.println("Error: Current password incorrect.");

                    } else {

                        String newPass = InputUtils.readPassword(scanner, "Enter new password");

                        currentUser.setPassword(PasswordRulesCheck.hashPassword(newPass));

                        saveUser(currentUser, "Password changed successfully!");

                    }

                }

                case 5 -> done = true;

                default -> System.out.println("Invalid selection.");

            }

        }

    }

    //search user by id, username, first name, or last name and edit details

    private void adminManageUser(Scanner scanner) {

        ConsoleFormatter.printHeader("ADMIN USER MANAGEMENT");

        User targetUser = searchUserForAdmin(scanner);

        if (targetUser == null) return;

        userViewer.displayUserProfile(targetUser);

        System.out.println("\nSelect Action:");

        System.out.println("1. Reset Password");

        System.out.println("2. Edit Account Information");

        System.out.println("3. Cancel");

        int action = InputUtils.readInt(scanner, "\nChoose option: ");

        switch (action) {

            case 1 -> {

                String newPass = InputUtils.readPassword(scanner, "Enter temporary/new password for " + targetUser.getFirstName());

                targetUser.setPassword(PasswordRulesCheck.hashPassword(newPass));

                saveUser(targetUser, "Password reset successfully for User #" + targetUser.getId());

            }

            case 2 -> adminEditUser(scanner, targetUser);

            case 3 -> System.out.println("Operation canceled");

            default -> System.out.println("Invalid selection");

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");

    }

    private User searchUserForAdmin(Scanner scanner) {

        System.out.println("1. Search by User ID");

        System.out.println("2. Search by Username");

        System.out.println("3. Search by First Name");

        System.out.println("4. Search by Last Name");

        System.out.println("5. Cancel");

        int option = InputUtils.readInt(scanner, "\nChoose search criteria: ");

        switch (option) {

            case 1 -> {

                int id = InputUtils.readInt(scanner, "Enter User ID (0 to cancel): ");

                if (id == 0) return null;

                User user = userDAO.findById(id);

                if (user == null) System.out.println("User ID not found");

                return user;

            }

            case 2 -> {

                String username = InputUtils.readUsername(scanner, "Enter Username");

                User user = userDAO.findByUsername(username);

                if (user == null) System.out.println("Username not found");

                return user;

            }

            case 3 -> {

                String firstName = InputUtils.readString(scanner, "Enter First Name: ");

                List<User> users = userDAO.findByFirstName(firstName);

                return selectUserFromList(scanner, users);

            }

            case 4 -> {

                String lastName = InputUtils.readString(scanner, "Enter Last Name: ");

                List<User> users = userDAO.findByLastName(lastName);

                return selectUserFromList(scanner, users);

            }

            default -> { return null; }

        }

    }

    private User selectUserFromList(Scanner scanner, List<User> users) {

        if (users == null || users.isEmpty()) {

            System.out.println("No matching users found.");

            return null;

        }

        if (users.size() == 1) {

            return users.get(0);

        }

        System.out.println("\nMatches found:");

        userViewer.displayUserList(users);

        int selectedId = InputUtils.readInt(scanner, "\nEnter User ID from list (0 to cancel): ");

        if (selectedId == 0) return null;

        return userDAO.findById(selectedId);

    }

    private void adminEditUser(Scanner scanner, User targetUser) {

        System.out.println("\n--- Updating User #" + targetUser.getId() + " ---");

        String newUsername = InputUtils.readUsername(scanner, "New Username");

        if (isUsernameTaken(newUsername, targetUser.getId())) {

            System.out.println("Error: Username '" + newUsername + "' is already taken. Username skipped.");

        } else {

            targetUser.setUsername(newUsername);

        }

        String newEmail = InputUtils.readEmail(scanner, "New Email");

        targetUser.setEmail(newEmail);

        String newCell = InputUtils.readCellular(scanner, "New Cellular");

        targetUser.setCellular(newCell);

        saveUser(targetUser, "User profile updated successfully!");

    }

    private boolean isUsernameTaken(String username, int currentUserId) {

        User existing = userDAO.findByUsername(username);

        return existing != null && existing.getId() != currentUserId;

    }

    private void saveUser(User user, String successMsg) {

        boolean updated = userDAO.update(user);

        if (updated) {

            System.out.println("\n" + successMsg);

        } else {

            System.out.println("\nError, failed to save updates to database");

        }

    }

}