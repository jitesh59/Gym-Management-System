package view;

import controller.EquipmentController;
import model.Equipment;
import utils.DateUtil;
import utils.UITheme;
import utils.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * EquipmentPanel.java
 * Full Equipment Management UI: add, edit, delete, search, low-stock alert.
 */
public class EquipmentPanel extends JPanel {

    private final EquipmentController equipmentController;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    private JTextField idField, nameField, quantityField, purchaseDateField, maintenanceDateField;
    private JComboBox<String> statusBox;

    public EquipmentPanel(EquipmentController equipmentController) {
        this.equipmentController = equipmentController;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
        refreshTable(equipmentController.getAllEquipment());
    }

    private void buildUI() {
        JLabel title = new JLabel("Equipment Management");
        title.setFont(UITheme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildTablePanel(), buildFormPanel());
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> refreshTable(equipmentController.search(searchField.getText())));
        JButton lowStockBtn = new JButton("Low Stock Alert");
        lowStockBtn.addActionListener(e -> refreshTable(equipmentController.getLowStockEquipment()));
        JButton clearBtn = new JButton("Show All");
        clearBtn.addActionListener(e -> { searchField.setText(""); refreshTable(equipmentController.getAllEquipment()); });
        JPanel btnRow = new JPanel(new GridLayout(1, 3, 5, 0));
        btnRow.setOpaque(false);
        btnRow.add(searchBtn); btnRow.add(lowStockBtn); btnRow.add(clearBtn);
        searchPanel.add(new JLabel("Search (Name/ID): "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(btnRow, BorderLayout.EAST);

        String[] columns = {"ID", "Name", "Quantity", "Purchase Date", "Maintenance Date", "Status", "Low Stock"};
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
        quantityField = new JTextField();
        purchaseDateField = new JTextField(DateUtil.format(LocalDate.now()));
        maintenanceDateField = new JTextField(DateUtil.format(LocalDate.now().plusMonths(6)));
        statusBox = new JComboBox<>(new String[]{"Working", "Under Maintenance", "Damaged"});

        form.add(new JLabel("Equipment ID:")); form.add(idField);
        form.add(new JLabel("Name:*")); form.add(nameField);
        form.add(new JLabel("Quantity:*")); form.add(quantityField);
        form.add(new JLabel("Purchase Date (dd-MM-yyyy):")); form.add(purchaseDateField);
        form.add(new JLabel("Maintenance Date (dd-MM-yyyy):")); form.add(maintenanceDateField);
        form.add(new JLabel("Status:")); form.add(statusBox);

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

        outer.add(new JLabel("Equipment Details"), BorderLayout.NORTH);
        outer.add(form, BorderLayout.CENTER);
        outer.add(buttonPanel, BorderLayout.SOUTH);
        return outer;
    }

    private void refreshTable(List<Equipment> list) {
        tableModel.setRowCount(0);
        for (Equipment e : list) {
            tableModel.addRow(new Object[]{e.getEquipmentId(), e.getEquipmentName(), e.getQuantity(),
                    DateUtil.format(e.getPurchaseDate()), DateUtil.format(e.getMaintenanceDate()), e.getStatus(),
                    e.isLowStock() ? "YES" : "No"});
        }
    }

    private void loadSelectedIntoForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        Equipment e = equipmentController.getEquipmentById(id);
        if (e == null) return;
        idField.setText(e.getEquipmentId());
        nameField.setText(e.getEquipmentName());
        quantityField.setText(String.valueOf(e.getQuantity()));
        purchaseDateField.setText(DateUtil.format(e.getPurchaseDate()));
        maintenanceDateField.setText(DateUtil.format(e.getMaintenanceDate()));
        statusBox.setSelectedItem(e.getStatus());
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        quantityField.setText("");
        purchaseDateField.setText(DateUtil.format(LocalDate.now()));
        maintenanceDateField.setText(DateUtil.format(LocalDate.now().plusMonths(6)));
        statusBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateForm() {
        if (!Validator.isNotEmpty(nameField.getText())) { showError("Equipment name is required."); return false; }
        if (!Validator.isValidIntegerString(quantityField.getText())) { showError("Quantity must be a valid number."); return false; }
        try {
            DateUtil.parse(purchaseDateField.getText());
            DateUtil.parse(maintenanceDateField.getText());
        } catch (DateTimeParseException ex) {
            showError("Dates must be in dd-MM-yyyy format.");
            return false;
        }
        return true;
    }

    private void onAdd() {
        if (!validateForm()) return;
        String id = equipmentController.getNextEquipmentId();
        Equipment e = new Equipment(id, nameField.getText().trim(), Integer.parseInt(quantityField.getText().trim()),
                DateUtil.parse(purchaseDateField.getText()), DateUtil.parse(maintenanceDateField.getText()),
                (String) statusBox.getSelectedItem());
        if (equipmentController.addEquipment(e)) {
            JOptionPane.showMessageDialog(this, "Equipment added! ID: " + id);
            refreshTable(equipmentController.getAllEquipment());
            clearForm();
        } else {
            showError("Equipment ID already exists.");
        }
    }

    private void onUpdate() {
        String id = idField.getText().trim();
        if (id.isEmpty()) { showError("Select equipment to update."); return; }
        if (!validateForm()) return;
        Equipment e = new Equipment(id, nameField.getText().trim(), Integer.parseInt(quantityField.getText().trim()),
                DateUtil.parse(purchaseDateField.getText()), DateUtil.parse(maintenanceDateField.getText()),
                (String) statusBox.getSelectedItem());
        if (equipmentController.updateEquipment(e)) {
            JOptionPane.showMessageDialog(this, "Equipment updated.");
            refreshTable(equipmentController.getAllEquipment());
            clearForm();
        } else {
            showError("Equipment not found.");
        }
    }

    private void onDelete() {
        String id = idField.getText().trim();
        if (id.isEmpty()) { showError("Select equipment to delete."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete equipment " + id + "?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            equipmentController.deleteEquipment(id);
            refreshTable(equipmentController.getAllEquipment());
            clearForm();
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
}
