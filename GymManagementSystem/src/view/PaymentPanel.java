package view;

import controller.MemberController;
import controller.PaymentController;
import model.Member;
import model.Payment;
import utils.DateUtil;
import utils.FileManager;
import utils.UITheme;
import utils.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * PaymentPanel.java
 * Handles fee collection, receipt generation, payment history,
 * and pending payments.
 */
public class PaymentPanel extends JPanel {

    private final PaymentController paymentController;
    private final MemberController memberController;

    private DefaultTableModel tableModel;
    private JTextField memberIdField, amountField, gstField, discountField;
    private JComboBox<String> paymentModeBox;
    private JComboBox<String> statusBox;

    public PaymentPanel(PaymentController paymentController, MemberController memberController) {
        this.paymentController = paymentController;
        this.memberController = memberController;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
        refreshTable(paymentController.getAllPayments());
    }

    private void buildUI() {
        JLabel title = new JLabel("Payment Management");
        title.setFont(UITheme.FONT_TITLE);
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildTablePanel(), buildFormPanel());
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel actions = new JPanel(new GridLayout(1, 2, 6, 0));
        actions.setOpaque(false);
        JButton allBtn = new JButton("All Payments");
        JButton pendingBtn = new JButton("Pending Payments");
        allBtn.addActionListener(e -> refreshTable(paymentController.getAllPayments()));
        pendingBtn.addActionListener(e -> refreshTable(paymentController.getPendingPayments()));
        actions.add(allBtn); actions.add(pendingBtn);

        String[] cols = {"Receipt No", "Member ID", "Name", "Plan", "Total", "Date", "Mode", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);

        panel.add(actions, BorderLayout.NORTH);
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

        memberIdField = new JTextField();
        amountField = new JTextField();
        gstField = new JTextField("0");
        discountField = new JTextField("0");
        paymentModeBox = new JComboBox<>(new String[]{"Cash", "Card", "UPI", "Online"});
        statusBox = new JComboBox<>(new String[]{"Paid", "Pending"});

        form.add(new JLabel("Member ID:*")); form.add(memberIdField);
        form.add(new JLabel("Amount (Rs.):*")); form.add(amountField);
        form.add(new JLabel("GST (Rs.):")); form.add(gstField);
        form.add(new JLabel("Discount (Rs.):")); form.add(discountField);
        form.add(new JLabel("Payment Mode:")); form.add(paymentModeBox);
        form.add(new JLabel("Status:")); form.add(statusBox);

        JButton collectBtn = new JButton("Collect Payment & Generate Receipt");
        collectBtn.addActionListener(e -> onCollectPayment());

        outer.add(new JLabel("Fee Collection"), BorderLayout.NORTH);
        outer.add(form, BorderLayout.CENTER);
        outer.add(collectBtn, BorderLayout.SOUTH);
        return outer;
    }

    private void onCollectPayment() {
        String id = memberIdField.getText().trim();
        Member m = memberController.getMemberById(id);
        if (m == null) { showError("Member ID not found."); return; }
        if (!Validator.isValidNumberString(amountField.getText())) { showError("Enter a valid amount."); return; }
        double amount = Double.parseDouble(amountField.getText().trim());
        double gst = Validator.isValidNumberString(gstField.getText()) ? Double.parseDouble(gstField.getText().trim()) : 0;
        double discount = Validator.isValidNumberString(discountField.getText()) ? Double.parseDouble(discountField.getText().trim()) : 0;
        String mode = (String) paymentModeBox.getSelectedItem();
        String status = (String) statusBox.getSelectedItem();

        Payment payment = paymentController.recordPayment(id, m.getFullName(), m.getMembershipPlan(),
                amount, gst, discount, LocalDate.now(), mode, status);

        String receiptText = paymentController.generateReceiptText(payment);
        String fileName = "receipt_" + payment.getReceiptNumber() + ".txt";
        FileManager.writeTextReport(fileName, receiptText);

        refreshTable(paymentController.getAllPayments());
        JTextArea area = new JTextArea(receiptText);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Receipt Generated - " + fileName,
                JOptionPane.INFORMATION_MESSAGE);

        memberIdField.setText("");
        amountField.setText("");
        gstField.setText("0");
        discountField.setText("0");
    }

    private void refreshTable(List<Payment> payments) {
        tableModel.setRowCount(0);
        for (Payment p : payments) {
            tableModel.addRow(new Object[]{p.getReceiptNumber(), p.getMemberId(), p.getMemberName(),
                    p.getMembershipPlan(), p.getTotalAmount(), DateUtil.format(p.getDate()), p.getPaymentMode(), p.getStatus()});
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
}
