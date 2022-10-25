package cz.muni.fi.pv168.cashflowevidence.app;


import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

/**
 * Serving as a common superclass form all overviews.
 */
public class AbstractOverview extends JPanel {

    protected final JButton editRowButton = new JButton("Edit selected");
    protected final JButton deleteRowButton = new JButton("Delete selected");

    protected final JPanel topPanel = new JPanel();
    protected final JTable overviewTable;

    public AbstractOverview(JTable overviewTable, String containerContextName) {
        this.overviewTable = overviewTable;
        this.setComponentPopupMenu(new RightClickMenu(containerContextName));
        this.overviewTable.setInheritsPopupMenu(true);

        var colIndex = overviewTable.getModel().getClass().equals(CategoryTableModel.class) ? 1 : 5;
        TableColumn col = overviewTable.getColumnModel().getColumn(colIndex);
        col.setCellRenderer(new ColorCellRenderer());
    }
    /**
     * Sets the basic layout and borders
     */
    public void setLayout() {
        this.setLayout(new BorderLayout(30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
    }

    protected JPanel rowActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(editRowButton);
        panel.add(deleteRowButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.EAST);

        return panel;
    }
}

class ColorCellRenderer extends JPanel implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int rowIndex, int vColIndex) {
        if (value.getClass().equals(Category.class)) {
            setBackground(((Category) value).getColor());
            return this;
        }

        setBackground((Color) value);
        return this;
    }
}