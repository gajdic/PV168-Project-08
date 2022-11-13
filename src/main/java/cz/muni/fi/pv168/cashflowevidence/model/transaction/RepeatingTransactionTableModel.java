package cz.muni.fi.pv168.cashflowevidence.model.transaction;

import cz.muni.fi.pv168.cashflowevidence.model.Column;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// TODO somehow use TransactionTableModel here

public class RepeatingTransactionTableModel extends AbstractTableModel {

    private final List<RepeatingTransaction> repeatingTransactions;

    private final List<Column<RepeatingTransaction, ?>> columns = List.of(
            Column.editable("Name", String.class, RepeatingTransaction::getName, RepeatingTransaction::setName),
            Column.editable("Description", String.class, RepeatingTransaction::getDescription, RepeatingTransaction::setDescription),
            Column.editable("Amount (€)", Double.class, RepeatingTransaction::getAmount, RepeatingTransaction::setAmount),
            Column.editable("Created At", LocalDateTime.class, RepeatingTransaction::getDateTime, RepeatingTransaction::setDate),
            Column.editable("Category", Category.class, RepeatingTransaction::getCategory, RepeatingTransaction::setCategory),
            Column.readonly("Type", TransactionType.class, RepeatingTransaction::getTransactionType),
            Column.editable("Start date", LocalDate.class, RepeatingTransaction::getDateFrom, RepeatingTransaction::setDateFrom),
            Column.editable("End date", LocalDate.class, RepeatingTransaction::getDateTo, RepeatingTransaction::setDateTo),
            Column.editable("Repeats every (days)", Integer.class, RepeatingTransaction::getDays, RepeatingTransaction::setDays)
    );

    public RepeatingTransactionTableModel(Collection<RepeatingTransaction> repeatingTransactions) {
        this.repeatingTransactions = new ArrayList<>(repeatingTransactions);
    }

    @Override
    public int getRowCount() {
        return repeatingTransactions.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var transaction = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(transaction);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var transaction = getEntity(rowIndex);
        columns.get(columnIndex).setValue(value, transaction);
    }

    public RepeatingTransaction getEntity(int rowIndex) {
        return repeatingTransactions.get(rowIndex);
    }

    public List<RepeatingTransaction> getEntities() {
        return Collections.unmodifiableList(repeatingTransactions);
    }

    public void addRow(RepeatingTransaction transaction) {
        int newRowIndex = repeatingTransactions.size();
        repeatingTransactions.add(transaction);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(RepeatingTransaction transaction) {
        int rowIndex = repeatingTransactions.indexOf(transaction);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        repeatingTransactions.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
