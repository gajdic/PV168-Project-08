package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.model.Transaction;

import java.util.Collection;

public interface TransactionExporter {
    public void exportJson(Collection<Transaction> transactions, String filePath);
}
