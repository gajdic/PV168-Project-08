package cz.muni.fi.pv168.cashflowevidence.data.manipulation.old;

import cz.muni.fi.pv168.cashflowevidence.exceptions.ExportFailedException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.Collection;

public interface TransactionExporter {
    void exportTransactions(Collection<Transaction> transactions, String filePath) throws ExportFailedException;

    ExportFormat getFormat();
}
