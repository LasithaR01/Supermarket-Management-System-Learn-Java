package pos.pro;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Swaps the screen shown in the main area and lets it scroll if it is taller than the window.
 */
public class JpanelLoader {

    public void jPanelLoader(JPanel main, JPanel setPanel) {
        UiTheme.apply(setPanel);
        main.removeAll();
        main.setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(setPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UiTheme.PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        main.add(scroll, BorderLayout.CENTER);
        main.revalidate();
        main.repaint();
    }
}
