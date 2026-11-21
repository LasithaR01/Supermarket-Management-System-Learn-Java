package cmart.ui.theme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Builds the layout used when the app runs. NetBeans Design view keeps the old forms.
 */
public final class RunView {

    private RunView() {
    }

    public static void install(JPanel screen, JComponent body) {
        screen.removeAll();
        screen.setLayout(new BorderLayout());
        screen.setBackground(UiTheme.PAGE);
        screen.setBorder(new EmptyBorder(4, 4, 4, 4));
        screen.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        screen.add(body, BorderLayout.CENTER);
        UiTheme.apply(screen);
        screen.revalidate();
        screen.repaint();
    }

    public static JPanel grid() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        return panel;
    }

    public static JPanel card(JComponent content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UiTheme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UiTheme.LINE, 1, true),
                new EmptyBorder(14, 14, 14, 14)));
        card.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    public static JPanel page(JComponent north, JComponent center, JComponent south) {
        JPanel page = new JPanel(new BorderLayout(0, 12));
        page.setOpaque(false);
        page.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        page.setBorder(new EmptyBorder(8, 4, 4, 4));
        if (north != null) {
            page.add(north, BorderLayout.NORTH);
        }
        if (center != null) {
            page.add(center, BorderLayout.CENTER);
        }
        if (south != null) {
            page.add(south, BorderLayout.SOUTH);
        }
        return page;
    }

    public static void place(JPanel grid, JComponent component, int x, int y, int width, double weightX) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.weightx = weightX;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(4, 6, 4, 6);
        grid.add(component, constraints);
    }

    public static JPanel stack(JLabel label, JComponent field) {
        String text = label.getText() == null ? "" : label.getText().replace(":", "").trim();
        label.setText(text);
        JPanel box = new JPanel(new BorderLayout(0, 4));
        box.setOpaque(false);
        box.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        box.add(label, BorderLayout.NORTH);
        field.setMinimumSize(new Dimension(80, 28));
        box.add(field, BorderLayout.CENTER);
        return box;
    }

    public static JPanel actions(JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);
        bar.putClientProperty(UiTheme.KEEP, Boolean.TRUE);
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == null) {
                continue;
            }
            buttons[i].setPreferredSize(new Dimension(148, 38));
            bar.add(buttons[i]);
        }
        return bar;
    }

    public static void prepareTable(JScrollPane scroll, boolean stretchColumns) {
        scroll.setPreferredSize(new Dimension(480, 280));
        scroll.setMinimumSize(new Dimension(200, 160));
        if (scroll.getViewport().getView() instanceof JTable) {
            JTable table = (JTable) scroll.getViewport().getView();
            table.setAutoResizeMode(stretchColumns
                    ? JTable.AUTO_RESIZE_ALL_COLUMNS
                    : JTable.AUTO_RESIZE_OFF);
        }
    }

    public static void prepareArea(JScrollPane scroll) {
        scroll.setPreferredSize(new Dimension(200, 78));
        scroll.setMinimumSize(new Dimension(80, 64));
    }

    public static void showPeople(
            JPanel screen,
            String noun,
            JTabbedPane tabs,
            JLabel searchIdLabel, JTextField searchId, JButton searchButton,
            JLabel nameLabel, JTextField nameField,
            JLabel phoneLabel, JTextField phoneField,
            JLabel billLabel, JScrollPane billScroll,
            JLabel shipLabel, JScrollPane shipScroll,
            JCheckBox sameBox,
            JLabel bankLabel, JTextField bankField,
            JLabel cityLabel, JTextField cityField,
            JLabel contactLabel, JTextField contactField,
            JLabel personLabel, JTextField personField,
            JLabel personPhoneLabel, JTextField personPhoneField,
            JLabel emailLabel, JTextField emailField,
            JLabel onlineLabel, JTextField onlineField,
            JButton save, JButton update, JButton delete,
            JLabel findNameLabel, JTextField findName,
            JLabel findCityLabel, JTextField findCity,
            JLabel findPhoneLabel, JTextField findPhone,
            JLabel findContactLabel, JTextField findContact,
            JLabel findPersonLabel, JTextField findPerson,
            JScrollPane tableScroll,
            JButton editButton,
            JButton allReports,
            JLabel reportIdLabel, JTextField reportId, JButton viewReport) {

        prepareArea(billScroll);
        prepareArea(shipScroll);
        prepareTable(tableScroll, false);
        reportId.setPreferredSize(null);

        JPanel details = grid();
        place(details, stack(searchIdLabel, searchId), 0, 0, 1, 1);
        searchButton.setPreferredSize(new Dimension(120, 36));
        place(details, searchButton, 1, 0, 1, 0);
        place(details, stack(nameLabel, nameField), 0, 1, 1, 1);
        place(details, stack(phoneLabel, phoneField), 1, 1, 1, 1);
        place(details, stack(billLabel, billScroll), 0, 2, 1, 1);
        place(details, stack(shipLabel, shipScroll), 1, 2, 1, 1);
        place(details, sameBox, 0, 3, 2, 1);
        place(details, stack(bankLabel, bankField), 0, 4, 1, 1);
        place(details, stack(cityLabel, cityField), 1, 4, 1, 1);

        JLabel contactTitle = new JLabel("Contact person");
        contactTitle.setFont(UiTheme.FONT_BOLD);
        contactTitle.setForeground(UiTheme.TEXT);
        place(details, contactTitle, 0, 5, 2, 1);
        place(details, stack(personLabel, personField), 0, 6, 1, 1);
        place(details, stack(contactLabel, contactField), 1, 6, 1, 1);
        place(details, stack(personPhoneLabel, personPhoneField), 0, 7, 1, 1);
        place(details, stack(emailLabel, emailField), 1, 7, 1, 1);
        place(details, stack(onlineLabel, onlineField), 0, 8, 1, 1);

        JPanel find = grid();
        place(find, stack(findNameLabel, findName), 0, 0, 1, 1);
        place(find, stack(findCityLabel, findCity), 1, 0, 1, 1);
        place(find, stack(findPhoneLabel, findPhone), 0, 1, 1, 1);
        place(find, stack(findContactLabel, findContact), 1, 1, 1, 1);
        place(find, stack(findPersonLabel, findPerson), 0, 2, 1, 1);

        JPanel reports = grid();
        allReports.setPreferredSize(new Dimension(220, 40));
        viewReport.setPreferredSize(new Dimension(160, 38));
        place(reports, allReports, 0, 0, 1, 0);
        place(reports, stack(reportIdLabel, reportId), 0, 1, 1, 0.4);
        place(reports, viewReport, 1, 1, 1, 0);

        tabs.removeAll();
        tabs.addTab(noun, page(card(details), null, actions(save, update, delete)));
        tabs.addTab("Find", page(card(find), tableScroll, editButton == null ? null : actions(editButton)));
        tabs.addTab("Reports", page(card(reports), null, null));

        install(screen, tabs);
    }
}
