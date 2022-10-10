package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.model.Category;


import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Represents the panel of categories
 */
public class CategoriesOverview extends AbstractOverview {

    private final JTable overviewTable = new JTable(new DefaultTableModel
            (new String[]{"Category name", "Color"}, 0));

    public CategoriesOverview() {
        this.setLayout(new BorderLayout(30, 30));
        addComponents();
    }

    private void addComponents() {
        addLabel();
        addOverview();
    }

    private void addLabel() {
        this.add(new JLabel("Categories"), BorderLayout.NORTH);
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
}
