package cmart.ui.settings;

import cmart.config.SettingKeys;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.service.SettingService;
import cmart.ui.theme.UiTheme;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Shop options editor. Values persist in {@code app_setting} via {@link SettingService}.
 */
public class SettingsPanel extends JPanel {

    private final SettingService settingService = new SettingService();

    private final JTextField shopNameField = new JTextField(24);
    private final JTextField taglineField = new JTextField(24);
    private final JTextField receiptFooterField = new JTextField(24);
    private final JTextField currencyField = new JTextField(8);
    private final JTextField taxRateField = new JTextField(8);
    private final JTextField lowStockField = new JTextField(8);
    private final JCheckBox taxEnabledBox = new JCheckBox("Enable tax on bills");
    private final JCheckBox allowNegativeStockBox = new JCheckBox("Allow selling below stock (not recommended)");

    public SettingsPanel() {
        setLayout(new BorderLayout(0, 12));
        setBackground(UiTheme.PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        buildUi();
        loadValues();
    }

    private void buildUi() {
        JLabel title = new JLabel("Shop settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UiTheme.SIDEBAR);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(true);
        form.setBackground(java.awt.Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;
        row = addRow(form, gc, row, "Shop name", shopNameField);
        row = addRow(form, gc, row, "Tagline", taglineField);
        row = addRow(form, gc, row, "Receipt footer", receiptFooterField);
        row = addRow(form, gc, row, "Currency symbol", currencyField);
        row = addRow(form, gc, row, "Tax rate %", taxRateField);
        row = addRow(form, gc, row, "Low-stock alert at qty ≤", lowStockField);

        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        form.add(taxEnabledBox, gc);
        gc.gridy = row++;
        form.add(allowNegativeStockBox, gc);

        JButton save = new JButton("Save settings");
        save.setFont(new Font("Segoe UI", Font.BOLD, 14));
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveValues();
            }
        });
        gc.gridy = row;
        form.add(save, gc);

        add(form, BorderLayout.CENTER);

        if (!SessionContext.can(Permission.SETTINGS)) {
            save.setEnabled(false);
            JOptionPane.showMessageDialog(this, "You do not have permission to change settings.");
        }
    }

    private int addRow(JPanel form, GridBagConstraints gc, int row, String caption, JTextField field) {
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = row;
        JLabel label = new JLabel(caption);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(label, gc);
        gc.gridx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(field, gc);
        return row + 1;
    }

    private void loadValues() {
        try {
            Map<String, String> all = settingService.loadAll();
            shopNameField.setText(all.get(SettingKeys.SHOP_NAME));
            taglineField.setText(all.get(SettingKeys.SHOP_TAGLINE));
            receiptFooterField.setText(all.get(SettingKeys.RECEIPT_FOOTER));
            currencyField.setText(all.get(SettingKeys.CURRENCY_SYMBOL));
            taxRateField.setText(all.get(SettingKeys.TAX_RATE_PERCENT));
            lowStockField.setText(all.get(SettingKeys.STOCK_LOW_THRESHOLD));
            taxEnabledBox.setSelected(Boolean.parseBoolean(all.get(SettingKeys.TAX_ENABLED)));
            allowNegativeStockBox.setSelected(Boolean.parseBoolean(all.get(SettingKeys.ALLOW_NEGATIVE_STOCK)));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load settings: " + e.getMessage());
        }
    }

    private void saveValues() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put(SettingKeys.SHOP_NAME, shopNameField.getText());
        values.put(SettingKeys.SHOP_TAGLINE, taglineField.getText());
        values.put(SettingKeys.RECEIPT_FOOTER, receiptFooterField.getText());
        values.put(SettingKeys.CURRENCY_SYMBOL, currencyField.getText());
        values.put(SettingKeys.TAX_RATE_PERCENT, taxRateField.getText());
        values.put(SettingKeys.STOCK_LOW_THRESHOLD, lowStockField.getText());
        values.put(SettingKeys.TAX_ENABLED, String.valueOf(taxEnabledBox.isSelected()));
        values.put(SettingKeys.ALLOW_NEGATIVE_STOCK, String.valueOf(allowNegativeStockBox.isSelected()));
        try {
            settingService.save(values);
            JOptionPane.showMessageDialog(this, "Settings saved. Re-open Home to refresh the shop name.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not save: " + e.getMessage());
        }
    }
}
