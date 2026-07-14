package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Trainer.java
 * Represents a Gym Trainer entity.
 */
public class Trainer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String trainerId;
    private String name;
    private int experience;          // years
    private String specialization;
    private String phone;
    private double salary;
    private List<String> assignedMembers; // list of member IDs

    public Trainer() {
        this.assignedMembers = new ArrayList<>();
    }

    public Trainer(String trainerId, String name, int experience, String specialization,
                   String phone, double salary) {
        this.trainerId = trainerId;
        this.name = name;
        this.experience = experience;
        this.specialization = specialization;
        this.phone = phone;
        this.salary = salary;
        this.assignedMembers = new ArrayList<>();
    }

    public void assignMember(String memberId) {
        if (!assignedMembers.contains(memberId)) {
            assignedMembers.add(memberId);
        }
    }

    public void removeMember(String memberId) {
        assignedMembers.remove(memberId);
    }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public List<String> getAssignedMembers() { return assignedMembers; }
    public void setAssignedMembers(List<String> assignedMembers) { this.assignedMembers = assignedMembers; }

    public int getAssignedMemberCount() { return assignedMembers.size(); }

    @Override
    public String toString() {
        return trainerId + " - " + name;
    }
}
