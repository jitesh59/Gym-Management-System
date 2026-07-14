package utils;

/**
 * IDGenerator.java
 * Generates sequential, prefixed IDs for the different entities
 * (members, trainers, equipment, attendance, payments) based on
 * the current size of the corresponding collection.
 */
public class IDGenerator {

    public static String generateId(String prefix, int currentCount) {
        return String.format("%s%03d", prefix, currentCount + 1);
    }

    public static String generateMemberId(int count) {
        return generateId("MEM", count);
    }

    public static String generateTrainerId(int count) {
        return generateId("TRN", count);
    }

    public static String generateEquipmentId(int count) {
        return generateId("EQP", count);
    }

    public static String generateAttendanceId(int count) {
        return generateId("ATD", count);
    }

    public static String generateReceiptNumber(int count) {
        return generateId("RCP", count);
    }
}
