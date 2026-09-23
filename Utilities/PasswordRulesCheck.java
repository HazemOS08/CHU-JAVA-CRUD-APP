package Utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class PasswordRulesCheck {
    public static void main(String[] args) {
    System.out.println("Generated Hash: " + hashPassword("pass123"));
    }

    public static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }



    // Interface for anonymous inner class 
    interface SecurityChallenge {
        boolean executeChallenge();
    }
    
    //for external class usage
    public static boolean isStrongPassword(String password) {
        return SecureLoginSystem.PasswordRules.isStrongPassword(password);
    }


    public static class SecureLoginSystem {

  
        public static class PasswordRules {

            public static boolean isStrongPassword(String password) {
                if (password == null || password.length() < 8) {
                    return false;
                }
                boolean hasUpper = false;
                boolean hasLower = false;
                boolean hasDigit = false;

                for (char c : password.toCharArray()) {
                    if (Character.isUpperCase(c)) hasUpper = true;
                    else if (Character.isLowerCase(c)) hasLower = true;
                    else if (Character.isDigit(c)) hasDigit = true;
                }
                return hasUpper && hasLower && hasDigit; 
            }
        }

        // Login method returning a Pair<username, password> if valid
        public Pair<String, String> login(Scanner scanner) {

            String username = InputUtils.readString(scanner, "Enter username: ");
            String password = InputUtils.readString(scanner, "Enter password: ");

            if (!PasswordRules.isStrongPassword(password)) {
                System.out.println("Password does not meet security rules (8+ chars, upper, lower, digit)");
                return null;
            }

            //security Challenge
            SecurityChallenge challenge = new SecurityChallenge() {
                @Override
                public boolean executeChallenge() {
                    Random rand = new Random();
                    int num1 = rand.nextInt(10) + 1;
                    int num2 = rand.nextInt(10) + 1;
                    int answer = num1 + num2;

                    int userAns = InputUtils.readInt(scanner, "Security Check: What is " + num1 + " + " + num2 + "? ");
                    return userAns == answer;
                }
            };


            if (!challenge.executeChallenge()) {
                System.out.println("Security check failed. Login denied");
                return null;
            }

            //return valid credentials pair if both checks pass
            return new Pair<>(username, password);
        }
    }
    
    //check for valid username (no spaces and at least 6 chars)
    public static boolean isValidUsername(String username) {
    if (username == null || username.isBlank()) {
        return false;
    }
    return !username.contains(" ") && username.length() >= 6;
    }
    
    //check for email pattern text@text.text

    public static boolean isValidEmail(String email){

        if (email == null) { 
            return false;
        }

        String trimmed = email.trim();

        int atIndex = trimmed.indexOf('@');

        int lastAtIndex = trimmed.lastIndexOf('@');

        int dotIndex = trimmed.lastIndexOf('.');

        
        if (atIndex == -1 || atIndex != lastAtIndex) {
            return false;
        }

        
        if (dotIndex == -1 || dotIndex <= atIndex) {
            return false;
        }

        
        String prefix = trimmed.substring(0, atIndex);
        String domain = trimmed.substring(atIndex + 1, dotIndex);
        String extension = trimmed.substring(dotIndex + 1);

        if (prefix.isBlank() || domain.isBlank() || extension.isBlank()) {
            return false;
        }

        
        return !trimmed.contains(" ");

    }
    
    //check for cellular pattern +nnn nnn-nnn-nnnn

    public static boolean isValidCellular(String cel){

     if (cel == null) {

            return false;

        }


        String trimmed = cel.trim();

        
        if (!trimmed.startsWith("+")) {
            return false;
        }

        int spaceIndex = trimmed.indexOf(' ');
        if (spaceIndex == -1) {
            return false;
        }

        
        String codeStr = trimmed.substring(1, spaceIndex);
        if (codeStr.isBlank() || codeStr.length() > 3) {
            return false;
        }

        
        for (char c : codeStr.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        int countryCode = Integer.parseInt(codeStr);
        if (countryCode <= 0 || countryCode > 999) {
            return false;
        }

        
        String numberPart = trimmed.substring(spaceIndex + 1);
        
        
        if (numberPart.length() != 12) {
            return false;
        }

        
        if (numberPart.charAt(3) != '-' || numberPart.charAt(7) != '-') {
            return false;
        }

        
        for (int i = 0; i < numberPart.length(); i++) {

            if (i == 3 || i == 7) continue;

            if (!Character.isDigit(numberPart.charAt(i))) {

                return false;
            }
        }

        return true;
    }

    //basic java built in hash

public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into a lowercase hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Ensures leading zeros aren't dropped
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }



}




