package controller;

import model.Payment;
import utils.FileManager;
import utils.IDGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentController.java
 * Handles fee collection, receipt generation, payment history, and
 * pending payment tracking.
 */
public class PaymentController {

    private static final String PAYMENTS_FILE = "payments.dat";

    private List<Payment> payments;

    public PaymentController() {
        payments = FileManager.loadList(PAYMENTS_FILE);
    }

    public String getNextReceiptNumber() {
        return IDGenerator.generateReceiptNumber(payments.size());
    }

    public Payment recordPayment(String memberId, String memberName, String plan, double amount,
                                   double gst, double discount, LocalDate date, String mode, String status) {
        Payment payment = new Payment(getNextReceiptNumber(), memberId, memberName, plan,
                amount, gst, discount, date, mode, status);
        payments.add(payment);
        save();
        return payment;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }

    public List<Payment> getPaymentsByMember(String memberId) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : payments) {
            if (p.getMemberId().equals(memberId)) result.add(p);
        }
        return result;
    }

    public List<Payment> getPendingPayments() {
        List<Payment> result = new ArrayList<>();
        for (Payment p : payments) {
            if ("Pending".equalsIgnoreCase(p.getStatus())) result.add(p);
        }
        return result;
    }

    public double getMonthlyRevenue(int year, int month) {
        double total = 0;
        for (Payment p : payments) {
            if (p.getDate().getYear() == year && p.getDate().getMonthValue() == month
                    && "Paid".equalsIgnoreCase(p.getStatus())) {
                total += p.getTotalAmount();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public String generateReceiptText(Payment p) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== GYM MANAGEMENT SYSTEM ==========\n");
        sb.append("             PAYMENT RECEIPT\n");
        sb.append("=============================================\n");
        sb.append("Receipt Number : ").append(p.getReceiptNumber()).append("\n");
        sb.append("Member Name    : ").append(p.getMemberName()).append("\n");
        sb.append("Member ID      : ").append(p.getMemberId()).append("\n");
        sb.append("Membership Plan: ").append(p.getMembershipPlan()).append("\n");
        sb.append("Amount         : Rs.").append(p.getAmount()).append("\n");
        sb.append("GST            : Rs.").append(p.getGst()).append("\n");
        sb.append("Discount       : Rs.").append(p.getDiscount()).append("\n");
        sb.append("Total Paid     : Rs.").append(p.getTotalAmount()).append("\n");
        sb.append("Date           : ").append(p.getDate()).append("\n");
        sb.append("Payment Mode   : ").append(p.getPaymentMode()).append("\n");
        sb.append("Status         : ").append(p.getStatus()).append("\n");
        sb.append("=============================================\n");
        sb.append("        Thank you for your payment!\n");
        sb.append("=============================================\n");
        return sb.toString();
    }

    private void save() {
        FileManager.saveList(PAYMENTS_FILE, payments);
    }
}
