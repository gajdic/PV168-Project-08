package cz.muni.fi.pv168.cashflowevidence.model;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {

    private final List<Transaction> transactions;

    private final List<Column<Transaction, ?>> columns = List.of(
            Column.editable("Name", String.class, Transaction::getName, Transaction::setName),
            Column.editable("Description", String.class, Transaction::getDescription, Transaction::setDescription),
            Column.editable("Amount ($)", Double.class, Transaction::getAmount, Transaction::setAmount),
            Column.editable("Date", LocalDateTime.class, Transaction::getDateTime, Transaction::setDateTime),
            Column.editable("Category", Category.class, Transaction::getCategory, Transaction::setCategory),
            Column.readonly("Type", TransactionType.class, Transaction::getTransactionType),
            Column.readonly("Repeating", String.class, t -> String.valueOf(t.isRepeating()))
    );

    public TransactionTableModel(Collection<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);

        //tmp
    }

    @Override
    public int getRowCount() {
        return transactions.size();
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

    public Transaction getEntity(int rowIndex) {
        return transactions.get(rowIndex);
    }

    public List<Transaction> getEntities() {
        return Collections.unmodifiableList(transactions);
    }

    public void addRow(Transaction transaction) {
        int newRowIndex = transactions.size();
        transactions.add(transaction);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Transaction transaction) {
        int rowIndex = transactions.indexOf(transaction);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        transactions.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
