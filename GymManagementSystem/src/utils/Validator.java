package utils;

import java.util.regex.Pattern;

/**
 * Validator.java
 * Centralized input validation utility used across all modules
 * to keep validation logic consistent and reusable.
 */
public class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return isNotEmpty(phone) && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidAge(int age) {
        return age >= 5 && age <= 100;
    }

    public static boolean isPositiveNumber(double number) {
        return number > 0;
    }

    public static boolean isValidNumberString(String value) {
        if (!isNotEmpty(value)) return false;
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidIntegerString(String value) {
        if (!isNotEmpty(value)) return false;
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
