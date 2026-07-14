package view;

import controller.TrainerController;
import model.Trainer;
import utils.UITheme;
import utils.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * TrainerPanel.java
 * Full Trainer Management UI: add, edit, delete, search.
 */
public class TrainerPanel extends JPanel {

    private final TrainerController trainerController;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    private JTextField idField, nameField, experienceField, specializationField, phoneField, salaryField;

    public TrainerPanel(TrainerController trainerController) {
        this.trainerController = trainerController;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
        refreshTable(trainerController.getAllTrainers());
    }

    private void buildUI() {
        JLabel title = new JLabel("Trainer Management");
        title.setFont(UITheme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildTablePanel(), buildFormPanel());
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> refreshTable(trainerController.search(searchField.getText())));
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> { searchField.setText(""); refreshTable(trainerController.getAllTrainers()); });
        JPanel searchButtons = new JPanel(new GridLayout(1, 2, 5, 0));
        searchButtons.setOpaque(false);
        searchButtons.add(searchBtn);
        searchButtons.add(clearBtn);
        searchPanel.add(new JLabel("Search (Name/ID/Phone): "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        String[] columns = {"ID", "Name", "Experience (yrs)", "Specialization", "Phone", "Salary", "Assigned"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedIntoForm();
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER), new EmptyBorder(15, 15, 15, 15)));
        outer.setBackground(UITheme.CARD_BG);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setOpaque(false);

        idField = new JTextField(); idField.setEditable(false);
        nameField = new JTextField();
        experienceField = new JTextField();
        specializationField = new JTextField();
        phoneField = new JTextField();
        salaryField = new JTextField();

        form.add(new JLabel("Trainer ID:")); form.add(idField);
        form.add(new JLabel("Name:*")); form.add(nameField);
        form.add(new JLabel("Experience (yrs):*")); form.add(experienceField);
        form.add(new JLabel("Specialization:")); form.add(specializationField);
        form.add(new JLabel("Phone (10 digits):*")); form.add(phoneField);
        form.add(new JLabel("Salary:*")); form.add(salaryField);

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
        buttonPanel.add(addBtn); buttonPanel.add(updateBtn); buttonPanel.add(deleteBtn); buttonPanel.add(clearBtn);

        outer.add(new JLabel("Trainer Details"), BorderLayout.NORTH);
        outer.add(form, BorderLayout.CENTER);
        outer.add(buttonPanel, BorderLayout.SOUTH);
        return outer;
    }

    private void refreshTable(List<Trainer> trainers) {
        tableModel.setRowCount(0);
        for (Trainer t : trainers) {
            tableModel.addRow(new Object[]{t.getTrainerId(), t.getName(), t.getExperience(),
                    t.getSpecialization(), t.getPhone(), t.getSalary(), t.getAssignedMemberCount()});
        }
    }

    private void loadSelectedIntoForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        Trainer t = trainerController.getTrainerById(id);
        if (t == null) return;
        idField.setText(t.getTrainerId());
        nameField.setText(t.getName());
        experienceField.setText(String.valueOf(t.getExperience()));
        specializationField.setText(t.getSpecialization());
        phoneField.setText(t.getPhone());
        salaryField.setText(String.valueOf(t.getSalary()));
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        experienceField.setText("");
        specializationField.setText("");
        phoneField.setText("");
        salaryField.setText("");
        table.clearSelection();
    }

    private boolean validateForm() {
        if (!Validator.isNotEmpty(nameField.getText())) { showError("Name is required."); return false; }
        if (!Validator.isValidIntegerString(experienceField.getText())) { showError("Experience must be a number."); return false; }
        if (!Validator.isValidPhone(phoneField.getText())) { showError("Enter a valid 10-digit phone number."); return false; }
        if (!Validator.isValidNumberString(salaryField.getText())) { showError("Salary must be numeric."); return false; }
        return true;
    }

    private void onAdd() {
        if (!validateForm()) return;
        String id = trainerController.getNextTrainerId();
        Trainer t = new Trainer(id, nameField.getText().trim(), Integer.parseInt(experienceField.getText().trim()),
                specializationField.getText().trim(), phoneField.getText().trim(), Double.parseDouble(salaryField.getText().trim()));
        if (trainerController.addTrainer(t)) {
            JOptionPane.showMessageDialog(this, "Trainer added successfully! ID: " + id);
            refreshTable(trainerController.getAllTrainers());
            clearForm();
        } else {
            showError("Trainer ID already exists.");
        }
    }

    private void onUpdate() {
        String id = idField.getText().trim();
        if (id.isEmpty()) { showError("Select a trainer to update."); return; }
        if (!validateForm()) return;
        Trainer t = new Trainer(id, nameField.getText().trim(), Integer.parseInt(experienceField.getText().trim()),
                specializationField.getText().trim(), phoneField.getText().trim(), Double.parseDouble(salaryField.getText().trim()));
        if (trainerController.updateTrainer(t)) {
            JOptionPane.showMessageDialog(this, "Trainer updated.");
            refreshTable(trainerController.getAllTrainers());
            clearForm();
        } else {
            showError("Trainer not found.");
        }
    }

    private void onDelete() {
        String id = idField.getText().trim();
        if (id.isEmpty()) { showError("Select a trainer to delete."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete trainer " + id + "?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            trainerController.deleteTrainer(id);
            refreshTable(trainerController.getAllTrainers());
            clearForm();
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
}
