package pos.pro;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.DefaultListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Shared colors and fonts so every screen looks like one app.
 */
public final class UiTheme {

    public static final Color SIDEBAR = new Color(0x143D2C);
    public static final Color NAV_ACTIVE = new Color(0x2D6A4F);
    public static final Color PAGE = new Color(0xF3F7F4);
    public static final Color CARD = Color.WHITE;
    public static final Color LINE = new Color(0xD5DDD8);
    public static final Color TEXT = new Color(0x1F2933);
    public static final Color MUTED = new Color(0x5C6B73);
    public static final Color PRIMARY = new Color(0x1B7F4E);
    public static final Color INFO = new Color(0x1D6A9C);
    public static final Color DANGER = new Color(0xB23A3A);
    public static final Color NEUTRAL = new Color(0x3D5A4C);
    public static final Color HEADER = new Color(0x1B4332);
    public static final Color STRIPE = new Color(0xF4FAF6);
    public static final Color SELECT = new Color(0xD8F3DC);

    public static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    /** Panels marked with this keep the layout and colors set for the running app. */
    public static final String KEEP = "run.keep";

    private UiTheme() {
    }

    public static void install() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // Keep the default look if the cross-platform one is missing.
        }
        UIManager.put("Panel.background", PAGE);
        UIManager.put("OptionPane.messageFont", FONT);
        UIManager.put("OptionPane.buttonFont", FONT);
        UIManager.put("TabbedPane.font", FONT_BOLD);
    }

    public static void apply(Component root) {
        style(root);
    }

    private static void style(Component component) {
        if (component instanceof JPanel) {
            stylePanel((JPanel) component);
        } else if (component instanceof JButton) {
            styleButton((JButton) component);
        } else if (component instanceof JTextField) {
            styleField((JTextField) component);
        } else if (component instanceof JComboBox) {
            styleCombo((JComboBox) component);
            return;
        } else if (component instanceof JTable) {
            styleTable((JTable) component);
        } else if (component instanceof JLabel) {
            styleLabel((JLabel) component);
        } else if (component instanceof JTextArea) {
            JTextArea area = (JTextArea) component;
            area.setFont(FONT);
            area.setForeground(TEXT);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
        } else if (component instanceof JCheckBox) {
            JCheckBox box = (JCheckBox) component;
            box.setFont(FONT);
            box.setForeground(TEXT);
            box.setOpaque(false);
        } else if (component instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) component;
            tabs.setFont(FONT_BOLD);
            tabs.setBackground(PAGE);
            tabs.setForeground(TEXT);
        } else if (component instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) component;
            scroll.setBorder(new LineBorder(LINE, 1, true));
            scroll.getViewport().setBackground(CARD);
            if (scroll.getViewport().getView() != null) {
                style(scroll.getViewport().getView());
            }
            return;
        }

        if (component instanceof Container) {
            Component[] children = ((Container) component).getComponents();
            for (int i = 0; i < children.length; i++) {
                style(children[i]);
            }
        }
    }

    private static void stylePanel(JPanel panel) {
        if (Boolean.TRUE.equals(panel.getClientProperty(KEEP))) {
            return;
        }
        boolean card = panel.getBorder() != null;
        panel.setOpaque(true);
        panel.setBackground(card ? CARD : PAGE);
        if (card) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(LINE, 1, true),
                    new EmptyBorder(10, 12, 10, 12)));
        }
    }

    private static void styleButton(JButton button) {
        String text = button.getText() == null ? "" : button.getText().toLowerCase();
        Color background = NEUTRAL;
        String tip = null;
        if (text.contains("delete") || text.contains("remove")) {
            background = DANGER;
            tip = "Remove the selected record";
        } else if (text.contains("save") || text.contains("pay") || text.contains("add to cart")) {
            background = PRIMARY;
            tip = "Save and continue";
        } else if (text.contains("update") || text.contains("edit")) {
            background = INFO;
            tip = "Update the selected record";
        } else if (text.contains("search") || text.contains("view") || text.contains("refresh") || text.contains("report")) {
            background = NEUTRAL;
            tip = "Show matching records";
        }
        button.setUI(new BasicButtonUI());
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BOLD);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 14, 8, 14));
        if (button.getToolTipText() == null && tip != null) {
            button.setToolTipText(tip);
        }
    }

    private static void styleField(JTextField field) {
        field.setFont(FONT);
        field.setForeground(TEXT);
        field.setBackground(Color.WHITE);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0xC5D0C8), 1, true),
                new EmptyBorder(6, 8, 6, 8)));
    }

    private static void styleCombo(JComboBox combo) {
        combo.setFont(FONT);
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT);
        combo.setOpaque(true);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(FONT);
                label.setBorder(new EmptyBorder(4, 8, 4, 8));
                label.setOpaque(true);
                if (isSelected) {
                    label.setBackground(SELECT);
                    label.setForeground(TEXT);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(TEXT);
                }
                return label;
            }
        });
    }

    private static void styleLabel(JLabel label) {
        String text = label.getText() == null ? "" : label.getText().trim();
        boolean value = text.length() > 0 && text.indexOf(':') < 0 && text.matches(".*\\d.*") && text.length() < 16;
        if (value) {
            label.setFont(new Font("Segoe UI", Font.BOLD, 16));
            label.setForeground(PRIMARY);
        } else {
            label.setFont(FONT);
            label.setForeground(TEXT);
        }
    }

    private static void styleTable(JTable table) {
        table.setFont(FONT);
        table.setRowHeight(32);
        table.setForeground(TEXT);
        table.setBackground(CARD);
        table.setSelectionBackground(SELECT);
        table.setSelectionForeground(TEXT);
        table.setGridColor(new Color(0xE4EBE6));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new StripeRenderer());

        JTableHeader header = table.getTableHeader();
        header.setBackground(HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setDefaultRenderer(new HeaderRenderer());
        header.setReorderingAllowed(false);
    }

    private static class StripeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(new EmptyBorder(0, 10, 0, 10));
            setFont(FONT);
            if (isSelected) {
                setBackground(SELECT);
                setForeground(TEXT);
            } else {
                setBackground(row % 2 == 0 ? CARD : STRIPE);
                setForeground(TEXT);
            }
            return component;
        }
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBackground(HEADER);
            label.setForeground(Color.WHITE);
            label.setFont(FONT_BOLD);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setBorder(new EmptyBorder(0, 10, 0, 10));
            label.setOpaque(true);
            return label;
        }
    }
}
