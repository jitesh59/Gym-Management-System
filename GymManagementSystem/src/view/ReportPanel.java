package view;

import controller.ReportController;
import utils.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * ReportPanel.java
 * Report Module: generate and export various reports as text files.
 */
public class ReportPanel extends JPanel {

    private final ReportController reportController;
    private JTextArea reportArea;
    private JTextField monthField, yearField;
    private String currentFileName = "report.txt";

    public ReportPanel(ReportController reportController) {
        this.reportController = reportController;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JLabel title = new JLabel("Reports");
        title.setFont(UITheme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createTitledBorder("Generate Report"));
        left.setPreferredSize(new Dimension(240, 0));

        JButton activeBtn = new JButton("Active Members Report");
        activeBtn.addActionListener(e -> show(reportController.generateActiveMembersReport(), "active_members_report.txt"));

        JButton expiredBtn = new JButton("Expired Members Report");
        expiredBtn.addActionListener(e -> show(reportController.generateExpiredMembersReport(), "expired_members_report.txt"));

        JButton equipmentBtn = new JButton("Equipment Report");
        equipmentBtn.addActionListener(e -> show(reportController.generateEquipmentReport(), "equipment_report.txt"));

        JButton trainerBtn = new JButton("Trainer Report");
        trainerBtn.addActionListener(e -> show(reportController.generateTrainerReport(), "trainer_report.txt"));

        JPanel monthYearPanel = new JPanel(new GridLayout(1, 2, 4, 0));
        monthField = new JTextField(String.valueOf(LocalDate.now().getMonthValue()));
        yearField = new JTextField(String.valueOf(LocalDate.now().getYear()));
        monthYearPanel.add(monthField);
        monthYearPanel.add(yearField);
        monthYearPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton revenueBtn = new JButton("Monthly Revenue Report");
        revenueBtn.addActionListener(e -> {
            int[] my = readMonthYear();
            if (my != null) show(reportController.generateMonthlyRevenueReport(my[1], my[0]), "monthly_revenue_report.txt");
        });

        JButton attendanceBtn = new JButton("Monthly Attendance Report");
        attendanceBtn.addActionListener(e -> {
            int[] my = readMonthYear();
            if (my != null) show(reportController.generateAttendanceReport(my[1], my[0]), "monthly_attendance_report.txt");
        });

        for (JButton b : new JButton[]{activeBtn, expiredBtn, equipmentBtn, trainerBtn}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            left.add(b);
            left.add(Box.createVerticalStrut(8));
        }
        JLabel myLabel = new JLabel("Month / Year:");
        myLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(myLabel);
        left.add(Box.createVerticalStrut(4));
        monthYearPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(monthYearPanel);
        left.add(Box.createVerticalStrut(8));
        for (JButton b : new JButton[]{revenueBtn, attendanceBtn}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            left.add(b);
            left.add(Box.createVerticalStrut(8));
        }

        left.add(Box.createVerticalGlue());
        JButton exportBtn = new JButton("Export Current Report to File");
        exportBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        exportBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        exportBtn.addActionListener(e -> exportCurrent());
        left.add(exportBtn);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setText("Select a report to generate...");

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        add(left, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
    }

    private int[] readMonthYear() {
        try {
            int month = Integer.parseInt(monthField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());
            return new int[]{month, year};
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid month and year numbers.");
            return null;
        }
    }

    private void show(String content, String fileName) {
        reportArea.setText(content);
        currentFileName = fileName;
    }

    private void exportCurrent() {
        reportController.exportReport(currentFileName, reportArea.getText());
        JOptionPane.showMessageDialog(this, "Report exported to data/" + currentFileName);
    }
}
