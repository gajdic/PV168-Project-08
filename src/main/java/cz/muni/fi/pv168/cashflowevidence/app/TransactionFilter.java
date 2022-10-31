package cz.muni.fi.pv168.cashflowevidence.app;

import com.github.lgooddatepicker.components.DatePicker;
import cz.muni.fi.pv168.cashflowevidence.model.Category;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionType;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    JTable overviewTable;

    //incoming/outgoing
    private final JCheckBox incomingCheckbox;
    private final JCheckBox outgoingCheckbox;

    //dates
    private final DatePicker dateFrom;
    private final DatePicker dateTo;

    //values
    private final JFormattedTextField valueFrom;
    private final JFormattedTextField valueTo;

    //categorises
    private final JRadioButton allCategories;
    private final JRadioButton specifiedCategories;
    private final JComboBox<Category> categoriesJComboBox;

    //repeating transactions
    private final JRadioButton allTransactions;
    private final JRadioButton onlyNonRepeating;
    private final JRadioButton onlyRepeating;

    //balance
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
        allTransactions = overview.getAllTransactions();
        onlyNonRepeating = overview.getOnlyNonRepeating();
        onlyRepeating = overview.getOnlyRepeating();

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

        if (!incomingCheckbox.isSelected()) {
            filters.add(incomingFilter());
        } if (!outgoingCheckbox.isSelected()) {
            filters.add(outgoingFilter());
        } if (dateFrom.getDate() != null) {
            filters.add(dateFromFilter());
        } if (dateTo.getDate() != null) {
            filters.add(dateToFilter());
        } if (valueFrom.isValid() && valueFrom.getValue() != null) {
            filters.add(valueFromFilter());
        } if (valueTo.isValid() && valueTo.getValue() != null) {
            filters.add(valueToFilter());
        } if (specifiedCategories.isSelected()) {
            filters.add(categoryFilter());
        } if (onlyRepeating.isSelected()) {
            filters.add(repeatingTransactionsFilter());
        } if (onlyNonRepeating.isSelected()) {
            filters.add(nonRepeatingTransactionsFilter());
        }
        return filters;
    }

    private RowFilter<TableModel, Integer> incomingFilter() {

        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return !(entry.getValue(5) == TransactionType.Incoming);
            }
        };
    }

    private RowFilter<TableModel, Integer> outgoingFilter() {

        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return !(entry.getValue(5) == TransactionType.Outgoing);
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
                return entry.getValue(4).equals(category);
            }
        };
    }

    private RowFilter<TableModel, Integer> repeatingTransactionsFilter() {
        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return entry.getValue(6).equals(String.valueOf(true));
            }
        };
    }

    private RowFilter<TableModel, Integer> nonRepeatingTransactionsFilter() {
        return new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return entry.getValue(6).equals(String.valueOf(false));
            }
        };
    }

    public void resetFilter() {
        resetIncomingAndOutgoing();
        resetDates();
        resetValues();
        resetCategories();
        resetRepeatingTransactions();
        overviewTable.setRowSorter(null);
        filteredBalance.setText(totalBalance.getText());
    }

    private void resetIncomingAndOutgoing() {
        incomingCheckbox.setSelected(true);
        outgoingCheckbox.setSelected(true);
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
        categoriesJComboBox.setVisible(false);
    }

    private void resetRepeatingTransactions() {
        allTransactions.setSelected(true);
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
        return Transaction.formatAmount(amount) + " $";
    }
}
