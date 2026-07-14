package view;

import controller.AttendanceController;
import controller.EquipmentController;
import controller.MemberController;
import controller.PaymentController;
import controller.TrainerController;
import utils.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * DashboardPanel.java
 * Displays key gym statistics as summary cards, plus quick action buttons.
 */
public class DashboardPanel extends JPanel {

    private final MemberController memberController;
    private final TrainerController trainerController;
    private final EquipmentController equipmentController;
    private final AttendanceController attendanceController;
    private final PaymentController paymentController;

    private JLabel totalMembersValue, activeMembersValue, expiredValue, todayAttendanceValue,
            revenueValue, trainersValue, equipmentValue;

    public DashboardPanel(MemberController memberController, TrainerController trainerController,
                            EquipmentController equipmentController, AttendanceController attendanceController,
                            PaymentController paymentController) {
        this.memberController = memberController;
        this.trainerController = trainerController;
        this.equipmentController = equipmentController;
        this.attendanceController = attendanceController;
        this.paymentController = paymentController;

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(25, 25, 25, 25));
        buildUI();
        refreshStats();
    }

    private void buildUI() {
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 4, 18, 18));
        cardsPanel.setOpaque(false);

        totalMembersValue = new JLabel("0");
        activeMembersValue = new JLabel("0");
        expiredValue = new JLabel("0");
        todayAttendanceValue = new JLabel("0");
        revenueValue = new JLabel("Rs.0");
        trainersValue = new JLabel("0");
        equipmentValue = new JLabel("0");

        cardsPanel.add(buildCard("Total Members", totalMembersValue, UITheme.PRIMARY));
        cardsPanel.add(buildCard("Active Members", activeMembersValue, UITheme.ACCENT_GREEN));
        cardsPanel.add(buildCard("Expired Memberships", expiredValue, UITheme.ACCENT_RED));
        cardsPanel.add(buildCard("Today's Attendance", todayAttendanceValue, UITheme.ACCENT_ORANGE));
        cardsPanel.add(buildCard("Monthly Revenue", revenueValue, UITheme.PRIMARY_LIGHT));
        cardsPanel.add(buildCard("Trainers", trainersValue, UITheme.PRIMARY));
        cardsPanel.add(buildCard("Equipment", equipmentValue, UITheme.ACCENT_GREEN));
        cardsPanel.add(buildQuickActionsCard());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardsPanel, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel buildCard(String label, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(18, 18, 18, 18)));

        JPanel stripe = new JPanel();
        stripe.setBackground(accent);
        stripe.setPreferredSize(new Dimension(40, 5));
        stripe.setMaximumSize(new Dimension(40, 5));
        stripe.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel captionLabel = new JLabel(label);
        captionLabel.setFont(UITheme.FONT_LABEL);
        captionLabel.setForeground(UITheme.TEXT_MUTED);
        captionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(UITheme.FONT_CARD_VALUE);
        valueLabel.setForeground(UITheme.TEXT_DARK);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(stripe);
        card.add(Box.createVerticalStrut(10));
        card.add(captionLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);
        return card;
    }

    private JPanel buildQuickActionsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel label = new JLabel("Quick Actions");
        label.setFont(UITheme.FONT_LABEL_BOLD);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(label);
        card.add(Box.createVerticalStrut(8));

        JButton refreshBtn = new JButton("Refresh Stats");
        refreshBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        refreshBtn.addActionListener(e -> refreshStats());
        card.add(refreshBtn);

        return card;
    }

    public void refreshStats() {
        totalMembersValue.setText(String.valueOf(memberController.getTotalMemberCount()));
        activeMembersValue.setText(String.valueOf(memberController.getActiveMemberCount()));
        expiredValue.setText(String.valueOf(memberController.getExpiredMemberCount()));
        todayAttendanceValue.setText(String.valueOf(attendanceController.getTodayAttendanceCount()));
        LocalDate now = LocalDate.now();
        revenueValue.setText("Rs." + paymentController.getMonthlyRevenue(now.getYear(), now.getMonthValue()));
        trainersValue.setText(String.valueOf(trainerController.getTrainerCount()));
        equipmentValue.setText(String.valueOf(equipmentController.getEquipmentCount()));
    }
}
