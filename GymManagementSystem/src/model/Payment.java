package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Payment.java
 * Represents a fee payment / receipt record.
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String receiptNumber;
    private String memberId;
    private String memberName;
    private String membershipPlan;
    private double amount;
    private double gst;          // optional GST amount
    private double discount;     // discount applied
    private LocalDate date;
    private String paymentMode;  // Cash, Card, UPI, Online
    private String status;       // Paid, Pending

    public Payment() {
    }

    public Payment(String receiptNumber, String memberId, String memberName, String membershipPlan,
                    double amount, double gst, double discount, LocalDate date, String paymentMode, String status) {
        this.receiptNumber = receiptNumber;
        this.memberId = memberId;
        this.memberName = memberName;
        this.membershipPlan = membershipPlan;
        this.amount = amount;
        this.gst = gst;
        this.discount = discount;
        this.date = date;
        this.paymentMode = paymentMode;
        this.status = status;
    }

    public double getTotalAmount() {
        return Math.round((amount + gst - discount) * 100.0) / 100.0;
    }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMembershipPlan() { return membershipPlan; }
    public void setMembershipPlan(String membershipPlan) { this.membershipPlan = membershipPlan; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getGst() { return gst; }
    public void setGst(double gst) { this.gst = gst; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return receiptNumber + " | " + memberName + " | Rs." + getTotalAmount();
    }
}
