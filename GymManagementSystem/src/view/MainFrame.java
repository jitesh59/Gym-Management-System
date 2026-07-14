package view;

import controller.*;
import utils.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * MainFrame.java
 * The main application shell after login: left navigation sidebar
 * + CardLayout content area holding all module panels.
 */
public class MainFrame extends JFrame {

    private final AuthController authController;
    private final MemberController memberController;
    private final TrainerController trainerController;
    private final EquipmentController equipmentController;
    private final AttendanceController attendanceController;
    private final PaymentController paymentController;
    private final MembershipController membershipController;
    private final ReportController reportController;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private DashboardPanel dashboardPanel;

    public MainFrame(AuthController authController) {
        this.authController = authController;
        this.memberController = new MemberController();
        this.trainerController = new TrainerController();
        this.equipmentController = new EquipmentController();
        this.attendanceController = new AttendanceController();
        this.paymentController = new PaymentController();
        this.membershipController = new MembershipController();
        this.reportController = new ReportController(memberController, trainerController,
                equipmentController, attendanceController, paymentController);

        setTitle("Gym Management System - Dashboard");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel sidebar = buildSidebar();
        root.add(sidebar, BorderLayout.WEST);

        contentPanel.setBackground(UITheme.BACKGROUND);

        dashboardPanel = new DashboardPanel(memberController, trainerController, equipmentController,
                attendanceController, paymentController);

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(new MemberPanel(memberController, membershipController), "members");
        contentPanel.add(new MembershipPanel(membershipController, memberController), "memberships");
        contentPanel.add(new TrainerPanel(trainerController), "trainers");
        contentPanel.add(new AttendancePanel(attendanceController, memberController), "attendance");
        contentPanel.add(new PaymentPanel(paymentController, memberController), "payments");
        contentPanel.add(new EquipmentPanel(equipmentController), "equipment");
        contentPanel.add(new ReportPanel(reportController), "reports");

        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(UITheme.PRIMARY_DARK);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel logo = new JLabel("  \uD83C\uDFCB GYM MGMT");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(new EmptyBorder(0, 10, 25, 0));
        sidebar.add(logo);

        sidebar.add(navButton("Dashboard", "dashboard"));
        sidebar.add(navButton("Members", "members"));
        sidebar.add(navButton("Memberships", "memberships"));
        sidebar.add(navButton("Trainers", "trainers"));
        sidebar.add(navButton("Attendance", "attendance"));
        sidebar.add(navButton("Payments", "payments"));
        sidebar.add(navButton("Equipment", "equipment"));
        sidebar.add(navButton("Reports", "reports"));

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("Logout");
        styleNavButton(logoutBtn);
        logoutBtn.setForeground(UITheme.ACCENT_RED);
        logoutBtn.addActionListener(this::onLogout);
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JButton navButton(String text, String cardName) {
        JButton button = new JButton("  " + text);
        styleNavButton(button);
        button.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, cardName);
            if (cardName.equals("dashboard")) {
                dashboardPanel.refreshStats();
            }
        });
        return button;
    }

    private void styleNavButton(JButton button) {
        button.setFont(UITheme.FONT_NAV);
        button.setForeground(Color.WHITE);
        button.setBackground(UITheme.PRIMARY_DARK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void onLogout(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            authController.logout();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginView(authController).setVisible(true));
        }
    }
}
