package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.model.Category;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * Represents the panel of categories
 */
public class CategoriesOverview extends JPanel {

    private final JPanel topPanel = new JPanel(new BorderLayout());

    private final JButton editRowButton = new JButton("Edit");
    private final JButton deleteRowButton = new JButton("Delete");

    private final JTable overviewTable = new JTable(new DefaultTableModel
            (new String[]{"Category name", "Color"}, 0));

    public CategoriesOverview() {
        this.setLayout(new BorderLayout(30, 30));
        addComponents();
    }

    private void addComponents() {
        this.add(topPanel, BorderLayout.NORTH);
        addOverview();
        addRowActionsPanel();
    }

    private void addRowActionsPanel() {
        JPanel panel = new JPanel();
        panel.add(editRowButton);
        panel.add(deleteRowButton);

        topPanel.add(panel, BorderLayout.WEST);
    }

    private void addOverview() {
        addRows(new Category("Grocery", Color.BLUE)); //temporary
        addRows(new Category("Sport", Color.RED)); //temporary

        overviewTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        overviewTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        JScrollPane scrollPane = new JScrollPane(overviewTable);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        this.add(scrollPane, BorderLayout.WEST);
    }

    /**
     * Method for adding new category into the overview table
     * @param category to be added
     */
    public void addRows(Category category) {
        DefaultTableModel model = (DefaultTableModel) overviewTable.getModel();

        TableColumn col = overviewTable.getColumnModel().getColumn(1);
        col.setCellRenderer(new CellRenderer());

        int rgb =category.getColor().getRGB();
        JLabel label = new JLabel(new ImageIcon(createImage(rgb)));

        model.addRow(new Object[]{category.getName(), label});
    }

    /**
     * Method that creates a new image
     * @param rgb of the image
     * @return new image
     */
    private BufferedImage createImage(int rgb) {
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
