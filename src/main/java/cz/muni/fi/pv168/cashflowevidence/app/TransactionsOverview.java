package cz.muni.fi.pv168.cashflowevidence.app;


import cz.muni.fi.pv168.cashflowevidence.model.Category;
import org.jdatepicker.JDatePicker;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

public class TransactionsOverview extends AbstractOverview {

    private final JPanel topPanel = new JPanel(new BorderLayout(5, 5));
    private final JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

    //incoming/outgoing
    private final JCheckBox incomingCheckbox = new JCheckBox("Incoming");
    private final JCheckBox outgoingCheckbox = new JCheckBox("Outgoing");

    //dates
    private final JDatePicker dateFrom = new JDatePicker();
    private final JDatePicker dateTo = new JDatePicker();

    //values
    private final JFormattedTextField valueFrom = new JFormattedTextField
            (new CustomNumberFormatter(NumberFormat.getIntegerInstance()));
    private final JFormattedTextField valueTo = new JFormattedTextField
            (new CustomNumberFormatter(NumberFormat.getIntegerInstance()));

    //categorises
    private final JRadioButton allCategories = new JRadioButton("All", true);
    private final JRadioButton specifiedCategories = new JRadioButton("Choose", false);
    private final JComboBox<Category> categoriesJComboBox = new JComboBox<>();

    //repeating transactions
    private final JRadioButton allTransactions = new JRadioButton("All", true);
    private final JRadioButton onlyNonRepeating = new JRadioButton("Only non-repeating", false);
    private final JRadioButton onlyRepeating = new JRadioButton("Only repeating", false);

    //filtration
    private final JButton filterButton = new JButton("Filter");
    private final JButton resetFilterButton = new JButton("Reset filter");

    //overview
    private final JTable overviewTable = new JTable(new DefaultTableModel
            (new Object[]{"Date", "Amount", "Name", "Description", "Category"}, 0));

    /**
     * Represents the panel of transactions
     */
    public TransactionsOverview() {
        setLayout();
        addComponents();

        topPanel.add(optionsPanel, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);

        generateCategories();
    }


    private void addComponents() {
        optionsPanel.add(rowActionsPanel());
        optionsPanel.add(new JSeparator(JSeparator.VERTICAL));
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
        panel.setPreferredSize(new Dimension(65, 30));

        panel.add(new JLabel("Value from"), BorderLayout.NORTH);
        panel.add(new JLabel("€"), BorderLayout.EAST);
        panel.add(valueFrom, BorderLayout.CENTER);

        return panel;
    }

    private JPanel toValue() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(65, 30));

        panel.add(new JLabel("Value to"), BorderLayout.NORTH);
        panel.add(new JLabel("€"), BorderLayout.EAST);
        panel.add(valueTo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel categories() {
        JPanel categoriesPanel = new JPanel(new BorderLayout());
        JPanel checkboxPanel = new JPanel(new GridLayout(2, 1));
        JPanel comboPanel = new JPanel();

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(allCategories);
        buttonGroup.add(specifiedCategories);

        checkboxPanel.add(allCategories);
        checkboxPanel.add(specifiedCategories);

        categoriesJComboBox.setVisible(false);
        allCategories.addActionListener(e -> specifiedCategorySetTrue());
        specifiedCategories.addActionListener(e -> specifiedCategorySetTrue());

        Category[] temporaryCategories = generateCategories(); //temporary
        for (Category temporaryCategory : temporaryCategories) {
            categoriesJComboBox.addItem(temporaryCategory);
        }

        comboPanel.add(categoriesJComboBox);
        categoriesJComboBox.setPreferredSize(new Dimension(
                (int) categoriesJComboBox.getPreferredSize().getWidth() + 10,
                (int) categoriesJComboBox.getPreferredSize().getHeight()));

        categoriesPanel.add(new JLabel("Categories"), BorderLayout.NORTH);
        categoriesPanel.add(checkboxPanel, BorderLayout.WEST);
        categoriesPanel.add(comboPanel, BorderLayout.CENTER);

        return categoriesPanel;
    }

    private void specifiedCategorySetTrue() {
        categoriesJComboBox.setVisible(specifiedCategories.isSelected());
    }

    private JPanel repeatingTransactions() {
        JPanel repeatingTransactionsPanel = new JPanel(new BorderLayout());
        JPanel checkboxPanel = new JPanel(new GridLayout(3, 1));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(allTransactions);
        buttonGroup.add(onlyRepeating);
        buttonGroup.add(onlyNonRepeating);

        checkboxPanel.add(allTransactions);
        checkboxPanel.add(onlyRepeating);
        checkboxPanel.add(onlyNonRepeating);

        repeatingTransactionsPanel.add(new JLabel("Repeating transactions"), BorderLayout.NORTH);
        repeatingTransactionsPanel.add(checkboxPanel, BorderLayout.WEST);

        return repeatingTransactionsPanel;
    }

    private JButton filterButton() {
        return filterButton;
    }

    private JButton resetFilterButton() {
        return resetFilterButton;
    }

    private JScrollPane overview() {
        addRow(new Object[]{"28.2.2022", "132$", "Lidl", "Dinner", Color.RED}); //temporary
        addRow(new Object[]{"18.8.2022", "534$", "New bicycle", "Mountain bike", Color.YELLOW}); //temporary

        return new JScrollPane(overviewTable);
    }

    /**
     * Method for adding data to the table
     * @param data to be added
     */
    private void addRow(Object[] data) {
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

    /**
     * Temporary data generator
     * @return an array of Categories
     */
    private Category[] generateCategories() {
        Category[] categories = new Category[3];
        String[] titles = new String[]{"Category 1", "Category 2", "Category 3"};
        Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN};

        for (int i = 0; i < titles.length; i++) {
            categories[i] = new Category(titles[i], colors[i]);
        }
        return categories;
    }
}
