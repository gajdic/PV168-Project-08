package cz.muni.fi.pv168.cashflowevidence.app;


import cz.muni.fi.pv168.cashflowevidence.model.CategoryTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.image.BufferedImage;

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

        var colIndex = overviewTable.getModel().getClass() == CategoryTableModel.class ? 1 : 4;
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

    protected JScrollPane scrollPane() {
        JScrollPane scrollPane = new JScrollPane(overviewTable);
        scrollPane.setInheritsPopupMenu(true);
        return scrollPane;
    }

    /**
     * Method that creates a new image
     * @param rgb of the image
     * @return new image
     */
    public BufferedImage createImage(int rgb) {
        BufferedImage image = new BufferedImage(30, 10, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 10; j++) {
                image.setRGB(i, j, rgb);
            }
        }
        return image;
    }

    /**
     * Custom cell renderer
     */
    static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                return (JLabel)value;
            }
            else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }
}

class ColorCellRenderer extends JPanel implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int rowIndex, int vColIndex) {
        setBackground((Color) value);

        return this;
    }
}