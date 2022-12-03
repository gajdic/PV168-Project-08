package cz.muni.fi.pv168.cashflowevidence.data.manipulation.old;

import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.util.Collection;

public interface TransactionImporter {
    Collection<Transaction> importRecords(String filePath) throws InvalidImportException;
    ImportFormat getFormat();
}
