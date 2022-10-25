package cz.muni.fi.pv168.cashflowevidence.model;

import javax.swing.*;
import java.util.List;


public class TransactionListModel extends AbstractListModel<Transaction> {

    private final List<Transaction> transactions;

    public TransactionListModel(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public int getSize() {
        return transactions.size();
    }

    @Override
    public Transaction getElementAt(int index) {
        return transactions.get(index);
    }
}
