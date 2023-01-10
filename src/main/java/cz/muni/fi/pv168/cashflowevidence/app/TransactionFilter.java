package cz.muni.fi.pv168.cashflowevidence.app;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.cashflowevidence.app.overview.TransactionsOverview;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    private final JTable overviewTable;

    // incoming/outgoing
    private final JCheckBox incomingCheckbox;
    private final JCheckBox outgoingCheckbox;

    // dates
    private final DatePicker dateFrom;
    private final DatePicker dateTo;

    // values (€)
    private final JFormattedTextField valueFrom;
    private final JFormattedTextField valueTo;

    // categories
    private final JRadioButton allCategories;
    private final JRadioButton specifiedCategories;
    private final JComboBox<Category> categoriesJComboBox;

    // balance
    private final JLabel totalBalance;
    private final JLabel filteredBalance;

    public TransactionFilter(JTable overviewTable, JLabel totalBalance, JLabel filteredBalance, TransactionsOverview overview) {
        this.overviewTable = overviewTable;
        incomingCheckbox = overview.getIncomingCheckbox();
        outgoingCheckbox = overview.getOutgoingCheckbox();
        dateFrom = overview.getDateFrom();
        dateTo = overview.getDateTo();
        valueFrom = overview.getValueFrom();
        valueTo = overview.getValueTo();
        allCategories = overview.getAllCategories();
        specifiedCategories = overview.getSpecifiedCategories();
        categoriesJComboBox = overview.getCategoriesJComboBox();

        this.totalBalance = totalBalance;
        this.filteredBalance = filteredBalance;
        filteredBalance.setText(calculateBalance());
    }

    public void filterRows() {
        TableModel model = overviewTable.getModel();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.addRowSorterListener(e -> filteredBalance.setText(calculateBalance()));

        overviewTable.setRowSorter(sorter);
        RowFilter<TableModel, Integer> filter = RowFilter.andFilter(evaluate());
        sorter.setRowFilter(filter);

        filteredBalance.setText(calculateBalance());
    }

    private List<RowFilter<TableModel, Integer>> evaluate() {
        List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();

        if (dateFrom.getDate() != null) {
            filters.add(dateFromFilter());
        } if (dateTo.getDate() != null) {
            filters.add(dateToFilter());
        } if (valueFrom.isValid() && valueFrom.getValue() != null) {
            filters.add(valueFromFilter());
        } if (valueTo.isValid() && valueTo.getValue() != null) {
            filters.add(valueToFilter());
        } if (specifiedCategories.isSelected()) {
            filters.add(categoryFilter());
        }

        filters.add(typeFilter());

        return filters;
    }

    private RowFilter<TableModel, Integer> typeFilter() {

        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                TransactionType type = (TransactionType) entry.getValue(5);
                boolean includeBoth = !incomingCheckbox.isSelected() && !outgoingCheckbox.isSelected();

                return includeBoth ||
                        (incomingCheckbox.isSelected() && type == TransactionType.Incoming) ||
                        (outgoingCheckbox.isSelected() && type == TransactionType.Outgoing);
            }
        };
    }

    private RowFilter<TableModel, Integer> dateFromFilter() {
        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                LocalDate minimumDate = dateFrom.getDate().minusDays(1);
                LocalDateTime rowDateTime = (LocalDateTime) entry.getValue(3);
                LocalDate rowDate = rowDateTime.toLocalDate();
                return minimumDate.isBefore(rowDate);
            }
        };
    }

    private RowFilter<TableModel, Integer> dateToFilter() {
        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                LocalDate maximumDate = dateTo.getDate().plusDays(1);
                LocalDateTime rowDateTime = (LocalDateTime) entry.getValue(3);
                LocalDate rowDate = rowDateTime.toLocalDate();
                return maximumDate.isAfter(rowDate);
            }
        };
    }

    private RowFilter<TableModel, Integer> valueFromFilter() {
        return new RowFilter<>() {
            final Long minValue = (Long) valueFrom.getValue();
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return Double.parseDouble(entry.getValue(2).toString()) >= minValue;
            }
        };
    }

    private RowFilter<TableModel, Integer> valueToFilter() {
        return new RowFilter<>() {
            final Long maxValue = (Long) valueTo.getValue();
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return Double.parseDouble(entry.getValue(2).toString()) <= maxValue;
            }
        };
    }

    private RowFilter<TableModel, Integer> categoryFilter() {
        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                Category category = (Category) categoriesJComboBox.getSelectedItem();
                if (category == null) {
                    return entry.getValue(4) == null;
                }

                return entry.getValue(4) != null && entry.getValue(4).equals(category);
            }
        };
    }

    public void resetFilter() {
        resetIncomingAndOutgoing();
        resetDates();
        resetValues();
        resetCategories();
        overviewTable.setRowSorter(null);
        filteredBalance.setText(totalBalance.getText());
    }

    private void resetIncomingAndOutgoing() {
        incomingCheckbox.setSelected(false);
        outgoingCheckbox.setSelected(false);
    }

    private void resetDates() {
        dateFrom.clear();
        dateTo.clear();
    }

    private void resetValues() {
        valueFrom.setValue(null);
        valueTo.setValue(null);
    }

    private void resetCategories() {
        allCategories.setSelected(true);
        categoriesJComboBox.setEnabled(false);
    }

    private String calculateBalance() {
        double amount = 0;
        for (int i = 0; i < overviewTable.getRowCount(); i++) {
            double cellAmount = (Double) overviewTable.getValueAt(i, 2);
            if (overviewTable.getValueAt(i, 5) == TransactionType.Incoming) {
                amount += cellAmount;
            }
            else {
                amount -= cellAmount;
            }
        }
        return Transaction.formatAmount(amount) + " €";
    }
}
