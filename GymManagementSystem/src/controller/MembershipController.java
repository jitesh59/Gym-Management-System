package controller;

import model.Member;
import model.MembershipPlan;
import utils.DateUtil;
import utils.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MembershipController.java
 * Handles membership plan CRUD, expiry date calculation, renewal,
 * and reminders for members whose membership is about to expire.
 */
public class MembershipController {

    private static final String PLANS_FILE = "plans.dat";

    private List<MembershipPlan> plans;

    public MembershipController() {
        plans = FileManager.loadList(PLANS_FILE);
        if (plans.isEmpty()) {
            // seed default plans
            plans.add(new MembershipPlan("Monthly", 1, 1500));
            plans.add(new MembershipPlan("Quarterly", 3, 4000));
            plans.add(new MembershipPlan("Half-Yearly", 6, 7500));
            plans.add(new MembershipPlan("Annual", 12, 14000));
            save();
        }
    }

    public List<MembershipPlan> getAllPlans() {
        return new ArrayList<>(plans);
    }

    public boolean addPlan(MembershipPlan plan) {
        for (MembershipPlan p : plans) {
            if (p.getPlanName().equalsIgnoreCase(plan.getPlanName())) return false;
        }
        plans.add(plan);
        save();
        return true;
    }

    public boolean updatePlan(MembershipPlan updated) {
        for (int i = 0; i < plans.size(); i++) {
            if (plans.get(i).getPlanName().equalsIgnoreCase(updated.getPlanName())) {
                plans.set(i, updated);
                save();
                return true;
            }
        }
        return false;
    }

    public boolean deletePlan(String planName) {
        boolean removed = plans.removeIf(p -> p.getPlanName().equalsIgnoreCase(planName));
        if (removed) save();
        return removed;
    }

    public MembershipPlan getPlanByName(String planName) {
        for (MembershipPlan p : plans) {
            if (p.getPlanName().equalsIgnoreCase(planName)) return p;
        }
        return null;
    }

    public LocalDate calculateExpiryDate(LocalDate startDate, String planName) {
        return DateUtil.calculateExpiry(startDate, planName);
    }

    /**
     * Renews a member's plan starting from today (or from existing expiry if
     * it is still in the future), and returns the new expiry date.
     */
    public LocalDate renewMembership(Member member, String planName) {
        LocalDate start = member.getExpiryDate() != null && member.getExpiryDate().isAfter(LocalDate.now())
                ? member.getExpiryDate() : LocalDate.now();
        LocalDate newExpiry = calculateExpiryDate(start, planName);
        member.setMembershipPlan(planName);
        member.setExpiryDate(newExpiry);
        member.refreshStatus();
        return newExpiry;
    }

    /**
     * Returns members (from the given list) whose membership will expire
     * within the given number of days - used for renewal reminders.
     */
    public List<Member> getMembersNearingExpiry(List<Member> allMembers, int withinDays) {
        List<Member> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusDays(withinDays);
        for (Member m : allMembers) {
            if (m.getExpiryDate() != null
                    && !m.getExpiryDate().isBefore(today)
                    && !m.getExpiryDate().isAfter(cutoff)) {
                result.add(m);
            }
        }
        return result;
    }

    private void save() {
        FileManager.saveList(PLANS_FILE, plans);
    }
}
