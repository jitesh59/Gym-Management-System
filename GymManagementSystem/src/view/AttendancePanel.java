package view;

import controller.AttendanceController;
import controller.MemberController;
import model.Attendance;
import model.Member;
import utils.DateUtil;
import utils.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * AttendancePanel.java
 * Handles daily attendance marking (Present/Absent), attendance history,
 * search, and monthly attendance reports.
 */
public class AttendancePanel extends JPanel {

    private final AttendanceController attendanceController;
    private final MemberController memberController;

    private DefaultTableModel tableModel;
    private JTextField memberIdField;
    private JTextField dateField;
    private JTextField searchField;
    private JTextField monthField, yearField;

    public AttendancePanel(AttendanceController attendanceController, MemberController memberController) {
        this.attendanceController = attendanceController;
        this.memberController = memberController;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
        refreshTable(attendanceController.getAttendanceByDate(LocalDate.now()));
    }

    private void buildUI() {
        JLabel title = new JLabel("Attendance Management");
        title.setFont(UITheme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        JPanel top = new JPanel(new GridLayout(1, 2, 15, 0));
        top.setOpaque(false);
        top.add(buildMarkAttendancePanel());
        top.add(buildMonthlyReportPanel());

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(top, BorderLayout.NORTH);
        center.add(buildTablePanel(), BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    private JPanel buildMarkAttendancePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Mark Attendance"));
        memberIdField = new JTextField();
        dateField = new JTextField(DateUtil.format(LocalDate.now()));

        JButton presentBtn = new JButton("Mark Present");
        JButton absentBtn = new JButton("Mark Absent");
        presentBtn.addActionListener(e -> mark("Present"));
        absentBtn.addActionListener(e -> mark("Absent"));

        panel.add(new JLabel("Member ID:")); panel.add(memberIdField);
        panel.add(new JLabel("Date (dd-MM-yyyy):")); panel.add(dateField);
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 6, 0));
        btnRow.add(presentBtn); btnRow.add(absentBtn);
        panel.add(new JLabel("")); panel.add(btnRow);
        return panel;
    }

    private JPanel buildMonthlyReportPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Search / Monthly Report"));
        searchField = new JTextField();
        monthField = new JTextField(String.valueOf(LocalDate.now().getMonthValue()));
        yearField = new JTextField(String.valueOf(LocalDate.now().getYear()));

        JButton searchBtn = new JButton("Search by Name/ID");
        searchBtn.addActionListener(e -> refreshTable(attendanceController.search(searchField.getText())));

        JButton monthlyBtn = new JButton("View Monthly Report");
        monthlyBtn.addActionListener(e -> {
            try {
                int month = Integer.parseInt(monthField.getText().trim());
                int year = Integer.parseInt(yearField.getText().trim());
                refreshTable(attendanceController.getMonthlyAttendance(year, month));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid month/year numbers.");
            }
        });

        panel.add(new JLabel("Search:")); panel.add(searchField);
        panel.add(new JLabel("Month/Year:"));
        JPanel monthYearRow = new JPanel(new GridLayout(1, 2, 4, 0));
        monthYearRow.add(monthField); monthYearRow.add(yearField);
        panel.add(monthYearRow);
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 6, 0));
        btnRow.add(searchBtn); btnRow.add(monthlyBtn);
        panel.add(new JLabel("")); panel.add(btnRow);
        return panel;
    }

    private JScrollPane buildTablePanel() {
        String[] cols = {"Attendance ID", "Member ID", "Member Name", "Date", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        return new JScrollPane(table);
    }

    private void mark(String status) {
        String id = memberIdField.getText().trim();
        Member m = memberController.getMemberById(id);
        if (m == null) {
            JOptionPane.showMessageDialog(this, "Member ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate date;
        try {
            date = DateUtil.parse(dateField.getText());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Enter date as dd-MM-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        attendanceController.markAttendance(id, m.getFullName(), date, status);
        refreshTable(attendanceController.getAttendanceByDate(date));
        JOptionPane.showMessageDialog(this, m.getFullName() + " marked " + status + " for " + DateUtil.format(date));
    }

    private void refreshTable(List<Attendance> records) {
        tableModel.setRowCount(0);
        for (Attendance a : records) {
            tableModel.addRow(new Object[]{a.getAttendanceId(), a.getMemberId(), a.getMemberName(),
                    DateUtil.format(a.getDate()), a.getStatus()});
        }
    }
}
