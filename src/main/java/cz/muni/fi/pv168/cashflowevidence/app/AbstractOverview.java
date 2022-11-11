package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;
import cz.muni.fi.pv168.cashflowevidence.renderer.ColorCellRenderer;

import javax.swing.JPanel;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Serving as a common superclass form all overviews.
 */
abstract public class AbstractOverview extends JPanel {

    private final List<Action> actions;
    protected final JTable overviewTable;

    public AbstractOverview(JTable overviewTable, ArrayList<Action> actions) {
        this.actions = actions;
        this.overviewTable = overviewTable;

        this.setComponentPopupMenu(new RightClickMenu(actions));
        this.overviewTable.setInheritsPopupMenu(true);

        // TODO: enable after finishing the RepeatingTransactionOverview table
        //setColorCellRenderer();
    }
    /**
     * Sets the basic layout and borders
     */
    protected void setLayout() {
        this.setLayout(new BorderLayout(30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
    }

    protected JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setMaximumSize(new Dimension(1920, 50));

        return topPanel;
    }

    protected JPanel getCrudPanel() {
        JPanel crudPanel = new JPanel();
        addActionsToPanel(crudPanel);
        crudPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        return crudPanel;
    }

    private void setColorCellRenderer() {
        int colorColIndex = overviewTable.getModel().getClass().equals(CategoryTableModel.class) ? 1 : 4;
        TableColumn col = overviewTable.getColumnModel().getColumn(colorColIndex);

        col.setCellRenderer(new ColorCellRenderer());
    }

    private void addActionsToPanel(JPanel panel) {
        for (Action action: actions) {
            JButton button = new JButton();
            button.setAction(action);
            panel.add(button);
        }
    }
}
