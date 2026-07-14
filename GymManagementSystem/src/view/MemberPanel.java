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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * MemberPanel.java
 * Full Member Management UI: add, edit, delete, search, view details.
 */
public class MemberPanel extends JPanel {

    private final MemberController memberController;
    private final MembershipController membershipController;

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    // Form fields
    private JTextField idField, nameField, ageField, phoneField, emailField, addressField,
            heightField, weightField, emergencyField, joinDateField, expiryDateField;
    private JComboBox<String> genderBox, planBox;

    public MemberPanel(MemberController memberController, MembershipController membershipController) {
        this.memberController = memberController;
        this.membershipController = membershipController;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
        refreshTable(memberController.getAllMembers());
    }

    private void buildUI() {
        JLabel title = new JLabel("Member Management");
        title.setFont(UITheme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildTablePanel(), buildFormPanel());
        splitPane.setResizeWeight(0.62);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> refreshTable(memberController.search(searchField.getText())));
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> { searchField.setText(""); refreshTable(memberController.getAllMembers()); });
        JPanel searchButtons = new JPanel(new GridLayout(1, 2, 5, 0));
        searchButtons.setOpaque(false);
        searchButtons.add(searchBtn);
        searchButtons.add(clearBtn);
        searchPanel.add(new JLabel("Search (Name/ID/Phone): "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        String[] columns = {"ID", "Name", "Gender", "Age", "Phone", "Plan", "Expiry", "Status", "BMI"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedIntoForm();
        });
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER), new EmptyBorder(15, 15, 15, 15)));
        outer.setBackground(UITheme.CARD_BG);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setOpaque(false);

        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        ageField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();
        heightField = new JTextField();
        weightField = new JTextField();
        planBox = new JComboBox<>();
        for (MembershipPlan p : membershipController.getAllPlans()) planBox.addItem(p.getPlanName());
        joinDateField = new JTextField(DateUtil.format(LocalDate.now()));
        expiryDateField = new JTextField();
        expiryDateField.setEditable(false);
        emergencyField = new JTextField();

        form.add(new JLabel("Member ID:")); form.add(idField);
        form.add(new JLabel("Full Name:*")); form.add(nameField);
        form.add(new JLabel("Gender:")); form.add(genderBox);
        form.add(new JLabel("Age:*")); form.add(ageField);
        form.add(new JLabel("Phone (10 digits):*")); form.add(phoneField);
        form.add(new JLabel("Email:*")); form.add(emailField);
        form.add(new JLabel("Address:")); form.add(addressField);
        form.add(new JLabel("Height (cm):")); form.add(heightField);
        form.add(new JLabel("Weight (kg):")); form.add(weightField);
        form.add(new JLabel("Membership Plan:")); form.add(planBox);
        form.add(new JLabel("Join Date (dd-MM-yyyy):")); form.add(joinDateField);
        form.add(new JLabel("Expiry Date (auto):")); form.add(expiryDateField);
        form.add(new JLabel("Emergency Contact:")); form.add(emergencyField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 6, 0));
        buttonPanel.setOpaque(false);
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        addBtn.addActionListener(e -> onAdd());
        updateBtn.addActionListener(e -> onUpdate());
        deleteBtn.addActionListener(e -> onDelete());
        clearBtn.addActionListener(e -> clearForm());
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        outer.add(new JLabel("Member Details"), BorderLayout.NORTH);
        outer.add(form, BorderLayout.CENTER);
        outer.add(buttonPanel, BorderLayout.SOUTH);
        return outer;
    }

    private void refreshTable(java.util.List<Member> members) {
        tableModel.setRowCount(0);
        for (Member m : members) {
            tableModel.addRow(new Object[]{
                    m.getMemberId(), m.getFullName(), m.getGender(), m.getAge(), m.getPhoneNumber(),
                    m.getMembershipPlan(), DateUtil.format(m.getExpiryDate()), m.getStatus(), m.getBMI()
            });
        }
    }

    private void loadSelectedIntoForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        Member m = memberController.getMemberById(id);
        if (m == null) return;
        idField.setText(m.getMemberId());
        nameField.setText(m.getFullName());
        genderBox.setSelectedItem(m.getGender());
        ageField.setText(String.valueOf(m.getAge()));
        phoneField.setText(m.getPhoneNumber());
        emailField.setText(m.getEmail());
        addressField.setText(m.getAddress());
        heightField.setText(String.valueOf(m.getHeight()));
        weightField.setText(String.valueOf(m.getWeight()));
        planBox.setSelectedItem(m.getMembershipPlan());
        joinDateField.setText(DateUtil.format(m.getJoinDate()));
        expiryDateField.setText(DateUtil.format(m.getExpiryDate()));
        emergencyField.setText(m.getEmergencyContact());
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        genderBox.setSelectedIndex(0);
        ageField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        heightField.setText("");
        weightField.setText("");
        planBox.setSelectedIndex(0);
        joinDateField.setText(DateUtil.format(LocalDate.now()));
        expiryDateField.setText("");
        emergencyField.setText("");
        table.clearSelection();
    }

    private boolean validateForm() {
        if (!Validator.isNotEmpty(nameField.getText())) {
            showError("Full name is required.");
            return false;
        }
        if (!Validator.isValidIntegerString(ageField.getText()) || !Validator.isValidAge(Integer.parseInt(ageField.getText().trim()))) {
            showError("Please enter a valid age (5-100).");
            return false;
        }
        if (!Validator.isValidPhone(phoneField.getText())) {
            showError("Please enter a valid 10-digit phone number.");
            return false;
        }
        if (!Validator.isValidEmail(emailField.getText())) {
            showError("Please enter a valid email address.");
            return false;
        }
        if (Validator.isNotEmpty(heightField.getText()) && !Validator.isValidNumberString(heightField.getText())) {
            showError("Height must be numeric.");
            return false;
        }
        if (Validator.isNotEmpty(weightField.getText()) && !Validator.isValidNumberString(weightField.getText())) {
            showError("Weight must be numeric.");
            return false;
        }
        try {
            DateUtil.parse(joinDateField.getText());
        } catch (DateTimeParseException ex) {
            showError("Join date must be in dd-MM-yyyy format.");
            return false;
        }
        return true;
    }

    private void onAdd() {
        if (!validateForm()) return;
        String id = memberController.getNextMemberId();
        Member m = buildMemberFromForm(id);
        if (memberController.addMember(m)) {
            JOptionPane.showMessageDialog(this, "Member added successfully! ID: " + id);
            refreshTable(memberController.getAllMembers());
            clearForm();
        } else {
            showError("A member with this ID already exists.");
        }
    }

    private void onUpdate() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            showError("Select a member from the table to update.");
            return;
        }
        if (!validateForm()) return;
        Member m = buildMemberFromForm(id);
        if (memberController.updateMember(m)) {
            JOptionPane.showMessageDialog(this, "Member updated successfully.");
            refreshTable(memberController.getAllMembers());
            clearForm();
        } else {
            showError("Member not found.");
        }
    }

    private void onDelete() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            showError("Select a member from the table to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete member " + id + "?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            memberController.deleteMember(id);
            refreshTable(memberController.getAllMembers());
            clearForm();
        }
    }

    private Member buildMemberFromForm(String id) {
        LocalDate joinDate = DateUtil.parse(joinDateField.getText());
        String plan = (String) planBox.getSelectedItem();
        LocalDate expiry = membershipController.calculateExpiryDate(joinDate, plan);
        double height = Validator.isNotEmpty(heightField.getText()) ? Double.parseDouble(heightField.getText().trim()) : 0;
        double weight = Validator.isNotEmpty(weightField.getText()) ? Double.parseDouble(weightField.getText().trim()) : 0;
        Member m = new Member(id, nameField.getText().trim(), (String) genderBox.getSelectedItem(),
                Integer.parseInt(ageField.getText().trim()), phoneField.getText().trim(), emailField.getText().trim(),
                addressField.getText().trim(), height, weight, plan, joinDate, expiry, emergencyField.getText().trim());
        return m;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
}
