package view;

import controller.MemberController;
import controller.MembershipController;
import model.Member;
import model.MembershipPlan;
import utils.DateUtil;
import utils.UITheme;
import utils.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MembershipPanel.java
 * Manages membership plans (add/edit/delete) and allows renewing
 * a member's membership, plus shows upcoming renewal reminders.
 */
public class MembershipPanel extends JPanel {

    private final MembershipController membershipController;
    private final MemberController memberController;

    private DefaultTableModel plansTableModel;
    private JTable plansTable;
    private DefaultTableModel reminderTableModel;

    private JTextField planNameField, durationField, priceField;
    private JTextField renewMemberIdField;
    private JComboBox<String> renewPlanBox;

    public MembershipPanel(MembershipController membershipController, MemberController memberController) {
        this.membershipController = membershipController;
        this.memberController = memberController;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
        refreshPlansTable();
        refreshReminders();
    }

    private void buildUI() {
        JLabel title = new JLabel("Membership Management");
        title.setFont(UITheme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 15, 0));
        center.setOpaque(false);
        center.add(buildPlansPanel());
        center.add(buildRenewalAndReminderPanel());
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildPlansPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Membership Plans"));

        String[] cols = {"Plan Name", "Duration (Months)", "Price (Rs.)"};
        plansTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        plansTable = new JTable(plansTableModel);
        plansTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && plansTable.getSelectedRow() >= 0) {
                int row = plansTable.getSelectedRow();
                planNameField.setText((String) plansTableModel.getValueAt(row, 0));
                durationField.setText(String.valueOf(plansTableModel.getValueAt(row, 1)));
                priceField.setText(String.valueOf(plansTableModel.getValueAt(row, 2)));
            }
        });
        panel.add(new JScrollPane(plansTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        form.setOpaque(false);
        planNameField = new JTextField();
        durationField = new JTextField();
        priceField = new JTextField();
        form.add(new JLabel("Plan Name:")); form.add(planNameField);
        form.add(new JLabel("Duration (Months):")); form.add(durationField);
        form.add(new JLabel("Price (Rs.):")); form.add(priceField);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 6, 0));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        addBtn.addActionListener(e -> onAddPlan());
        updateBtn.addActionListener(e -> onUpdatePlan());
        deleteBtn.addActionListener(e -> onDeletePlan());
        buttons.add(addBtn); buttons.add(updateBtn); buttons.add(deleteBtn);
        form.add(new JLabel(""));
        form.add(buttons);

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildRenewalAndReminderPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        JPanel renewPanel = new JPanel(new GridLayout(4, 2, 6, 6));
        renewPanel.setBorder(BorderFactory.createTitledBorder("Renew Membership"));
        renewMemberIdField = new JTextField();
        renewPlanBox = new JComboBox<>();
        JButton renewBtn = new JButton("Renew");
        renewBtn.addActionListener(e -> onRenew());
        renewPanel.add(new JLabel("Member ID:")); renewPanel.add(renewMemberIdField);
        renewPanel.add(new JLabel("New Plan:")); renewPanel.add(renewPlanBox);
        renewPanel.add(new JLabel("")); renewPanel.add(renewBtn);

        JPanel reminderPanel = new JPanel(new BorderLayout());
        reminderPanel.setBorder(BorderFactory.createTitledBorder("Renewal Reminders (Next 7 Days)"));
        String[] cols = {"Member ID", "Name", "Plan", "Expiry Date"};
        reminderTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable reminderTable = new JTable(reminderTableModel);
        reminderPanel.add(new JScrollPane(reminderTable), BorderLayout.CENTER);

        panel.add(renewPanel, BorderLayout.NORTH);
        panel.add(reminderPanel, BorderLayout.CENTER);
        return panel;
    }

    private void refreshPlansTable() {
        plansTableModel.setRowCount(0);
        renewPlanBox.removeAllItems();
        for (MembershipPlan p : membershipController.getAllPlans()) {
            plansTableModel.addRow(new Object[]{p.getPlanName(), p.getDurationInMonths(), p.getPrice()});
            renewPlanBox.addItem(p.getPlanName());
        }
    }

    private void refreshReminders() {
        reminderTableModel.setRowCount(0);
        List<Member> nearingExpiry = membershipController.getMembersNearingExpiry(memberController.getAllMembers(), 7);
        for (Member m : nearingExpiry) {
            reminderTableModel.addRow(new Object[]{m.getMemberId(), m.getFullName(), m.getMembershipPlan(),
                    DateUtil.format(m.getExpiryDate())});
        }
    }

    private void onAddPlan() {
        if (!Validator.isNotEmpty(planNameField.getText()) || !Validator.isValidIntegerString(durationField.getText())
                || !Validator.isValidNumberString(priceField.getText())) {
            showError("Please fill all plan fields with valid values.");
            return;
        }
        MembershipPlan plan = new MembershipPlan(planNameField.getText().trim(),
                Integer.parseInt(durationField.getText().trim()), Double.parseDouble(priceField.getText().trim()));
        if (membershipController.addPlan(plan)) {
            refreshPlansTable();
            JOptionPane.showMessageDialog(this, "Plan added.");
        } else {
            showError("A plan with this name already exists.");
        }
    }

    private void onUpdatePlan() {
        if (!Validator.isNotEmpty(planNameField.getText()) || !Validator.isValidIntegerString(durationField.getText())
                || !Validator.isValidNumberString(priceField.getText())) {
            showError("Please fill all plan fields with valid values.");
            return;
        }
        MembershipPlan plan = new MembershipPlan(planNameField.getText().trim(),
                Integer.parseInt(durationField.getText().trim()), Double.parseDouble(priceField.getText().trim()));
        if (membershipController.updatePlan(plan)) {
            refreshPlansTable();
            JOptionPane.showMessageDialog(this, "Plan updated.");
        } else {
            showError("Plan not found.");
        }
    }

    private void onDeletePlan() {
        String name = planNameField.getText().trim();
        if (name.isEmpty()) { showError("Select a plan to delete."); return; }
        if (membershipController.deletePlan(name)) {
            refreshPlansTable();
        } else {
            showError("Plan not found.");
        }
    }

    private void onRenew() {
        String id = renewMemberIdField.getText().trim();
        Member m = memberController.getMemberById(id);
        if (m == null) {
            showError("Member ID not found.");
            return;
        }
        String plan = (String) renewPlanBox.getSelectedItem();
        if (plan == null) { showError("No plan selected."); return; }
        java.time.LocalDate newExpiry = membershipController.renewMembership(m, plan);
        memberController.updateMember(m);
        refreshReminders();
        JOptionPane.showMessageDialog(this, "Membership renewed. New expiry: " + DateUtil.format(newExpiry));
        renewMemberIdField.setText("");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
}
