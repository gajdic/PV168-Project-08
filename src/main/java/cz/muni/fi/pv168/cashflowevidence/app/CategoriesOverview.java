package cz.muni.fi.pv168.cashflowevidence.app;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

/**
 * Represents the panel of categories
 */
public class CategoriesOverview extends AbstractOverview {

    public CategoriesOverview(JTable categoryTable, ArrayList<Action> actions) {
        super(categoryTable, actions);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(getTopPanel(), BorderLayout.NORTH);

        var scrollPane = new JScrollPane(overviewTable);
        scrollPane.setComponentPopupMenu(new RightClickMenu(actions));
        add(scrollPane);
    }

    protected JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(1920, 50));
        addActionsToPanel(topPanel);
        return topPanel;
    }
}
