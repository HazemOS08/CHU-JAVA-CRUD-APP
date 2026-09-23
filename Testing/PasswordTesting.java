package Testing;
import Utilities.PasswordRulesCheck;
import Utilities.PasswordRulesCheck.SecureLoginSystem;

import java.util.Scanner;
/* 
  
  NOTE: This main class was generated with AI assistance (Gemini) strictly 
  for unit testing and verifying. logic and utilities were implemented independently
 */
public class PasswordTesting {

    public static void main(String[] args) {

        // =========================================================================
        // 1. AUTOMATED SUITE: Static Password Rule Validation
        // =========================================================================
        System.out.println("==================================================");
        System.out.println("       AUTOMATED PASSWORD STRENGTH TESTS          ");
        System.out.println("==================================================");

        String[] testPasswords = {
            "Admin123",      // VALID: 8+ chars, upper, lower, digit
            "pass",          // INVALID: Too short (<8)
            "ALLUPPERCASE1",  // INVALID: Missing lowercase
            "alllowercase1",  // INVALID: Missing uppercase
            "NoDigitsHere",  // INVALID: Missing digit
            "StrongP4ssword" // VALID: Meets all criteria
        };

        for (String pwd : testPasswords) {
            boolean isValid = PasswordRulesCheck.isStrongPassword(pwd);
            System.out.printf("Password: %-16s -> Strong: %s%n", 
                "\"" + pwd + "\"", 
                isValid ? "PASSED [✓]" : "FAILED [X]"
            );
        }

        // =========================================================================
        // 2. INTERACTIVE SUITE: Secure Login & Security Challenge
        // =========================================================================
        System.out.println("\n==================================================");
        System.out.println("       INTERACTIVE LOGIN SYSTEM TEST              ");
        System.out.println("==================================================");

        Scanner scanner = new Scanner(System.in);
        SecureLoginSystem loginSystem = new SecureLoginSystem();

        System.out.println("Testing interactive login flow...\n");
        
        // Executes the full login workflow (Username -> Password Check -> CAPTCHA Math Check)
        PasswordRulesCheck.Pair<String, String> credentials = loginSystem.login(scanner);

        if (credentials != null) {
            System.out.println("\nLogin Successful!");
            System.out.println("Authenticated User: " + credentials.getKey());
            System.out.println("Password accepted by system.");
        } else {
            System.out.println("\nLogin Failed. Please re-check your inputs.");
        }

        scanner.close();
    }
}

// =========================================================================
// MOCK INPUTUTILS (Provides standalone compilation if InputUtils is absent)
// =========================================================================
class InputUtils {
    public static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }
}
