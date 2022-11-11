package cz.muni.fi.pv168.cashflowevidence.app;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * Represents the panel of categories
 */
public class CategoriesOverview extends AbstractOverview {

    public CategoriesOverview(JTable categoryTable, ArrayList<Action> actions) {
        super(categoryTable, actions);
        setLayout();

        JPanel topPanel = getTopPanel();
        topPanel.add(getCrudPanel(), BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        var scrollPane = new JScrollPane(overviewTable);
        scrollPane.setComponentPopupMenu(new RightClickMenu(actions));
        add(scrollPane);
    }
}
