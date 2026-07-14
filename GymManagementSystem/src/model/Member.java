package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Member.java
 * Represents a Gym Member entity.
 * Demonstrates Encapsulation (private fields with getters/setters).
 */
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String fullName;
    private String gender;
    private int age;
    private String phoneNumber;
    private String email;
    private String address;
    private double height;   // in cm
    private double weight;   // in kg
    private String membershipPlan;   // Monthly, Quarterly, Half-Yearly, Annual
    private LocalDate joinDate;
    private LocalDate expiryDate;
    private String emergencyContact;
    private String photoPath;        // optional path to uploaded photo
    private String status;           // Active, Expired

    public Member() {
    }

    public Member(String memberId, String fullName, String gender, int age, String phoneNumber,
                  String email, String address, double height, double weight, String membershipPlan,
                  LocalDate joinDate, LocalDate expiryDate, String emergencyContact) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.height = height;
        this.weight = weight;
        this.membershipPlan = membershipPlan;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
        this.emergencyContact = emergencyContact;
        this.status = "Active";
    }

    // ---------- BMI Calculation (Polymorphism candidate / business logic) ----------
    public double getBMI() {
        if (height <= 0) return 0;
        double heightInMeters = height / 100.0;
        return Math.round((weight / (heightInMeters * heightInMeters)) * 100.0) / 100.0;
    }

    public String getBMICategory() {
        double bmi = getBMI();
        if (bmi == 0) return "N/A";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    public void refreshStatus() {
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            this.status = "Expired";
        } else {
            this.status = "Active";
        }
    }

    // ---------- Getters and Setters ----------
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getMembershipPlan() { return membershipPlan; }
    public void setMembershipPlan(String membershipPlan) { this.membershipPlan = membershipPlan; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getStatus() {
        refreshStatus();
        return status;
    }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return memberId + " - " + fullName;
    }
}
