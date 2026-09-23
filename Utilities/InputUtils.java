package Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {

public static int readInt(Scanner scanner, String prompt) {

        while (true) {

        System.out.print(prompt);

            String input = scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println("[!] Invalid input. Please enter a valid number.");
            }
        }
    }

public static String readString(Scanner scanner, String prompt) {

        System.out.print(prompt);

    return scanner.nextLine().trim();

    }

public static LocalDate readDate(Scanner scanner, String prompt) {

        while (true) {

            System.out.print(prompt + " (YYYY-MM-DD): ");

            String input = scanner.nextLine().trim();
            try {
                
                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {

                System.out.println("Invalid date format. Please use YYYY-MM-DD (e.g 2026-09-21)");
            }
       
        }
      }

public static LocalDateTime readDateTime(Scanner scanner, String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (true) {

            System.out.print(prompt + " (YYYY-MM-DD HH:mm): ");

            String input = scanner.nextLine().trim();
            try {
                
                return LocalDateTime.parse(input,formatter);

            } catch (DateTimeParseException e) {

                System.out.println("Invalid date format. Please use YYYY-MM-DD HH:mm (e.g 2026-09-21 10:30)");
            }
       
        }
      }

public static String readUsername(Scanner scanner, String prompt) {

    while (true) {

        System.out.print(prompt + ": ");

        String input = scanner.nextLine().trim();

        if (PasswordRulesCheck.isValidUsername(input)) {

            return input;
        }


        System.out.println("Invalid username, must be at least 6 characters long with no spaces");
    }
}

public static String readPassword(Scanner scanner, String prompt) {

    while (true) {

        System.out.print(prompt + ": ");

        String input = scanner.nextLine().trim();

        if (PasswordRulesCheck.isStrongPassword(input)) {

            return input;
        }

        System.out.println("Invalid password, must be at least 8 characters, containing an uppercase letter, lowercase letter, and a digit");
    }
}

public static String readEmail(Scanner scanner, String prompt) {

    while (true) {

        System.out.print(prompt + ": ");

        String input = scanner.nextLine().trim();

        if (PasswordRulesCheck.isValidEmail(input)) {
            return input;
        }

        System.out.println("Invalid email format, pls use text@domain.ext (eg user@example.com)");
    }
}

public static String readCellular(Scanner scanner, String prompt) {

    while (true) {

        System.out.print(prompt + " (+CCC NNN-NNN-NNNN): ");

        String input = scanner.nextLine().trim();

        if (PasswordRulesCheck.isValidCellular(input)) {

            return input;
        }

        System.out.println("Invalid phone format, pls use +countryCode number (e.g. +1 999-999-9999 or +001 999-999-9999)");
    }
}

}

