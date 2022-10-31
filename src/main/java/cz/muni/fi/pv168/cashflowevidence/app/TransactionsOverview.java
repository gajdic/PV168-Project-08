package cz.muni.fi.pv168.cashflowevidence.app;


import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.renderer.AmountCellRenderer;
import cz.muni.fi.pv168.cashflowevidence.renderer.DateTimeCellRenderer;

import javax.swing.Action;
import javax.swing.ButtonGroup;
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
import javax.swing.text.NumberFormatter;
import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

// TODO: This class is too long. Consider splitting in multiple separate (Maybe more reusable?) Ones.
public class TransactionsOverview extends AbstractOverview {

    //incoming/outgoing
    private final JCheckBox incomingCheckbox = new JCheckBox("Incoming", true);
    private final JCheckBox outgoingCheckbox = new JCheckBox("Outgoing", true);

    //dates
    private final DatePicker dateFrom = new DatePicker();
    private final DatePicker dateTo = new DatePicker();

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
    private final TransactionFilter filter;


    /**
     * Represents the panel of transactions
     */
    public TransactionsOverview(JTable transactionsTable, ArrayList<Action> actions, JLabel totalBalance, JLabel filteredBalance) {
        super(transactionsTable, actions);
        setLayout();

        // TODO: Refactor and move this to AbstractOverview using abstract topPanel and Overview methods
        // TODO: Needed to not initialize fields in this class, as they are null;
        add(topPanel(), BorderLayout.NORTH);
        add(new JScrollPane(overviewTable));
        setAmountCellRenderer();
        setDateTimeCellRenderer();
        this.filter = new TransactionFilter(transactionsTable, getComponentList(), totalBalance, filteredBalance);
        generateCategories();
    }

    private JPanel topPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        JPanel optionsPanel = optionsPanel();
        topPanel.add(optionsPanel, BorderLayout.WEST);
        return topPanel;
    }

    private JPanel optionsPanel() {
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
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
        return optionsPanel;
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
        panel.add(new JLabel("$"), BorderLayout.EAST);
        panel.add(valueFrom, BorderLayout.CENTER);

        return panel;
    }

    private JPanel toValue() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(65, 30));

        panel.add(new JLabel("Value to"), BorderLayout.NORTH);
        panel.add(new JLabel("$"), BorderLayout.EAST);
        panel.add(valueTo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel rowActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        addActionsToPanel(panel);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.EAST);

        return panel;
    }

    // TODO: Split in multiple functions
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
        categoriesJComboBox.setSelectedIndex(0);
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
        filterButton.setMnemonic(KeyEvent.VK_I);
        filterButton.addActionListener(l -> filter.filterRows());
        return filterButton;
    }

    private JButton resetFilterButton() {
        resetFilterButton.setMnemonic(KeyEvent.VK_R);
        resetFilterButton.addActionListener(e -> filter.resetFilter());
        return resetFilterButton;
    }

    private void setAmountCellRenderer() {
        overviewTable.getColumnModel().getColumn(2).setCellRenderer(new AmountCellRenderer());
    }

    private void setDateTimeCellRenderer() {
        overviewTable.getColumnModel().getColumn(3).setCellRenderer(new DateTimeCellRenderer());
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
     *
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

    public ArrayList<JComponent> getComponentList() {
        ArrayList<JComponent> componentList = new ArrayList<>();
        componentList.add(incomingCheckbox);
        componentList.add(outgoingCheckbox);
        componentList.add(dateFrom);
        componentList.add(dateTo);
        componentList.add(valueFrom);
        componentList.add(valueTo);
        componentList.add(allCategories);
        componentList.add(specifiedCategories);
        componentList.add(categoriesJComboBox);
        componentList.add(allTransactions);
        componentList.add(onlyNonRepeating);
        componentList.add(onlyRepeating);

        return componentList;
    }
}
