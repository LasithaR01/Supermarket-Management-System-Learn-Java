package cmart.ui.shift;

import cmart.service.ShiftService;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ShiftPanel extends JPanel {

    private final ShiftService shiftService = new ShiftService();
    private final JLabel statusLabel = new JLabel(" ");
    private final JTextField openFloatField = new JTextField("0", 10);
    private final JTextField countedCashField = new JTextField("0", 10);

    public ShiftPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel title = new JLabel("Cashier shift");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UiTheme.SIDEBAR);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setOpaque(true);
        form.setBackground(java.awt.Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        form.add(statusLabel);
        form.add(new JLabel("Opening float"));
        form.add(openFloatField);
        JButton open = new JButton("Open shift");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openShift(); }
        });
        form.add(open);
        form.add(new JLabel("Counted cash at close"));
        form.add(countedCashField);
        JButton close = new JButton("Close shift");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { closeShift(); }
        });
        form.add(close);
        JButton refresh = new JButton("Refresh status");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refreshStatus(); }
        });
        form.add(refresh);
        add(form, BorderLayout.CENTER);
        refreshStatus();
    }

    private void refreshStatus() {
        try {
            String[] open = shiftService.currentOpenShift();
            if (open == null) {
                statusLabel.setText("No open shift.");
            } else {
                statusLabel.setText("Open shift #" + open[0] + " since " + open[2] + " · float " + open[3]);
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    private void openShift() {
        try {
            shiftService.open(Double.parseDouble(openFloatField.getText().trim()));
            JOptionPane.showMessageDialog(this, "Shift opened.");
            refreshStatus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void closeShift() {
        try {
            double[] r = shiftService.close(Double.parseDouble(countedCashField.getText().trim()));
            JOptionPane.showMessageDialog(this,
                    "Shift closed.\nOpen float: " + r[0]
                            + "\nSales approx: " + r[1]
                            + "\nExpected: " + r[2]
                            + "\nCounted: " + r[3]
                            + "\nDifference: " + r[4]);
            refreshStatus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
