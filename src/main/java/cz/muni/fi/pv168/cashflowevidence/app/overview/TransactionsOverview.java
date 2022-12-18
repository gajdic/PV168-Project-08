package cz.muni.fi.pv168.cashflowevidence.app.overview;


import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.cashflowevidence.UI.CustomComboBoxUI;
import cz.muni.fi.pv168.cashflowevidence.app.InputNumberFormatter;
import cz.muni.fi.pv168.cashflowevidence.app.RightClickMenu;
import cz.muni.fi.pv168.cashflowevidence.app.TransactionFilter;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.renderer.AmountCellRenderer;
import cz.muni.fi.pv168.cashflowevidence.renderer.CategoryColorListCellRenderer;
import cz.muni.fi.pv168.cashflowevidence.renderer.DateTimeCellRenderer;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;


// TODO: This class is too long. Consider splitting in multiple separate (Maybe more reusable?) Ones.
public class TransactionsOverview extends AbstractOverview {

    //incoming/outgoing
    private final JCheckBox incomingCheckbox = new JCheckBox("Incoming");
    private final JCheckBox outgoingCheckbox = new JCheckBox("Outgoing");

    //dates
    private final DatePicker dateFrom = new DatePicker();
    private final DatePicker dateTo = new DatePicker();

    //values
    private final JFormattedTextField valueFrom = new JFormattedTextField
            (new InputNumberFormatter(InputNumberFormatter.getDecimalFormat()));
    private final JFormattedTextField valueTo = new JFormattedTextField
            (new InputNumberFormatter(InputNumberFormatter.getDecimalFormat()));

    //categorises
    private final JRadioButton allCategories = new JRadioButton("All", true);
    private final JRadioButton specifiedCategories = new JRadioButton("Choose", false);
    private final JComboBox<Category> categoriesJComboBox = new JComboBox<>();

    //repeating transactions
    private final JCheckBox nonRepeatingCheckbox = new JCheckBox("Non repeating", true);
    private final JCheckBox repeatingCheckbox = new JCheckBox("Repeating", true);

    //filtration
    private final JButton filterButton = new JButton("Filter");
    private final JButton resetFilterButton = new JButton("Reset filter");
    private final TransactionFilter filter;

    private final Repository<Category> categoryRepository;

    /**
     * Represents the panel of transactions
     */
    public TransactionsOverview(JTable transactionsTable, ArrayList<Action> actions, JLabel totalBalance, JLabel filteredBalance, Repository<Category> categoryRepository) {
        super(transactionsTable, actions);
        setLayout();

        this.categoryRepository = categoryRepository;
        this.filter = new TransactionFilter(transactionsTable, totalBalance, filteredBalance, this);

        var scrollPane = new JScrollPane(overviewTable);
        scrollPane.setComponentPopupMenu(new RightClickMenu(actions));
        add(scrollPane);

        JPanel topPanel = getTopPanel();
        topPanel.add(getOptionsPanel(), BorderLayout.NORTH);
        topPanel.add(getCrudPanel(), BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        setAmountCellRenderer();
        setDateTimeCellRenderer();
    }

    private JPanel getOptionsPanel() {
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 15, 0));
        optionsPanel.add(new JSeparator(JSeparator.VERTICAL));
        optionsPanel.add(incomingOutgoingPanel());
        optionsPanel.add(fromCalendar());
        optionsPanel.add(toCalendar());
        optionsPanel.add(fromValue());
        optionsPanel.add(toValue());
        optionsPanel.add(categories());
        optionsPanel.add(filterActionsPanel());

        return optionsPanel;
    }

    private JPanel incomingOutgoingPanel() {
        JPanel incomingOutgoingPanel = new JPanel(new BorderLayout());
        JPanel checkboxPanel = new JPanel();

        checkboxPanel.add(incomingCheckbox);
        checkboxPanel.add(outgoingCheckbox);
        incomingOutgoingPanel.add(new JLabel("Type"), BorderLayout.NORTH);
        incomingOutgoingPanel.add(checkboxPanel, BorderLayout.SOUTH);

        return incomingOutgoingPanel;
    }

    private JPanel fromCalendar() {
        JPanel panel = new JPanel(new BorderLayout(0, 7));
        panel.add(new JLabel("Date From"), BorderLayout.NORTH);
        panel.add(dateFrom, BorderLayout.CENTER);

        return panel;
    }

    private JPanel toCalendar() {
        JPanel panel = new JPanel(new BorderLayout(0, 7));

        panel.add(new JLabel("Date To"), BorderLayout.NORTH);
        panel.add(dateTo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel fromValue() {
        JPanel panel = new JPanel(new BorderLayout(0, 7));

        panel.add(new JLabel("Amount from"), BorderLayout.NORTH);
        panel.add(new JLabel(" €"), BorderLayout.EAST);

        valueFrom.setPreferredSize(new Dimension(130, 25));
        panel.add(valueFrom, BorderLayout.CENTER);

        return panel;
    }

    private JPanel toValue() {
        JPanel panel = new JPanel(new BorderLayout(0, 7));

        panel.add(new JLabel("Amount to"), BorderLayout.NORTH);
        panel.add(new JLabel(" €"), BorderLayout.EAST);

        valueTo.setPreferredSize(new Dimension(130, 25));
        panel.add(valueTo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel categories() {
        JPanel categoriesPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel();

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(allCategories);
        buttonGroup.add(specifiedCategories);

        panel.add(allCategories);
        panel.add(specifiedCategories);

        fillComboBox();
        categoriesJComboBox.setPreferredSize(new Dimension(120, 25));
        categoriesJComboBox.setRenderer(new CategoryColorListCellRenderer());

        categoriesJComboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                categoriesJComboBox.removeAllItems();
                fillComboBox();
            }
        });

        categoriesJComboBox.setEnabled(false);
        allCategories.addActionListener(e -> specifiedCategorySetTrue());
        specifiedCategories.addActionListener(e -> specifiedCategorySetTrue());

        categoriesJComboBox.addItemListener(e -> changeComboColor());

        categoriesJComboBox.setUI(new CustomComboBoxUI());

        panel.add(categoriesJComboBox);
        categoriesJComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        categoriesPanel.add(new JLabel("Category"), BorderLayout.NORTH);
        categoriesPanel.add(panel, BorderLayout.SOUTH);

        return categoriesPanel;
    }

    private void changeComboColor() {
        Category c = (Category) categoriesJComboBox.getSelectedItem();
        if (c != null) {
            categoriesJComboBox.setBackground(c.getColor());
        } else {
            categoriesJComboBox.setBackground(Color.WHITE);
        }
    }

    private void specifiedCategorySetTrue() {
        if (specifiedCategories.isSelected()) {
            categoriesJComboBox.setEnabled(true);
            changeComboColor();
        } else {
            categoriesJComboBox.setEnabled(false);
            categoriesJComboBox.setBackground(Color.WHITE);
            categoriesJComboBox.setSelectedIndex(0);
        }
    }

    private void fillComboBox() {
        List<Category> categoryList = categoryRepository.findAll();
        categoriesJComboBox.addItem(null);
        for (Category category : categoryList) {
            categoriesJComboBox.addItem(category);
        }
    }

    private JPanel filterActionsPanel() {
        JPanel panel = new JPanel();

        filterButton.setMnemonic(KeyEvent.VK_I);
        filterButton.addActionListener(l -> filter.filterRows());
        panel.add(filterButton);

        resetFilterButton.setMnemonic(KeyEvent.VK_R);
        resetFilterButton.addActionListener(e -> filter.resetFilter());
        panel.add(resetFilterButton);

        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        return panel;
    }

    private void setAmountCellRenderer() {
        overviewTable.getColumnModel().getColumn(2).setCellRenderer(new AmountCellRenderer());
    }

    private void setDateTimeCellRenderer() {
        overviewTable.getColumnModel().getColumn(3).setCellRenderer(new DateTimeCellRenderer());
    }

    public JCheckBox getIncomingCheckbox() {
        return incomingCheckbox;
    }

    public JCheckBox getOutgoingCheckbox() {
        return outgoingCheckbox;
    }

    public DatePicker getDateFrom() {
        return dateFrom;
    }

    public DatePicker getDateTo() {
        return dateTo;
    }

    public JFormattedTextField getValueFrom() {
        return valueFrom;
    }

    public JFormattedTextField getValueTo() {
        return valueTo;
    }

    public JRadioButton getAllCategories() {
        return allCategories;
    }

    public JRadioButton getSpecifiedCategories() {
        return specifiedCategories;
    }

    public JComboBox<Category> getCategoriesJComboBox() {
        return categoriesJComboBox;
    }

    public JCheckBox getNonRepeatingCheckbox() {
        return nonRepeatingCheckbox;
    }

    public JCheckBox getRepeatingCheckbox() {
        return repeatingCheckbox;
    }
}
