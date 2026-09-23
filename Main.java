
import java.util.Scanner;
import Table_Classes.User;
import Ui.Menus;
import db.DatabaseInitializer;
public class Main {
public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseInitializer.initializeDatabase();

        while (true) {
            // 1. Prompt for login using your PasswordRulesCheck / UserDAO
            User currentUser = Menus.login(scanner); 
            
            if (currentUser == null) break; // User chose to exit
            
            // 2. Route to the appropriate CLI menu loop based on role
            if (currentUser.isAdmin()) {
                Menus.runAdminMenu(scanner, currentUser);
            } else if (currentUser.isManager()) {
                Menus.runManagerMenu(scanner, currentUser);
            } else {
                Menus.runStaffMenu(scanner, currentUser);
            }
        }
    }
}
