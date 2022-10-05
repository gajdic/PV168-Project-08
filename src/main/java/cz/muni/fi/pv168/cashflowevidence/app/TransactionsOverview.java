package cz.muni.fi.pv168.cashflowevidence.app;


import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionsOverview extends JPanel {

    private final JPanel topPanel = new JPanel(new BorderLayout(5, 5));
    private final JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

    private final JCheckBox incomingCheckbox = new JCheckBox("Incoming");
    private final JCheckBox outgoingCheckbox = new JCheckBox("Outgoing");
    private final JDatePicker dateFrom = new JDatePicker();
    private final JDatePicker dateTo = new JDatePicker();
    private final JTextField valueFrom = new JTextField();
    private final JTextField valueTo = new JTextField();
    private final JMenu categoriesMenu = new JMenu("All");
    private final JMenu currencyMenu = new JMenu("All");
    private final JMenu repeatingTransactionsMenu = new JMenu("None");
    private final JTable overviewTable = new JTable(new DefaultTableModel
            (new String[]{"Date", "Amount", "Name", "Description"}, 0));

    /**
     * Represents the panel of transactions
     */
    public TransactionsOverview() {
        this.setLayout(new BorderLayout(30, 30));

        addComponents();

        topPanel.add(optionsPanel, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);
    }

    private void addComponents() {
        addLabel();
        addIncomingOutgoingPanel();
        addFromCalendar();
        addToCalendar();
        addFromValue();
        addToValue();
        addCategories();
        addCurrency();
        addRepeatingTransactions();
        addOverview();
    }

    private void addLabel() {
        JLabel label = new JLabel("Transactions");
        topPanel.add(label, BorderLayout.NORTH);
    }

    private void addIncomingOutgoingPanel() {
        JPanel incomingOutgoingPanel = new JPanel(new BorderLayout());

        incomingOutgoingPanel.add(incomingCheckbox, BorderLayout.NORTH);
        incomingOutgoingPanel.add(outgoingCheckbox, BorderLayout.CENTER);
        optionsPanel.add(incomingOutgoingPanel);
    }

    private void addFromCalendar() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel("From"), BorderLayout.NORTH);
        panel.add(dateFrom, BorderLayout.CENTER);
        optionsPanel.add(panel);
    }

    private void addToCalendar() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel("To"), BorderLayout.NORTH);
        panel.add(dateTo, BorderLayout.CENTER);
        optionsPanel.add(panel);
    }

    private void addFromValue() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(50, 30));

        panel.add(new JLabel("Value from"), BorderLayout.NORTH);
        valueFrom.setEditable(true);
        panel.add(valueFrom, BorderLayout.CENTER);
        optionsPanel.add(panel);
    }

    private void addToValue() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(50, 30));

        panel.add(new JLabel("Value to"), BorderLayout.NORTH);
        valueFrom.setEditable(true);
        panel.add(valueTo, BorderLayout.CENTER);
        optionsPanel.add(panel);
    }

    private void addCategories() {
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
        optionsPanel.add(categoriesPanel);
    }

    private void addCurrency() {
        JPanel currencyPanel = new JPanel(new BorderLayout());
        JMenuBar currencyBar = new JMenuBar();

        String[] titles = new String[]{"Currency 1", "Currency 2", "Currency 3"}; //temporary currency
        for (String title : titles) {
            currencyMenu.add(new JMenuItem(title));
        }

        currencyMenu.setPreferredSize(new Dimension(50, 20));
        currencyBar.add(currencyMenu);
        currencyPanel.add(new JLabel("Currency"), BorderLayout.NORTH);
        currencyPanel.add(currencyBar, BorderLayout.CENTER);
        optionsPanel.add(currencyPanel);
    }

    private void addRepeatingTransactions() {
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
        optionsPanel.add(repeatingTransactionsPanel);
    }

    private void addOverview() {
        addRows(new String[]{"28.2.2022", "132$", "Lidl", "Dinner"}); //temporary
        addRows(new String[]{"18.8.2022", "534$", "New bicycle", "Mountain bike"}); //temporary

        JScrollPane scrollPane = new JScrollPane(overviewTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Temporary method for adding temporary data to fill out the table
     * @param data to be added
     */
    private void addRows(String[] data) {
        DefaultTableModel model = (DefaultTableModel) overviewTable.getModel();
        model.addRow(data);
    }
}
