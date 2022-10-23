package cz.muni.fi.pv168.cashflowevidence.app;


import cz.muni.fi.pv168.cashflowevidence.model.Category;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Represents the panel of categories
 */
public class CategoriesOverview extends AbstractOverview {

    private final JPanel topPanel = new JPanel();
    private final JTable overviewTable = new JTable(new DefaultTableModel
            (new String[]{"Category name", "Color"}, 0));

    public CategoriesOverview() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponents();
    }

    private void addComponents() {
        setupTopPanel();
        this.add(topPanel);
        this.add(overview());
    }

    private void setupTopPanel() {
        topPanel.add(editRowButton);
        topPanel.add(deleteRowButton);
        topPanel.setMaximumSize(new Dimension(1920, 50));
    }

    private JPanel overview() {
        addRow(new Category("Grocery", Color.BLUE)); //temporary
        addRow(new Category("Sport", Color.RED)); //temporary

        JScrollPane scrollPane = new JScrollPane(overviewTable);

        JPanel tablePanel = new JPanel();
        tablePanel.add(scrollPane);

        return tablePanel;
    }

    /**
     * Method for adding new category into the overview table
     * @param category to be added
     */
    public void addRow(Category category) {
        DefaultTableModel model = (DefaultTableModel) overviewTable.getModel();

        TableColumn col = overviewTable.getColumnModel().getColumn(1);
        col.setCellRenderer(new CellRenderer());

        int rgb =category.getColor().getRGB();
        JLabel label = new JLabel(new ImageIcon(createImage(rgb)));

        model.addRow(new Object[]{category.getName(), label});
    }
}
