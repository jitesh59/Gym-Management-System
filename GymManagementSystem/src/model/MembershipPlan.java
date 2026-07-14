package model;

import java.io.Serializable;

/**
 * MembershipPlan.java
 * Represents a membership plan type with duration and price.
 */
public class MembershipPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String planName;   // Monthly, Quarterly, Half-Yearly, Annual
    private int durationInMonths;
    private double price;

    public MembershipPlan() {
    }

    public MembershipPlan(String planName, int durationInMonths, double price) {
        this.planName = planName;
        this.durationInMonths = durationInMonths;
        this.price = price;
    }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public int getDurationInMonths() { return durationInMonths; }
    public void setDurationInMonths(int durationInMonths) { this.durationInMonths = durationInMonths; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return planName + " (" + durationInMonths + " months) - Rs." + price;
    }
}
