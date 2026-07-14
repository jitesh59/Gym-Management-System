package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * DateUtil.java
 * Utility class for date parsing, formatting, and membership expiry calculation.
 */
public class DateUtil {

    public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String format(LocalDate date) {
        if (date == null) return "";
        return date.format(DISPLAY_FORMAT);
    }

    public static LocalDate parse(String text) throws DateTimeParseException {
        return LocalDate.parse(text.trim(), DISPLAY_FORMAT);
    }

    /**
     * Calculates the expiry date based on plan name and a starting date.
     */
    public static LocalDate calculateExpiry(LocalDate startDate, String planName) {
        if (startDate == null) startDate = LocalDate.now();
        switch (planName) {
            case "Monthly":
                return startDate.plusMonths(1);
            case "Quarterly":
                return startDate.plusMonths(3);
            case "Half-Yearly":
                return startDate.plusMonths(6);
            case "Annual":
                return startDate.plusMonths(12);
            default:
                return startDate.plusMonths(1);
        }
    }

    public static boolean isExpired(LocalDate expiryDate) {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }
}
