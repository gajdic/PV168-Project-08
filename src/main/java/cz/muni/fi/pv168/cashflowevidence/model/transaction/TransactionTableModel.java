package cz.muni.fi.pv168.cashflowevidence.model.transaction;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.Column;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {

    private final Repository<Transaction> transactions;

    private final List<Column<Transaction, ?>> columns = List.of(
            Column.editable("Name", String.class, Transaction::getName, Transaction::setName),
            Column.editable("Description", String.class, Transaction::getDescription, Transaction::setDescription),
            Column.editable("Amount (€)", Double.class, Transaction::getAmount, Transaction::setAmount),
            Column.editable("Date", LocalDateTime.class, Transaction::getDateTime, Transaction::setDate),
            Column.editable("Category", Category.class, Transaction::getCategory, Transaction::setCategory),
            Column.readonly("Type", TransactionType.class, Transaction::getTransactionType)
    );

    public TransactionTableModel(Repository<Transaction> transactions) {
        this.transactions = transactions;

        //tmp
    }

    @Override
    public int getRowCount() {
        return transactions.getSize();
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
        return transactions.findByIndex(rowIndex).orElseThrow();
    }

    public List<Transaction> getEntities() {
        return transactions.findAll();
    }

    public void addRow(Transaction transaction) {
        int newRowIndex = transactions.getSize();
        transactions.create(transaction);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(int rowIndex, Transaction transaction) {
        transactions.update(transaction);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        transactions.deleteByIndex(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
