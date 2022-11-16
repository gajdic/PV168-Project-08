package cz.muni.fi.pv168.cashflowevidence.data.adapter;

import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidCSVFormatException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

public interface TransactionSerializer {

    Transaction deserialize(String csvLine) throws InvalidCSVFormatException;

    String serialize(Transaction transaction);
}
