import model.*;
import utils.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SampleDataGenerator.java
 * NOT part of the shipped application package structure.
 * Run this ONCE to populate the data/ folder with realistic sample
 * records for members, trainers, equipment, attendance, payments and plans,
 * so the application has sample data to demonstrate on first run.
 *
 * Usage: javac -d bin -cp bin tools/SampleDataGenerator.java
 *        java -cp bin SampleDataGenerator
 */
public class SampleDataGenerator {
    public static void main(String[] args) {

        // ---------------- Users ----------------
        List<User> users = new ArrayList<>();
        users.add(new User("admin", "admin123", "What is your favorite color?", "blue"));
        FileManager.saveList("users.dat", users);

        // ---------------- Membership Plans ----------------
        List<MembershipPlan> plans = new ArrayList<>();
        plans.add(new MembershipPlan("Monthly", 1, 1500));
        plans.add(new MembershipPlan("Quarterly", 3, 4000));
        plans.add(new MembershipPlan("Half-Yearly", 6, 7500));
        plans.add(new MembershipPlan("Annual", 12, 14000));
        FileManager.saveList("plans.dat", plans);

        // ---------------- Members ----------------
        List<Member> members = new ArrayList<>();
        // A realistic mix of active and expired memberships relative to today's date.
        LocalDate today = LocalDate.now();
        members.add(buildMember("MEM001", "Rohit Sharma", "Male", 28, "9876543210", "rohit.sharma@example.com",
                "12 MG Road, Pune", 175, 78, "Annual", today.plusMonths(4), "9876500001"));
        members.add(buildMember("MEM002", "Priya Verma", "Female", 24, "9876543211", "priya.verma@example.com",
                "45 Park Street, Kolkata", 162, 58, "Quarterly", today.plusDays(20), "9876500002"));
        members.add(buildMember("MEM003", "Aman Gupta", "Male", 32, "9876543212", "aman.gupta@example.com",
                "7 Sector 21, Chandigarh", 180, 85, "Monthly", today.minusDays(5), "9876500003"));
        members.add(buildMember("MEM004", "Sneha Reddy", "Female", 27, "9876543213", "sneha.reddy@example.com",
                "22 Banjara Hills, Hyderabad", 158, 55, "Half-Yearly", today.plusMonths(2), "9876500004"));
        members.add(buildMember("MEM005", "Vikram Singh", "Male", 35, "9876543214", "vikram.singh@example.com",
                "9 Civil Lines, Jaipur", 172, 90, "Annual", today.minusMonths(1), "9876500005"));
        members.add(buildMember("MEM006", "Anita Nair", "Female", 30, "9876543215", "anita.nair@example.com",
                "3 MG Road, Kochi", 165, 62, "Monthly", today.plusDays(5), "9876500006"));
        members.add(buildMember("MEM007", "Karan Mehta", "Male", 22, "9876543216", "karan.mehta@example.com",
                "18 Andheri West, Mumbai", 178, 72, "Quarterly", today.plusMonths(1), "9876500007"));
        members.add(buildMember("MEM008", "Divya Iyer", "Female", 26, "9876543217", "divya.iyer@example.com",
                "56 T Nagar, Chennai", 160, 54, "Annual", today.minusDays(15), "9876500008"));
        FileManager.saveList("members.dat", members);

        // ---------------- Trainers ----------------
        List<Trainer> trainers = new ArrayList<>();
        Trainer t1 = new Trainer("TRN001", "Rajesh Kumar", 8, "Strength & Conditioning", "9123456780", 45000);
        t1.assignMember("MEM001");
        t1.assignMember("MEM005");
        Trainer t2 = new Trainer("TRN002", "Meera Joshi", 5, "Yoga & Flexibility", "9123456781", 35000);
        t2.assignMember("MEM002");
        t2.assignMember("MEM004");
        Trainer t3 = new Trainer("TRN003", "Suresh Patil", 10, "Bodybuilding", "9123456782", 50000);
        t3.assignMember("MEM003");
        Trainer t4 = new Trainer("TRN004", "Neha Kapoor", 4, "Cardio & Weight Loss", "9123456783", 32000);
        t4.assignMember("MEM006");
        t4.assignMember("MEM007");
        t4.assignMember("MEM008");
        trainers.add(t1);
        trainers.add(t2);
        trainers.add(t3);
        trainers.add(t4);
        FileManager.saveList("trainers.dat", trainers);

        // ---------------- Equipment ----------------
        List<Equipment> equipmentList = new ArrayList<>();
        equipmentList.add(new Equipment("EQP001", "Treadmill", 5, LocalDate.of(2023, 1, 15),
                LocalDate.of(2026, 7, 15), "Working"));
        equipmentList.add(new Equipment("EQP002", "Dumbbell Set (5-50kg)", 3, LocalDate.of(2022, 6, 10),
                LocalDate.of(2026, 12, 10), "Working"));
        equipmentList.add(new Equipment("EQP003", "Leg Press Machine", 2, LocalDate.of(2023, 3, 5),
                LocalDate.of(2026, 9, 5), "Working"));
        equipmentList.add(new Equipment("EQP004", "Rowing Machine", 1, LocalDate.of(2021, 11, 20),
                LocalDate.of(2026, 8, 1), "Under Maintenance"));
        equipmentList.add(new Equipment("EQP005", "Exercise Bike", 4, LocalDate.of(2023, 5, 18),
                LocalDate.of(2026, 11, 18), "Working"));
        equipmentList.add(new Equipment("EQP006", "Smith Machine", 1, LocalDate.of(2022, 9, 9),
                LocalDate.of(2026, 10, 9), "Working"));
        equipmentList.add(new Equipment("EQP007", "Yoga Mats", 20, LocalDate.of(2024, 1, 1),
                LocalDate.of(2027, 1, 1), "Working"));
        FileManager.saveList("equipment.dat", equipmentList);

        // ---------------- Attendance ----------------
        List<Attendance> attendanceList = new ArrayList<>();
        attendanceList.add(new Attendance("ATD001", "MEM001", "Rohit Sharma", today, "Present"));
        attendanceList.add(new Attendance("ATD002", "MEM002", "Priya Verma", today, "Present"));
        attendanceList.add(new Attendance("ATD003", "MEM003", "Aman Gupta", today, "Absent"));
        attendanceList.add(new Attendance("ATD004", "MEM004", "Sneha Reddy", today, "Present"));
        attendanceList.add(new Attendance("ATD005", "MEM005", "Vikram Singh", today.minusDays(1), "Present"));
        attendanceList.add(new Attendance("ATD006", "MEM006", "Anita Nair", today.minusDays(1), "Present"));
        attendanceList.add(new Attendance("ATD007", "MEM007", "Karan Mehta", today.minusDays(1), "Absent"));
        FileManager.saveList("attendance.dat", attendanceList);

        // ---------------- Payments ----------------
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("RCP001", "MEM001", "Rohit Sharma", "Annual", 14000, 0, 500,
                LocalDate.now().withDayOfMonth(3), "Card", "Paid"));
        payments.add(new Payment("RCP002", "MEM002", "Priya Verma", "Quarterly", 4000, 0, 0,
                LocalDate.now().withDayOfMonth(5), "UPI", "Paid"));
        payments.add(new Payment("RCP003", "MEM003", "Aman Gupta", "Monthly", 1500, 0, 0,
                LocalDate.now().withDayOfMonth(6), "Cash", "Paid"));
        payments.add(new Payment("RCP004", "MEM004", "Sneha Reddy", "Half-Yearly", 7500, 0, 250,
                LocalDate.now().withDayOfMonth(8), "Online", "Pending"));
        payments.add(new Payment("RCP005", "MEM005", "Vikram Singh", "Annual", 14000, 0, 1000,
                LocalDate.now().withDayOfMonth(2), "Card", "Paid"));
        FileManager.saveList("payments.dat", payments);

        System.out.println("Sample data generated successfully in the data/ folder:");
        System.out.println(" - users.dat (" + users.size() + " user)");
        System.out.println(" - members.dat (" + members.size() + " members)");
        System.out.println(" - trainers.dat (" + trainers.size() + " trainers)");
        System.out.println(" - equipment.dat (" + equipmentList.size() + " items)");
        System.out.println(" - attendance.dat (" + attendanceList.size() + " records)");
        System.out.println(" - payments.dat (" + payments.size() + " records)");
        System.out.println(" - plans.dat (" + plans.size() + " plans)");
    }

    private static Member buildMember(String id, String name, String gender, int age, String phone,
                                        String email, String address, double height, double weight,
                                        String plan, LocalDate expiry, String emergency) {
        LocalDate join = expiry.minusMonths(planMonths(plan));
        return new Member(id, name, gender, age, phone, email, address, height, weight, plan, join, expiry, emergency);
    }

    private static int planMonths(String plan) {
        switch (plan) {
            case "Monthly": return 1;
            case "Quarterly": return 3;
            case "Half-Yearly": return 6;
            case "Annual": return 12;
            default: return 1;
        }
    }
}
