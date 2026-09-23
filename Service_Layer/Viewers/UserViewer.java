package Service_Layer.Viewers;

import java.util.List;
import java.util.Scanner;

import DAO.UserDAO;
import DAO.DAO_Implementation.UserDAOImp;
import Table_Classes.User;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class UserViewer {

    private final UserDAO userDAO = new UserDAOImp();
    
    public void runUserViewer(Scanner scanner, User currentUser) {

        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("USER VIEWER MENU");

            System.out.println("1. View My Profile");

            if (currentUser.isAdmin()) {

                System.out.println("2. Admin: View All System Users");
                System.out.println("3. Back to Main Menu");

            } else {

                System.out.println("2. Back to Main Menu");

            }

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

            if (currentUser.isAdmin()) {

            switch (choice) {

                    case 1 -> displayMyProfile(currentUser, scanner);

                    case 2 -> {

                        List<User> allUsers = userDAO.findAll();

                        displayUserList(allUsers);

                        InputUtils.readString(scanner, "\nPress Enter to return...");

                }
                    case 3 -> back = true;

                    default -> System.out.println("Invalid selection. Please try again.");
                }

            } else {
                switch (choice) {

                    case 1 -> displayMyProfile(currentUser, scanner);

                    case 2 -> back = true;

                    default -> System.out.println("Invalid selection. Please try again");

            }
         }
        }
    }

    public void displayMyProfile(User currentUser, Scanner scanner) {

        // display the latest updates

        User freshUser = userDAO.findById(currentUser.getId());
        if (freshUser == null) {
            freshUser = currentUser;
        }

        ConsoleFormatter.printHeader("MY PROFILE");
        System.out.println(freshUser);

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    public void displayUserProfile(User user) {
        ConsoleFormatter.printHeader("USER PROFILE: " + user.getFirstName() + " " + user.getLastName());
        System.out.println(user);
    }


     
    public void displayUserList(List<User> users) {
        
        ConsoleFormatter.printHeader("All CHU SAINTE JUSTINE Users");

        if (users == null || users.isEmpty()) {

            System.out.println("No matching users found.");

            return;
        }



        for (User u : users) {

             System.out.println(u);
       }


    }
}