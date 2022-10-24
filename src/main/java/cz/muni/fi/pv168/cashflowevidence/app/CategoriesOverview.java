package cz.muni.fi.pv168.cashflowevidence.app;


import cz.muni.fi.pv168.cashflowevidence.model.Category;

import javax.swing.BoxLayout;
import javax.swing.JTable;
import java.awt.Dimension;

/**
 * Represents the panel of categories
 */
public class CategoriesOverview extends AbstractOverview {

    public CategoriesOverview(JTable catTable) {
        super(catTable, "Category");
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponents();
    }

    private void addComponents() {
        setupTopPanel();
        this.add(topPanel);
        this.add(overviewTable);
    }

    private void setupTopPanel() {
        topPanel.add(editRowButton);
        topPanel.add(deleteRowButton);
        topPanel.setMaximumSize(new Dimension(1920, 50));
    }
}
