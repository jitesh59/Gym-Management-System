package controller;

import model.*;
import utils.FileManager;

import java.time.LocalDate;
import java.util.List;

/**
 * ReportController.java
 * Generates human-readable text reports for the Report Module and
 * exports them to .txt files using FileManager.
 */
public class ReportController {

    private MemberController memberController;
    private TrainerController trainerController;
    private EquipmentController equipmentController;
    private AttendanceController attendanceController;
    private PaymentController paymentController;

    public ReportController(MemberController memberController, TrainerController trainerController,
                              EquipmentController equipmentController, AttendanceController attendanceController,
                              PaymentController paymentController) {
        this.memberController = memberController;
        this.trainerController = trainerController;
        this.equipmentController = equipmentController;
        this.attendanceController = attendanceController;
        this.paymentController = paymentController;
    }

    public String generateActiveMembersReport() {
        StringBuilder sb = new StringBuilder("===== ACTIVE MEMBERS REPORT =====\n\n");
        for (Member m : memberController.getAllMembers()) {
            if ("Active".equals(m.getStatus())) {
                sb.append(String.format("%-8s %-20s %-12s %-12s%n",
                        m.getMemberId(), m.getFullName(), m.getMembershipPlan(), m.getExpiryDate()));
            }
        }
        return sb.toString();
    }

    public String generateExpiredMembersReport() {
        StringBuilder sb = new StringBuilder("===== EXPIRED MEMBERS REPORT =====\n\n");
        for (Member m : memberController.getAllMembers()) {
            if ("Expired".equals(m.getStatus())) {
                sb.append(String.format("%-8s %-20s %-12s %-12s%n",
                        m.getMemberId(), m.getFullName(), m.getMembershipPlan(), m.getExpiryDate()));
            }
        }
        return sb.toString();
    }

    public String generateMonthlyRevenueReport(int year, int month) {
        double revenue = paymentController.getMonthlyRevenue(year, month);
        StringBuilder sb = new StringBuilder("===== MONTHLY REVENUE REPORT =====\n\n");
        sb.append("Year: ").append(year).append("  Month: ").append(month).append("\n");
        sb.append("Total Revenue Collected: Rs.").append(revenue).append("\n");
        return sb.toString();
    }

    public String generateAttendanceReport(int year, int month) {
        StringBuilder sb = new StringBuilder("===== MONTHLY ATTENDANCE REPORT =====\n\n");
        List<Attendance> records = attendanceController.getMonthlyAttendance(year, month);
        for (Attendance a : records) {
            sb.append(String.format("%-10s %-8s %-20s %-10s%n",
                    a.getDate(), a.getMemberId(), a.getMemberName(), a.getStatus()));
        }
        return sb.toString();
    }

    public String generateEquipmentReport() {
        StringBuilder sb = new StringBuilder("===== EQUIPMENT REPORT =====\n\n");
        for (Equipment e : equipmentController.getAllEquipment()) {
            sb.append(String.format("%-8s %-20s Qty:%-5d Status:%-15s %s%n",
                    e.getEquipmentId(), e.getEquipmentName(), e.getQuantity(), e.getStatus(),
                    e.isLowStock() ? "[LOW STOCK]" : ""));
        }
        return sb.toString();
    }

    public String generateTrainerReport() {
        StringBuilder sb = new StringBuilder("===== TRAINER REPORT =====\n\n");
        for (Trainer t : trainerController.getAllTrainers()) {
            sb.append(String.format("%-8s %-20s %-15s Exp:%-3d Members:%-3d%n",
                    t.getTrainerId(), t.getName(), t.getSpecialization(),
                    t.getExperience(), t.getAssignedMemberCount()));
        }
        return sb.toString();
    }

    public void exportReport(String fileName, String content) {
        FileManager.writeTextReport(fileName, content);
    }
}
