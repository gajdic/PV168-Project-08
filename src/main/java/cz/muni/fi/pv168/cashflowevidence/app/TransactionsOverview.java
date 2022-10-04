package cz.muni.fi.pv168.cashflowevidence.app;


import org.jdatepicker.JDatePicker;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class TransactionsOverview extends JPanel {

    private final JPanel topPanel = new JPanel(new BorderLayout(5, 5));
    private final JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

    private final JCheckBox incomingCheckbox = new JCheckBox("Incoming");
    private final JCheckBox outgoingCheckbox = new JCheckBox("Outgoing");
    private final JDatePicker dateFrom = new JDatePicker();
    private final JDatePicker dateTo = new JDatePicker();
    private final JMenu categoriesMenu = new JMenu("All");
    private final JMenu currencyMenu = new JMenu("All");
    private final JMenu templatesMenu = new JMenu("None");
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
        addCategories();
        addCurrency();
        addTemplates();
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

    private void addCategories() {
        JPanel categoriesPanel = new JPanel(new BorderLayout());

        String[] titles = new String[]{"Category 1", "Category 2", "Category 3"}; //temporary categories
        for (String title : titles) {
            categoriesMenu.add(new JMenuItem(title));
        }

        categoriesPanel.add(new JLabel("Category"), BorderLayout.NORTH);
        categoriesPanel.add(categoriesMenu, BorderLayout.CENTER);
        optionsPanel.add(categoriesPanel);
    }

    private void addCurrency() {
        JPanel currencyPanel = new JPanel(new BorderLayout());

        String[] titles = new String[]{"Currency 1", "Currency 2", "Currency 3"}; //temporary currency
        for (String title : titles) {
            currencyMenu.add(new JMenuItem(title));
        }

        currencyPanel.add(new JLabel("Currency"), BorderLayout.NORTH);
        currencyPanel.add(currencyMenu, BorderLayout.CENTER);
        optionsPanel.add(currencyPanel);
    }

    private void addTemplates() {
        JPanel templatePanel = new JPanel(new BorderLayout());

        String[] titles = new String[]{"Template 1", "Template 2", "Template 3"}; //temporary templates
        for (String title : titles) {
            templatesMenu.add(new JMenuItem(title));
        }

        templatePanel.add(new JLabel("Template"), BorderLayout.NORTH);
        templatePanel.add(templatesMenu, BorderLayout.CENTER);
        optionsPanel.add(templatePanel);
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
