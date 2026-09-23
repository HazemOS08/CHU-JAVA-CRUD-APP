package Service_Layer.Controllers;

import java.util.List;
import java.util.Scanner;

import DAO.DepartmentsDAO;
import DAO.DAO_Implementation.DepartmentsDAOImp;
import Table_Classes.Departments;
import Table_Classes.User;
import Utilities.ConsoleFormatter;
import Utilities.InputUtils;

public class DepartmentController {

    private final DepartmentsDAO departmentDAO = new DepartmentsDAOImp();


    public void runDepartmentController(Scanner scanner, User currentUser) {

        if (!currentUser.isAdmin()) {
            System.out.println("Access Denied, only administrators can access Department Management");

            InputUtils.readString(scanner, "\nPress Enter to return...");

            return;
        }

        boolean back = false;

        while (!back) {

            ConsoleFormatter.printHeader("ADMIN: DEPARTMENT MANAGEMENT");

            System.out.println("1. View All Departments");
            System.out.println("2. Add New Department");
            System.out.println("3. Update Department");
            System.out.println("4. Delete Department");
            System.out.println("5. Back to Main Menu");

            int choice = InputUtils.readInt(scanner, "\nSelect an option: ");

        switch (choice) {

                case 1 -> viewAllDepartments(scanner);
                case 2 -> addDepartment(scanner);
                case 3 -> updateDepartment(scanner);
                case 4 -> deleteDepartment(scanner);
                case 5 -> back = true;
                default -> System.out.println("Invalid selection. Please try again.");

        }
     }
}

    private void viewAllDepartments(Scanner scanner) {

        ConsoleFormatter.printHeader("CHU SAINTE JUSTINE DEPARTMENTS");

        List<Departments> departments = departmentDAO.findAll();

        if (departments == null || departments.isEmpty()) {

            System.out.println("No departments found.");
            
        } else {

            displayDepartmentTable(departments);

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void addDepartment(Scanner scanner) {

        ConsoleFormatter.printHeader("ADD NEW DEPARTMENT");

        String name = InputUtils.readString(scanner, "Enter Department Name: ");

        if (name.trim().isEmpty()) {

            System.out.println("Error, department name cannot be empty");

            return;
        }

        Departments newDept = new Departments();
        newDept.setName(name);

        boolean added = departmentDAO.insert(newDept);

        if (added) {

            System.out.println("\nDepartment '" + name + "' created successfully!");

        } else {

            System.out.println("\nError, failed to create department");

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");

    }

    private void updateDepartment(Scanner scanner) {

        ConsoleFormatter.printHeader("UPDATE DEPARTMENT");

        List<Departments> departments = departmentDAO.findAll();

        if (departments == null || departments.isEmpty()) {

            System.out.println("No departments available to update");

            InputUtils.readString(scanner, "\nPress Enter to return...");

            return;
        }

        displayDepartmentTable(departments);

        int id = InputUtils.readInt(scanner, "\nEnter Department ID to update (0 to cancel): ");

        if (id == 0) return;

        Departments target = departmentDAO.findById(id);

        if (target == null) {

            System.out.println("Error, department ID not found");

            InputUtils.readString(scanner, "\nPress Enter to return...");

            return;

        }

        String newName = InputUtils.readString(scanner, "Enter new name for " + target.getName() + ": ");

        if (!newName.trim().isEmpty()) {

            target.setName(newName);

            boolean updated = departmentDAO.update(target);

            if (updated) {

                System.out.println("\nDepartment updated successfully!");

            } else {

                System.out.println("\nError: Failed to update department");

            }
        } else {

            System.out.println("Update canceled. Name cannot be empty");

        }

        InputUtils.readString(scanner, "\nPress Enter to return...");
    }

    private void deleteDepartment(Scanner scanner) {

        ConsoleFormatter.printHeader("DELETE DEPARTMENT");

        List<Departments> departments = departmentDAO.findAll();

        if (departments == null || departments.isEmpty()) {

            System.out.println("No departments available to delete");

            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        displayDepartmentTable(departments);

        int id = InputUtils.readInt(scanner, "\nEnter Department ID to delete (0 to cancel): ");

        if (id == 0) return;

        Departments target = departmentDAO.findById(id);

        if (target == null) {

            System.out.println("Error, department ID not found");
            InputUtils.readString(scanner, "\nPress Enter to return...");
            return;
        }

        String confirm = InputUtils.readString(scanner, 

                "Are you sure you want to delete '" + target.getName() + "'? (Y/N): ");

        if (confirm.equalsIgnoreCase("Y")) {

            boolean deleted = departmentDAO.delete(id);

            if (deleted) {

                System.out.println("\nDepartment deleted successfully!");

            } else {

                System.out.println("\nError, could not delete department (it may have assigned users or linked records).");
            }

        } else {

            System.out.println("Deletion canceled");
        }

        InputUtils.readString(scanner, "\nPress Enter to return...");

    }

    private void displayDepartmentTable(List<Departments> departments) {



        for (Departments d : departments) {
            
            System.out.println(d);
        }
        
    }
}
