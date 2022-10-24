package cz.muni.fi.pv168.cashflowevidence.model;

import javax.swing.*;
import java.util.List;


public class TransactionListModel extends AbstractListModel<Transaction> {

    private final List<Transaction> categories;

    public TransactionListModel(List<Transaction> categories) {
        this.categories = categories;
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Transaction getElementAt(int index) {
        return categories.get(index);
    }
}
