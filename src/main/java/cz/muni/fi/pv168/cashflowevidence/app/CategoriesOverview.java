package cz.muni.fi.pv168.cashflowevidence.app;

import cz.muni.fi.pv168.cashflowevidence.model.Category;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Represents the panel of categories
 */
public class CategoriesOverview extends AbstractOverview {

    private final JTable overviewTable = new JTable(new DefaultTableModel
            (new String[]{"Category name", "Color"}, 0));

    public CategoriesOverview() {
        this.setLayout(new BorderLayout(30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
        addComponents();
    }

    private void addComponents() {
        this.add(mainLabel(), BorderLayout.NORTH);
        this.add(overview(), BorderLayout.CENTER);
    }

    private JLabel mainLabel() {
        JLabel label = new JLabel("Categories");
        Font font = label.getFont();
        label.setFont(font.deriveFont(Font.BOLD));
        return label;
    }

    private JPanel overview() {
        addRows(new Category("Grocery", Color.BLUE)); //temporary
        addRows(new Category("Sport", Color.RED)); //temporary

        JScrollPane scrollPane = new JScrollPane(overviewTable);

        JPanel tablePanel = new JPanel();
        tablePanel.add(scrollPane);

        return tablePanel;
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
