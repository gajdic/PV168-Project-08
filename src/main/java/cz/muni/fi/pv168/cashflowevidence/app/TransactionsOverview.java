package cz.muni.fi.pv168.cashflowevidence.app;


import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

public class TransactionsOverview extends AbstractOverview {

    private final JPanel topPanel = new JPanel(new BorderLayout(5, 5));
    private final JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

    private final JCheckBox incomingCheckbox = new JCheckBox("Incoming");
    private final JCheckBox outgoingCheckbox = new JCheckBox("Outgoing");
    private final JDatePicker dateFrom = new JDatePicker();
    private final JDatePicker dateTo = new JDatePicker();
    private final JFormattedTextField valueFrom = new JFormattedTextField
            (new CustomNumberFormatter(NumberFormat.getIntegerInstance()));
    private final JFormattedTextField valueTo = new JFormattedTextField
            (new CustomNumberFormatter(NumberFormat.getIntegerInstance()));
    private final JMenu categoriesMenu = new JMenu("All");
    private final JMenu repeatingTransactionsMenu = new JMenu("None");
    private final JButton filterButton = new JButton("Filter");
    private final JButton resetFilterButton = new JButton("Reset filter");
    private final JTable overviewTable = new JTable(new DefaultTableModel
            (new Object[]{"Date", "Amount", "Name", "Description", "Category"}, 0));

    /**
     * Represents the panel of transactions
     */
    public TransactionsOverview() {
        this.setLayout(new BorderLayout(30, 30));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));

        addComponents();

        topPanel.add(optionsPanel, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);
    }

    private void addComponents() {
        topPanel.add(mainLabel(), BorderLayout.NORTH);
        optionsPanel.add(incomingOutgoingPanel());
        optionsPanel.add(fromCalendar());
        optionsPanel.add(toCalendar());
        optionsPanel.add(fromValue());
        optionsPanel.add(toValue());
        optionsPanel.add(categories());
        optionsPanel.add(repeatingTransactions());
        optionsPanel.add(filterButton());
        optionsPanel.add(resetFilterButton());
        this.add(overview(), BorderLayout.CENTER);
    }

    private JLabel mainLabel() {
        JLabel label = new JLabel("Transactions");
        Font font = label.getFont();
        label.setFont(font.deriveFont(Font.BOLD));
        return label;
    }

    private JPanel incomingOutgoingPanel() {
        JPanel incomingOutgoingPanel = new JPanel(new BorderLayout());

        incomingOutgoingPanel.add(incomingCheckbox, BorderLayout.NORTH);
        incomingOutgoingPanel.add(outgoingCheckbox, BorderLayout.CENTER);

        return incomingOutgoingPanel;
    }

    private JPanel fromCalendar() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel("From"), BorderLayout.NORTH);
        panel.add(dateFrom, BorderLayout.CENTER);

        return panel;
    }

    private JPanel toCalendar() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel("To"), BorderLayout.NORTH);
        panel.add(dateTo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel fromValue() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(50, 30));

        panel.add(new JLabel("Value from"), BorderLayout.NORTH);
        panel.add(new JLabel("€"), BorderLayout.EAST);
        panel.add(valueFrom, BorderLayout.CENTER);

        return panel;
    }

    private JPanel toValue() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(50, 30));

        panel.add(new JLabel("Value to"), BorderLayout.NORTH);
        panel.add(new JLabel("€"), BorderLayout.EAST);
        panel.add(valueTo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel categories() {
        JPanel categoriesPanel = new JPanel(new BorderLayout());
        JMenuBar categoriesBar = new JMenuBar();

        String[] titles = new String[]{"Category 1", "Category 2", "Category 3"}; //temporary categories
        for (String title : titles) {
            categoriesMenu.add(new JMenuItem(title));
        }
        categoriesMenu.setPreferredSize(new Dimension(50, 20));
        categoriesBar.add(categoriesMenu);
        categoriesPanel.add(new JLabel("Category"), BorderLayout.NORTH);
        categoriesPanel.add(categoriesBar, BorderLayout.CENTER);

        return categoriesPanel;
    }

    private JPanel repeatingTransactions() {
        JPanel repeatingTransactionsPanel = new JPanel(new BorderLayout());
        JMenuBar repeatingTransactionsBar = new JMenuBar();

        String[] titles = new String[]{"Repeating transaction 1",
                "Repeating transaction 2", "Repeating transaction 3"}; //temporary repeating transactions
        for (String title : titles) {
            repeatingTransactionsMenu.add(new JMenuItem(title));
        }

        repeatingTransactionsMenu.setPreferredSize(new Dimension(110, 20));
        repeatingTransactionsBar.add(repeatingTransactionsMenu);
        repeatingTransactionsPanel.add(new JLabel("Repeating transactions"), BorderLayout.NORTH);
        repeatingTransactionsPanel.add(repeatingTransactionsBar, BorderLayout.CENTER);

        return repeatingTransactionsPanel;
    }

    private JButton filterButton() {
        return filterButton;
    }

    private JButton resetFilterButton() {
        return resetFilterButton;
    }

    private JScrollPane overview() {
        addRows(new Object[]{"28.2.2022", "132$", "Lidl", "Dinner", Color.RED}); //temporary
        addRows(new Object[]{"18.8.2022", "534$", "New bicycle", "Mountain bike", Color.YELLOW}); //temporary

        return new JScrollPane(overviewTable);
    }

    /**
     * Temporary method for adding temporary data to fill out the table
     * @param data to be added
     */
    private void addRows(Object[] data) {
        DefaultTableModel model = (DefaultTableModel) overviewTable.getModel();

        TableColumn col = overviewTable.getColumnModel().getColumn(4);
        col.setCellRenderer(new CellRenderer());

        Color color = (Color) data[4];
        int rgb = color.getRGB();
        JLabel label = new JLabel(new ImageIcon(createImage(rgb)));
        data[4] = label;

        model.addRow(data);
    }

    /**
     * Custom number formatter used in 'ValueFrom' and 'ValueTo' text fields.
     */
    static class CustomNumberFormatter extends NumberFormatter {

        CustomNumberFormatter(NumberFormat numberFormat) {
            super(numberFormat);
        }
        @Override
        public Object stringToValue(String text) throws ParseException {
            if (Objects.equals(text, "")) {
                return null;
            }
            return super.stringToValue(text);
        }

        @Override
        public void setAllowsInvalid(boolean allowsInvalid) {
            super.setAllowsInvalid(false);
        }

        @Override
        public void setValueClass(Class<?> valueClass) {
            super.setValueClass(Integer.class);
        }
    }
}
